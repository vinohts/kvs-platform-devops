/**
 * ============================================================================
 * KVS Platform
 * Application Deployment Pipeline
 * ============================================================================
 */

def currentBranch
def config
def buildInfo
def downloadInfo

pipeline {

    agent any

    stages {

        stage('Branch Validation') {

            steps {

                script {

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

        stage('Download Artifact') {

            steps {

                script {

                    def download = load "jenkins/stages/download.groovy"
                    downloadInfo = download.call(buildInfo)

                }

            }

        }

        stage('Summary') {

            steps {

                script {

                    echo ""
                    echo "========================================"
                    echo "Deployment Summary"
                    echo "========================================"
                    echo "Pipeline Name : ${buildInfo.PIPELINE_NAME}"
                    echo "Build Version : ${buildInfo.BUILD_VERSION}"
                    echo "Environment   : ${buildInfo.ENVIRONMENT}"
                    echo "AWS Region    : ${buildInfo.AWS_REGION}"
                    echo "Artifact      : ${downloadInfo.ARTIFACT_NAME}"
                    echo "Download Path : ${downloadInfo.DOWNLOAD_PATH}"
                    echo "========================================"

                }

            }

        }

    }

}