def call(Map buildInfo, String artifactVersion) {

    def constants = load "jenkins/common/constants.groovy"
    def logger    = load "jenkins/common/logger.groovy"
    def c = constants.get()

    logger.section("Application Deployment")

    String bucket = c.S3_BUCKETS[buildInfo.GIT_BRANCH]
    String artifactName = "${c.PROJECT_NAME}-${artifactVersion}.zip"
    String objectKey = "${buildInfo.GIT_BRANCH}/${artifactVersion}/${artifactName}"
    String s3Uri = "s3://${bucket}/${objectKey}"
    String tfEnv = (buildInfo.GIT_BRANCH == 'release') ? 'prod' : 'dev'

    String unitFileContent = """[Unit]
Description=KvsOrderHub Application
After=network.target

[Service]
WorkingDirectory=/opt/app
ExecStart=/opt/app/${c.APP_ASSEMBLY_NAME}
Restart=always
RestartSec=5
Environment=ASPNETCORE_URLS=http://0.0.0.0:80
Environment=DB_CONNECTION_STRING=Host=localhost;Port=5432;Username=postgres;Password=postgres;Database=kvsorderhub

[Install]
WantedBy=multi-user.target
"""

    String unitFileBase64 = unitFileContent.bytes.encodeBase64().toString()

    String instanceId

    withAWS(credentials: buildInfo.AWS_CREDENTIAL, region: buildInfo.AWS_REGION) {

        instanceId = bat(
            script: """
            @echo off
            aws ec2 describe-instances ^
                --filters "Name=tag:Name,Values=${c.PROJECT_NAME}-${tfEnv}-app-01" "Name=instance-state-name,Values=running" ^
                --query "Reservations[0].Instances[0].InstanceId" ^
                --output text
            """,
            returnStdout: true
        ).trim().readLines().last().trim()

        if (!instanceId || instanceId == "None") {
            error "No running instance found tagged ${c.PROJECT_NAME}-${tfEnv}-app-01"
        }

        logger.kv("Target Instance", instanceId)

        String commandId = bat(
            script: """
            @echo off
            aws ssm send-command ^
                --instance-ids ${instanceId} ^
                --document-name "AWS-RunShellScript" ^
                --comment "Deploy ${artifactVersion}" ^
                --parameters commands="set -e","sudo mkdir -p /opt/app","sudo systemctl stop kvsorderhub || true","sudo aws s3 cp ${s3Uri} /tmp/${artifactName}","sudo rm -rf /opt/app/*","sudo unzip -o /tmp/${artifactName} -d /opt/app > /tmp/unzip.log 2>&1 || true","test -f /opt/app/${c.APP_ASSEMBLY_NAME}","sudo chmod +x /opt/app/${c.APP_ASSEMBLY_NAME}","sudo rm -f /tmp/${artifactName}","echo ${unitFileBase64} | base64 -d | sudo tee /etc/systemd/system/kvsorderhub.service > /dev/null","sudo systemctl daemon-reload","sudo systemctl enable kvsorderhub","sudo systemctl restart kvsorderhub" ^
                --query "Command.CommandId" ^
                --output text
            """,
            returnStdout: true
        ).trim().readLines().last().trim()

        logger.kv("SSM Command ID", commandId)

        sleep(time: 15, unit: "SECONDS")

        String status = bat(
            script: """
            @echo off
            aws ssm get-command-invocation ^
                --command-id ${commandId} ^
                --instance-id ${instanceId} ^
                --query "Status" ^
                --output text
            """,
            returnStdout: true
        ).trim().readLines().last().trim()

        logger.kv("Command Status", status)

        if (status != "Success") {

            String output = bat(
                script: """
                @echo off
                aws ssm get-command-invocation ^
                    --command-id ${commandId} ^
                    --instance-id ${instanceId} ^
                    --query "StandardErrorContent" ^
                    --output text
                """,
                returnStdout: true
            ).trim()

            error "Deployment command failed on ${instanceId}. Status: ${status}. Error: ${output}"

        }

    }

    logger.kv("Bucket", bucket)
    logger.kv("Object Key", objectKey)
    logger.kv("Deploy Target", "/opt/app on ${instanceId} (systemd: kvsorderhub)")

    return [
        ARTIFACT_NAME : artifactName,
        INSTANCE_ID   : instanceId,
        DEPLOY_PATH   : "/opt/app"
    ]

}

return this