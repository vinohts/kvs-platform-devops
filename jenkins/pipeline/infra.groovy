def currentBranch
def config
def buildInfo

pipeline {

    agent any

    parameters {
        choice(
            name: 'TF_ENV',
            choices: ['dev', 'prod'],
            description: 'Terraform environment to target'
        )
        booleanParam(
            name: 'AUTO_APPROVE',
            defaultValue: false,
            description: 'Skip manual approval before apply (use with caution)'
        )
    }

    stages {

        stage('Branch Validation') {
            steps {
                script {
                    currentBranch = params.TF_ENV == 'prod' ? 'release' : 'develop'
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

        stage('Terraform Init') {
            steps {
                dir("terraform/environments/${params.TF_ENV}") {
                    withAWS(credentials: buildInfo.AWS_CREDENTIAL, region: buildInfo.AWS_REGION) {
                        bat "terraform init -input=false"
                    }
                }
            }
        }

        stage('Terraform Plan') {
            steps {
                dir("terraform/environments/${params.TF_ENV}") {
                    withAWS(credentials: buildInfo.AWS_CREDENTIAL, region: buildInfo.AWS_REGION) {
                        bat "terraform plan -input=false -out=tfplan"
                    }
                }
            }
        }

        stage('Approval') {
            when { expression { return !params.AUTO_APPROVE } }
            steps {
                input message: "Apply Terraform plan for ${params.TF_ENV}?"
            }
        }

        stage('Terraform Apply') {
            steps {
                dir("terraform/environments/${params.TF_ENV}") {
                    withAWS(credentials: buildInfo.AWS_CREDENTIAL, region: buildInfo.AWS_REGION) {
                        bat "terraform apply -input=false tfplan"
                    }
                }
            }
        }

        stage('Summary') {
            steps {
                script {
                    echo ""
                    echo "========================================"
                    echo "Infrastructure Pipeline"
                    echo "========================================"
                    echo "Environment    : ${params.TF_ENV}"
                    echo "AWS Region     : ${buildInfo.AWS_REGION}"
                    echo "Auto Approved  : ${params.AUTO_APPROVE}"
                    echo "========================================"
                }
            }
        }

    }

}