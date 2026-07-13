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
                     * Later this will come from GitHub Multibranch Pipeline
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
                     * Pipeline Summary
                     * ----------------------------------------------------------------
                     */
                    echo ""
                    echo "========================================"
                    echo "Pipeline Summary"
                    echo "========================================"
                    echo "Pipeline Name : ${buildInfo.PIPELINE_NAME}"
                    echo "Job Name      : ${buildInfo.JOB_NAME}"
                    echo "Build Number  : ${buildInfo.BUILD_NUMBER}"
                    echo "Build Version : ${buildInfo.BUILD_VERSION}"
                    echo "Git Branch    : ${buildInfo.GIT_BRANCH}"
                    echo "Environment   : ${buildInfo.ENVIRONMENT}"
                    echo "AWS Region    : ${buildInfo.AWS_REGION}"
                    echo "Workspace     : ${buildInfo.WORKSPACE}"
                    echo "========================================"

                }

            }

        }

    }

}