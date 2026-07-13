/**
 * ============================================================================
 * KVS Platform
 * Artifact Management
 * ============================================================================
 *
 * Requires the "Pipeline: AWS Steps" plugin (jenkinsci/pipeline-aws-plugin).
 * Uses withAWS + s3Upload/s3Doesobjectexist instead of shelling out to the
 * AWS CLI, so there is no dependency on aws.exe being installed on the agent
 * and no risk of a step accidentally running outside the credential scope.
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
     * Upload Artifact + Verify (single credential scope)
     */
    withAWS(
        credentials: buildInfo.AWS_CREDENTIAL,
        region: buildInfo.AWS_REGION
    ) {

        s3Upload(
            file   : artifact.ARTIFACT_PATH,
            bucket : bucket,
            path   : objectKey
        )

        boolean exists = s3DoesObjectExist(
            bucket : bucket,
            path   : objectKey
        )

        if (!exists) {
            error "Artifact verification failed: s3://${bucket}/${objectKey} not found after upload"
        }

    }

    /*
     * Remove Local Artifact
     */
    bat """
    if exist "${artifact.ARTIFACT_PATH}" del "${artifact.ARTIFACT_PATH}"
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