/**
 * ============================================================================
 * KVS Platform
 * Artifact Management
 * ============================================================================
 */

def call(Map buildInfo, Map artifact) {

    echo "--------------------------------------------------"
    echo "Artifact Management"
    echo "--------------------------------------------------"

    /*
     * S3 Location
     */
    String bucket = "kvs-platform-artifacts"

    String objectKey = "${buildInfo.GIT_BRANCH}/${buildInfo.BUILD_VERSION}/${artifact.ARTIFACT_NAME}"

    /*
     * Upload Artifact
     */
    withCredentials([[
        $class: 'AmazonWebServicesCredentialsBinding',
        credentialsId: buildInfo.AWS_CREDENTIAL
    ]]) {

        bat """
        aws s3 cp ^
        "${artifact.ARTIFACT_PATH}" ^
        s3://${bucket}/${objectKey}
        """

    }

    /*
     * Verify Upload
     */
    bat """
    aws s3 ls s3://${bucket}/${objectKey}
    """

    /*
     * Remove Local Artifact
     */
    bat """
    del "${artifact.ARTIFACT_PATH}"
    """

    echo ""
    echo "Artifact Uploaded Successfully"
    echo "Bucket      : ${bucket}"
    echo "Object Key  : ${objectKey}"

    return [

        BUCKET : bucket,

        OBJECT_KEY : objectKey,

        S3_URI : "s3://${bucket}/${objectKey}"

    ]

}

return this