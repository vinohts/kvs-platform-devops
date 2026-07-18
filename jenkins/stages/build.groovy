def call(Map buildInfo) {

    def constants = load "jenkins/common/constants.groovy"
    def logger    = load "jenkins/common/logger.groovy"
    def c = constants.get()

    logger.section("Application Build")

    dir(c.APP_REPO_NAME) {

        logger.info("Application Repository : ${c.APP_REPO_NAME}")
        logger.info("Workspace              : ${pwd()}")

        if (!fileExists(c.README_MARKER)) {
            error "${c.README_MARKER} not found. Invalid application repository."
        }

        logger.info("Repository validation successful.")

        if (isUnix()) {
            sh "ls -la"
        } else {
            bat "dir"
        }

    }

    logger.info("Build completed successfully.")

}

return this