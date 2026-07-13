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
                     * Temporary branch value.
                     * Later this will come from GitHub Multibranch.
                     */
                    String currentBranch = "develop"

                    def branch = load "jenkins/stages/branch.groovy"

                    def config = branch.call(currentBranch)

                    echo ""
                    echo "========== Deployment Summary =========="
                    echo "Environment    : ${config.ENVIRONMENT}"
                    echo "AWS Region     : ${config.AWS_REGION}"
                    echo "Credential     : ${config.AWS_CREDENTIAL}"
                    echo "========================================"

                }

            }

        }

    }

}