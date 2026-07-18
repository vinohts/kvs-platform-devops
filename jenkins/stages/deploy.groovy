def call(Map buildInfo, String artifactVersion) {

    def constants = load "jenkins/common/constants.groovy"
    def logger    = load "jenkins/common/logger.groovy"
    def c = constants.get()

    logger.section("Application Deployment")

    String bucket = c.S3_BUCKETS[buildInfo.GIT_BRANCH]
    String artifactName = "${c.PROJECT_NAME}-${artifactVersion}.zip"
    String objectKey = "${buildInfo.GIT_BRANCH}/${artifactVersion}/${artifactName}"
    String s3Uri = "s3://${bucket}/${objectKey}"

    String instanceId

    withAWS(credentials: buildInfo.AWS_CREDENTIAL, region: buildInfo.AWS_REGION) {

        instanceId = bat(
            script: """
            @echo off
            aws ec2 describe-instances ^
                --filters "Name=tag:Name,Values=${c.PROJECT_NAME}-${buildInfo.GIT_BRANCH}-app-01" "Name=instance-state-name,Values=running" ^
                --query "Reservations[0].Instances[0].InstanceId" ^
                --output text
            """,
            returnStdout: true
        ).trim().readLines().last().trim()

        if (!instanceId || instanceId == "None") {
            error "No running instance found tagged ${c.PROJECT_NAME}-${buildInfo.GIT_BRANCH}-app-01"
        }

        logger.kv("Target Instance", instanceId)

        String commandId = bat(
            script: """
            @echo off
            aws ssm send-command ^
                --instance-ids ${instanceId} ^
                --document-name "AWS-RunShellScript" ^
                --comment "Deploy ${artifactVersion}" ^
                --parameters commands="sudo mkdir -p /opt/app","sudo aws s3 cp ${s3Uri} /tmp/${artifactName}","sudo rm -rf /opt/app/*","sudo unzip -o /tmp/${artifactName} -d /opt/app","sudo rm -f /tmp/${artifactName}" ^
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
    logger.kv("Deploy Target", "/opt/app on ${instanceId}")

    return [
        ARTIFACT_NAME : artifactName,
        INSTANCE_ID   : instanceId,
        DEPLOY_PATH   : "/opt/app"
    ]

}

return this