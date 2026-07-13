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
                    currentBranch = "develop"

                    /*
                     * ----------------------------------------------------------------
                     * Branch Validation
                     * ----------------------------------------------------------------
                     */
                    def branch = load "jenkins/stages/branch.groovy"
                    config = branch.call(currentBranch)

                }

            }

        }

        stage('Checkout') {

            steps {

                script {

                    /*
                     * ----------------------------------------------------------------
                     * Checkout Application Repository
                     * ----------------------------------------------------------------
                     */
                    def checkoutStage = load "jenkins/stages/checkout.groovy"
                    checkoutStage.call(currentBranch)

                }

            }

        }

        stage('Initialize') {

            steps {

                script {

                    /*
                     * ----------------------------------------------------------------
                     * Pipeline Initialization
                     * ----------------------------------------------------------------
                     */
                    def initialize = load "jenkins/stages/initialize.groovy"
                    buildInfo = initialize.call(currentBranch, config)

                }

            }

        }

        stage('Build') {

            steps {

                script {

                    /*
                     * ----------------------------------------------------------------
                     * Build Application
                     * ----------------------------------------------------------------
                     */
                    def build = load "jenkins/stages/build.groovy"
                    build.call(buildInfo)

                }

            }

        }

        stage('Package') {

            steps {

                script {

                    /*
                     * ----------------------------------------------------------------
                     * Package Application
                     * ----------------------------------------------------------------
                     */
                    def packageStage = load "jenkins/stages/package.groovy"
                    artifact = packageStage.call(buildInfo)

                }

            }

        }

        stage('Publish') {

            steps {

                script {

                    /*
                     * ----------------------------------------------------------------
                     * Publish Artifact to Amazon S3
                     * ----------------------------------------------------------------
                     */
                    def artifactStage = load "jenkins/stages/artifact.groovy"
                    s3Artifact = artifactStage.call(buildInfo, artifact)

                }

            }

        }

        stage('Summary') {

            steps {

                script {

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