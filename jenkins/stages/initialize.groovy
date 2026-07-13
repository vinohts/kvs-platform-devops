/**
 * ============================================================================
 * KVS Platform
 * Pipeline Initialization Stage
 * ============================================================================
 */

def call(String branchName, Map config) {

    echo "--------------------------------------------------"
    echo "Pipeline Initialization"
    echo "--------------------------------------------------"

    def buildInfo = [

        PIPELINE_NAME : "KVS Platform",

        JOB_NAME      : env.JOB_NAME,

        BUILD_NUMBER  : env.BUILD_NUMBER,

        BUILD_URL     : env.BUILD_URL,

        WORKSPACE     : env.WORKSPACE,

        GIT_BRANCH    : branchName,

        ENVIRONMENT   : config.ENVIRONMENT,

        AWS_REGION    : config.AWS_REGION,

        AWS_CREDENTIAL: config.AWS_CREDENTIAL,

        BUILD_VERSION : "${branchName}-${env.BUILD_NUMBER}",

        BUILD_TIME    : new Date().format("yyyy-MM-dd HH:mm:ss")

    ]

    echo "Pipeline Name : ${buildInfo.PIPELINE_NAME}"
    echo "Job Name      : ${buildInfo.JOB_NAME}"
    echo "Build Number  : ${buildInfo.BUILD_NUMBER}"
    echo "Git Branch    : ${buildInfo.GIT_BRANCH}"
    echo "Environment   : ${buildInfo.ENVIRONMENT}"
    echo "AWS Region    : ${buildInfo.AWS_REGION}"
    echo "Build Version : ${buildInfo.BUILD_VERSION}"
    echo "Workspace     : ${buildInfo.WORKSPACE}"

    return buildInfo

}

return this