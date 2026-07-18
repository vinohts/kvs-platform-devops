def call(Map buildInfo) {

    def constants = load "jenkins/common/constants.groovy"
    def logger    = load "jenkins/common/logger.groovy"
    def utils     = load "jenkins/common/utils.groovy"
    def c = constants.get()

    logger.section("Workspace Cleanup")

    utils.removeDir("${env.WORKSPACE}\\package")
    utils.removeDir("${env.WORKSPACE}\\${c.APP_REPO_NAME}")

    logger.info("Workspace cleanup completed.")

}

return this