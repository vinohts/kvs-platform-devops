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
            defaultValue: '',
            description: 'Previously published artifact version to roll back to, e.g. develop-14'
        )
    }

    stages {

        stage('Validate Input') {
            steps {
                script {
                    if (!params.ARTIFACT_VERSION?.trim()) {
                        error "ARTIFACT_VERSION is required, e.g. develop-14"
                    }
                    artifactVersion = params.ARTIFACT_VERSION
                    currentBranch = artifactVersion.tokenize('-')[0]
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

        stage('Rollback Deploy') {
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
                    echo "Rollback Pipeline"
                    echo "========================================"
                    echo "Environment      : ${buildInfo.ENVIRONMENT}"
                    echo "Rolled Back To   : ${artifactVersion}"
                    echo "Deployed File    : ${deploymentArtifact.ARTIFACT_NAME}"
                    echo "Deploy Path      : ${deploymentArtifact.DEPLOY_PATH}"
                    echo "========================================"
                }
            }
        }

    }

}