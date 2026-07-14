/**
 * ============================================================================
 * KVS Platform
 * Application Deployment Pipeline
 * ============================================================================
 */

def currentBranch
def config
def buildInfo

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

        stage('Summary') {

            steps {

                script {

                    echo ""
                    echo "========================================"
                    echo "Deployment Pipeline Summary"
                    echo "========================================"
                    echo "Pipeline Name : ${buildInfo.PIPELINE_NAME}"
                    echo "Build Version : ${buildInfo.BUILD_VERSION}"
                    echo "Environment   : ${buildInfo.ENVIRONMENT}"
                    echo "AWS Region    : ${buildInfo.AWS_REGION}"
                    echo "========================================"

                }

            }

        }

    }

}