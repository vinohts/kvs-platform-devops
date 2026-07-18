def currentBranch
def artifactVersion
def config
def buildInfo
def deploymentArtifact

pipeline {

    agent any

    parameters {

        choice(
            name: 'TARGET_ENV',
            choices: ['develop', 'release'],
            description: 'Environment to deploy to'
        )

        string(
            name: 'ARTIFACT_VERSION',
            defaultValue: '',
            description: 'Artifact version to deploy, e.g. develop-17 or release-4'
        )

    }

    stages {

        stage('Validate Input') {
            steps {
                script {
                    if (!params.ARTIFACT_VERSION?.trim()) {
                        error "ARTIFACT_VERSION is required, e.g. ${params.TARGET_ENV}-17"
                    }

                    currentBranch = params.TARGET_ENV
                    artifactVersion = params.ARTIFACT_VERSION

                    if (!artifactVersion.startsWith("${currentBranch}-")) {
                        error "ARTIFACT_VERSION '${artifactVersion}' does not match TARGET_ENV '${currentBranch}'. Expected it to start with '${currentBranch}-'."
                    }
                }
            }
        }

        stage('Branch Validation') {
            steps {
                script {
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
                    deploymentArtifact = deploy.call(buildInfo, artifactVersion)
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
                    echo "Target Env       : ${params.TARGET_ENV}"
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