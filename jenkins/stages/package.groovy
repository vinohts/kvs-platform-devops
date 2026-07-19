def call(Map buildInfo, Map buildOutput) {

    def constants = load "jenkins/common/constants.groovy"
    def logger    = load "jenkins/common/logger.groovy"
    def utils     = load "jenkins/common/utils.groovy"
    def c = constants.get()

    logger.section("Application Packaging")

    def packageDirectory = "${env.WORKSPACE}\\package"
    def artifactName = "${c.PROJECT_NAME}-${buildInfo.BUILD_VERSION}.zip"

    utils.recreateDir(packageDirectory)

    dir(buildOutput.PUBLISH_DIR) {
        bat """
        powershell Compress-Archive ^
            -Path * ^
            -DestinationPath "${packageDirectory}\\${artifactName}" ^
            -Force
        """
    }

    logger.kv("Package Directory", packageDirectory)
    logger.kv("Artifact Name", artifactName)
    logger.kv("Packaging Status", "SUCCESS")

    return [
        ARTIFACT_NAME : artifactName,
        ARTIFACT_PATH : "${packageDirectory}\\${artifactName}"
    ]

}

return this