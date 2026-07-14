/**
 * ============================================================================
 * KVS Platform
 * Application Continuous Deployment Pipeline
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
                     * Temporary Branch
                     * Later this will come from Multibranch.
                     */
                    currentBranch = "develop"

                    def branch = load "jenkins/stages/branch.groovy"

                    config = branch.call(currentBranch)

                }

            }

        }

        stage('Initialize') {

            steps {

                script {

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
                    echo "Deployment Pipeline"
                    echo "========================================"
                    echo "Pipeline Name : ${buildInfo.PIPELINE_NAME}"
                    echo "Environment   : ${buildInfo.ENVIRONMENT}"
                    echo "AWS Region    : ${buildInfo.AWS_REGION}"
                    echo "========================================"

                }

            }

        }

    }

}