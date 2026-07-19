def call(Map buildInfo) {

    def constants = load "jenkins/common/constants.groovy"
    def logger    = load "jenkins/common/logger.groovy"
    def utils     = load "jenkins/common/utils.groovy"
    def c = constants.get()

    logger.section("Application Build")

    String publishDirectory = "${env.WORKSPACE}\\publish"

    utils.recreateDir(publishDirectory)

    dir(c.APP_REPO_NAME) {

        logger.info("Application Repository : ${c.APP_REPO_NAME}")
        logger.info("Workspace              : ${pwd()}")

        bat """
        dotnet publish ^
            --configuration Release ^
            --runtime linux-x64 ^
            --self-contained true ^
            --output "${publishDirectory}"
        """

    }

    if (!fileExists("${publishDirectory}\\${c.APP_ASSEMBLY_NAME}.dll")) {
        error "dotnet publish did not produce ${c.APP_ASSEMBLY_NAME}.dll. Build failed."
    }

    logger.info("dotnet publish completed successfully.")
    logger.kv("Publish Directory", publishDirectory)

    return [
        PUBLISH_DIR : publishDirectory
    ]

}

return this