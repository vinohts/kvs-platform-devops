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
     * S3 Information
     */
    String bucket = "kvs-platform-artifacts"

    String artifactName = "kvs-platform-${artifactVersion}.zip"

    String objectKey = "${buildInfo.GIT_BRANCH}/${artifactVersion}/${artifactName}"

    /*
     * Download Location
     */
    String downloadDirectory = "${env.WORKSPACE}\\download"

    String downloadPath = "${downloadDirectory}\\${artifactName}"

    bat """
    if not exist "${downloadDirectory}" mkdir "${downloadDirectory}"
    """

    /*
     * Download Artifact
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
     * Verify Download
     */
    if (!fileExists(downloadPath)) {

        error "Artifact download failed."

    }

    echo ""
    echo "Artifact Download Successful"
    echo "Bucket          : ${bucket}"
    echo "Object Key      : ${objectKey}"
    echo "Download Path   : ${downloadPath}"

    return [

        ARTIFACT_NAME : artifactName,

        DOWNLOAD_PATH : downloadPath

    ]

}

return this