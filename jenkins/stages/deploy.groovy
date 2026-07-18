def call(Map buildInfo, String artifactVersion) {

    def constants = load "jenkins/common/constants.groovy"
    def logger    = load "jenkins/common/logger.groovy"
    def utils     = load "jenkins/common/utils.groovy"
    def c = constants.get()

    logger.section("Application Deployment")

    String artifactName = "${c.PROJECT_NAME}-${artifactVersion}.zip"
    String objectKey = "${buildInfo.GIT_BRANCH}/${artifactVersion}/${artifactName}"

    String downloadDirectory = "${env.WORKSPACE}\\download"
    String deployDirectory = "${env.WORKSPACE}\\deploy"
    String downloadPath = "${downloadDirectory}\\${artifactName}"

    utils.recreateDir(downloadDirectory)
    utils.recreateDir(deployDirectory)

    withAWS(credentials: buildInfo.AWS_CREDENTIAL, region: buildInfo.AWS_REGION) {
        s3Download(
            bucket : c.S3_ARTIFACT_BUCKET,
            path   : objectKey,
            file   : downloadPath,
            force  : true
        )
    }

    if (!fileExists(downloadPath)) {
        error "Artifact download failed."
    }

    powershell """
    Expand-Archive `
        -Path "${downloadPath}" `
        -DestinationPath "${deployDirectory}" `
        -Force
    """

    if (!fileExists("${deployDirectory}\\${c.README_MARKER}")) {
        error "Artifact extraction failed."
    }

    logger.kv("Bucket", c.S3_ARTIFACT_BUCKET)
    logger.kv("Object Key", objectKey)
    logger.kv("Download Path", downloadPath)
    logger.kv("Deploy Path", deployDirectory)

    bat """
    dir "${deployDirectory}"
    """

    return [
        ARTIFACT_NAME : artifactName,
        DOWNLOAD_PATH : downloadPath,
        DEPLOY_PATH   : deployDirectory
    ]

}

return this