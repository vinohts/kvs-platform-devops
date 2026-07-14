/**
 * ============================================================================
 * KVS Platform
 * Application Continuous Deployment Pipeline
 * ============================================================================
 */

def currentBranch
def artifactVersion
def config
def buildInfo
def deploymentArtifact

pipeline {

    agent any

    parameters {

        string(
            name: 'ARTIFACT_VERSION',
            defaultValue: 'develop-12',
            description: 'Artifact Version to Deploy'
        )

    }

    stages {

        stage('Branch Validation') {

            steps {

                script {

                    /*
                     * Temporary Branch
                     */
                    currentBranch = "develop"

                    artifactVersion = params.ARTIFACT_VERSION

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

        stage('Deploy') {

    steps {

        script {

            def deploy = load "jenkins/stages/deploy.groovy"

            deploymentArtifact = deploy.call(
                buildInfo,
                artifactVersion
            )

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
                    echo "Environment      : ${buildInfo.ENVIRONMENT}"
                    echo "AWS Region       : ${buildInfo.AWS_REGION}"
                    echo "Artifact Version : ${artifactVersion}"
                    echo "Downloaded File  : ${deploymentArtifact.ARTIFACT_NAME}"
                    echo "Download Path    : ${deploymentArtifact.DOWNLOAD_PATH}"
                    echo "========================================"

                }

            }

        }

    }

}