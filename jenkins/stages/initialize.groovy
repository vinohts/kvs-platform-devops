def call(String branchName, Map config) {

    def constants = load "jenkins/common/constants.groovy"
    def logger    = load "jenkins/common/logger.groovy"
    def utils     = load "jenkins/common/utils.groovy"
    def c = constants.get()

    logger.section("Pipeline Initialization")

    def buildInfo = [
        PIPELINE_NAME  : c.PROJECT_NAME,
        JOB_NAME       : env.JOB_NAME,
        BUILD_NUMBER   : env.BUILD_NUMBER,
        BUILD_URL      : env.BUILD_URL,
        WORKSPACE      : env.WORKSPACE,
        GIT_BRANCH     : branchName,
        ENVIRONMENT    : config.ENVIRONMENT,
        AWS_REGION     : config.AWS_REGION,
        AWS_CREDENTIAL : config.AWS_CREDENTIAL,
        BUILD_VERSION  : utils.buildVersion(branchName, env.BUILD_NUMBER),
        BUILD_TIME     : utils.timestamp()
    ]

    logger.kv("Pipeline Name", buildInfo.PIPELINE_NAME)
    logger.kv("Job Name", buildInfo.JOB_NAME)
    logger.kv("Build Number", buildInfo.BUILD_NUMBER)
    logger.kv("Git Branch", buildInfo.GIT_BRANCH)
    logger.kv("Environment", buildInfo.ENVIRONMENT)
    logger.kv("AWS Region", buildInfo.AWS_REGION)
    logger.kv("Build Version", buildInfo.BUILD_VERSION)
    logger.kv("Workspace", buildInfo.WORKSPACE)

    return buildInfo

}

return this