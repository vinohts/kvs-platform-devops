def call(Map buildInfo, Map artifact) {

    def constants = load "jenkins/common/constants.groovy"
    def logger    = load "jenkins/common/logger.groovy"
    def c = constants.get()

    logger.section("Artifact Management")

    String bucket = c.S3_BUCKETS[buildInfo.GIT_BRANCH]
    String objectKey = "${buildInfo.GIT_BRANCH}/${buildInfo.BUILD_VERSION}/${artifact.ARTIFACT_NAME}"

    withAWS(credentials: buildInfo.AWS_CREDENTIAL, region: buildInfo.AWS_REGION) {

        s3Upload(file: artifact.ARTIFACT_PATH, bucket: bucket, path: objectKey)

        boolean exists = s3DoesObjectExist(bucket: bucket, path: objectKey)
        if (!exists) {
            error "Artifact verification failed: s3://${bucket}/${objectKey} not found after upload"
        }

    }

    bat """
    if exist "${artifact.ARTIFACT_PATH}" del "${artifact.ARTIFACT_PATH}"
    """

    logger.kv("Bucket", bucket)
    logger.kv("Object Key", objectKey)

    return [
        BUCKET     : bucket,
        OBJECT_KEY : objectKey,
        S3_URI     : "s3://${bucket}/${objectKey}"
    ]

}

return this