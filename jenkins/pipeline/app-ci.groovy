/**
 * ============================================================================
 * KVS Platform
 * Application Continuous Integration Pipeline
 * ============================================================================
 */

pipeline {

    agent any

    stages {

        stage('Branch Validation') {

            steps {

                script {

                    /*
                     * ----------------------------------------------------------------
                     * Temporary Branch
                     * ----------------------------------------------------------------
                     */
                    String currentBranch = "develop"

                    /*
                     * ----------------------------------------------------------------
                     * Branch Validation
                     * ----------------------------------------------------------------
                     */
                    def branch = load "jenkins/stages/branch.groovy"
                    def config = branch.call(currentBranch)

                    /*
                     * ----------------------------------------------------------------
                     * Checkout Application Repository
                     * ----------------------------------------------------------------
                     */
                    def checkoutStage = load "jenkins/stages/checkout.groovy"
                    checkoutStage.call(currentBranch)

                    /*
                     * ----------------------------------------------------------------
                     * Pipeline Initialization
                     * ----------------------------------------------------------------
                     */
                    def initialize = load "jenkins/stages/initialize.groovy"
                    def buildInfo = initialize.call(currentBranch, config)

                    /*
                     * ----------------------------------------------------------------
                     * Build Application
                     * ----------------------------------------------------------------
                     */
                    def build = load "jenkins/stages/build.groovy"
                    build.call(buildInfo)

                    /*
                     * ----------------------------------------------------------------
                     * Package Application
                     * ----------------------------------------------------------------
                     */
                    def packageStage = load "jenkins/stages/package.groovy"
                    def artifact = packageStage.call(buildInfo)

                    /*
                     * ----------------------------------------------------------------
                     * Publish Artifact to Amazon S3
                     * ----------------------------------------------------------------
                     */
                    def artifactStage = load "jenkins/stages/artifact.groovy"
                    def s3Artifact = artifactStage.call(buildInfo, artifact)

                    /*
                     * ----------------------------------------------------------------
                     * Pipeline Summary
                     * ----------------------------------------------------------------
                     */
                    echo ""
                    echo "========================================"
                    echo "Pipeline Summary"
                    echo "========================================"
                    echo "Pipeline Name : ${buildInfo.PIPELINE_NAME}"
                    echo "Build Version : ${buildInfo.BUILD_VERSION}"
                    echo "Environment   : ${buildInfo.ENVIRONMENT}"
                    echo "AWS Region    : ${buildInfo.AWS_REGION}"
                    echo "Artifact      : ${artifact.ARTIFACT_NAME}"
                    echo "S3 Bucket     : ${s3Artifact.BUCKET}"
                    echo "S3 Object     : ${s3Artifact.OBJECT_KEY}"
                    echo "S3 URI        : ${s3Artifact.S3_URI}"
                    echo "========================================"

                }

            }

        }

    }

}