/**
 * ============================================================================
 * KVS Platform
 * Branch Validation Stage
 * ============================================================================
 */

def call(String branchName) {

    echo "--------------------------------------------------"
    echo "Branch Validation"
    echo "--------------------------------------------------"

    def envConfig = load 'jenkins/config/environments.groovy'

    def environments = envConfig.getConfigurations()

    if (!environments.containsKey(branchName)) {
        error "Unsupported branch : ${branchName}"
    }

    def config = environments[branchName]

    echo "Branch          : ${branchName}"
    echo "Environment     : ${config.ENVIRONMENT}"
    echo "AWS Region      : ${config.AWS_REGION}"
    echo "AWS Credential  : ${config.AWS_CREDENTIAL}"

    return config

}

return this