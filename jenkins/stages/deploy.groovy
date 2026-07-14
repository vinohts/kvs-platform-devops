/**
 * ============================================================================
 * KVS Platform
 * Deployment Stage
 * ============================================================================
 */

def call(Map buildInfo, String artifactVersion) {

    echo "--------------------------------------------------"
    echo "Application Deployment"
    echo "--------------------------------------------------"

    /*
     * ----------------------------------------------------------------
     * S3 Information
     * ----------------------------------------------------------------
     */
    String bucket = "kvs-platform-artifacts"

    String artifactName = "kvs-platform-${artifactVersion}.zip"

    String objectKey = "${buildInfo.GIT_BRANCH}/${artifactVersion}/${artifactName}"

    /*
     * ----------------------------------------------------------------
     * Workspace Directories
     * ----------------------------------------------------------------
     */
    String downloadDirectory = "${env.WORKSPACE}\\download"

    String deployDirectory = "${env.WORKSPACE}\\deploy"

    String downloadPath = "${downloadDirectory}\\${artifactName}"

    /*
     * ----------------------------------------------------------------
     * Prepare Directories
     * ----------------------------------------------------------------
     */
    bat """
    if exist "${downloadDirectory}" rmdir /S /Q "${downloadDirectory}"
    if exist "${deployDirectory}" rmdir /S /Q "${deployDirectory}"

    mkdir "${downloadDirectory}"
    mkdir "${deployDirectory}"
    """

    /*
     * ----------------------------------------------------------------
     * Download Artifact
     * ----------------------------------------------------------------
     */
    withAWS(
        credentials: buildInfo.AWS_CREDENTIAL,
        region: buildInfo.AWS_REGION
    ) {

        s3Download(
            bucket: bucket,
            path: objectKey,
            file: downloadPath,
            force: true
        )

    }

    /*
     * ----------------------------------------------------------------
     * Verify Download
     * ----------------------------------------------------------------
     */
    if (!fileExists(downloadPath)) {

        error "Artifact download failed."

    }

    /*
     * ----------------------------------------------------------------
     * Extract Artifact
     * ----------------------------------------------------------------
     */
    powershell """

    Expand-Archive `
        -Path "${downloadPath}" `
        -DestinationPath "${deployDirectory}" `
        -Force

    """

    /*
     * ----------------------------------------------------------------
     * Verify Extraction
     * ----------------------------------------------------------------
     */
    if (!fileExists("${deployDirectory}\\README.md")) {

        error "Artifact extraction failed."

    }

    echo ""
    echo "Artifact Download Successful"
    echo "Bucket          : ${bucket}"
    echo "Object Key      : ${objectKey}"
    echo "Download Path   : ${downloadPath}"

    echo ""
    echo "Artifact Extracted Successfully"
    echo "Deploy Path     : ${deployDirectory}"

    bat """
    dir "${deployDirectory}"
    """

    return [

        ARTIFACT_NAME : artifactName,

        DOWNLOAD_PATH : downloadPath,

        DEPLOY_PATH : deployDirectory

    ]

}

return this