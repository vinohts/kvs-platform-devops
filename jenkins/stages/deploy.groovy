/**
 * ============================================================================
 * KVS Platform
 * Download Artifact Stage
 * ============================================================================
 */

def call(Map buildInfo) {

    echo "--------------------------------------------------"
    echo "Download Artifact"
    echo "--------------------------------------------------"

    String bucket = "kvs-platform-artifacts"

    String artifactName = "kvs-platform-${buildInfo.BUILD_VERSION}.zip"

    String objectKey = "${buildInfo.GIT_BRANCH}/${buildInfo.BUILD_VERSION}/${artifactName}"

    String downloadDirectory = "${env.WORKSPACE}\\downloads"

    String downloadPath = "${downloadDirectory}\\${artifactName}"

    bat """
    if not exist "${downloadDirectory}" (
        mkdir "${downloadDirectory}"
    )
    """

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

    echo ""
    echo "Artifact Downloaded Successfully"
    echo "Bucket          : ${bucket}"
    echo "Object Key      : ${objectKey}"
    echo "Download Path   : ${downloadPath}"

    return [

        BUCKET : bucket,

        OBJECT_KEY : objectKey,

        ARTIFACT_NAME : artifactName,

        DOWNLOAD_PATH : downloadPath

    ]

}

return this