/**
 * ============================================================================
 * KVS Platform
 * Application Repository Checkout Stage
 * ============================================================================
 */

def call(String branchName) {

    echo "--------------------------------------------------"
    echo "Application Repository Checkout"
    echo "--------------------------------------------------"

    dir("kvs-platform-app") {

        checkout([
            $class: 'GitSCM',
            branches: [[name: "*/${branchName}"]],
            userRemoteConfigs: [[
                credentialsId: 'github-pat',
                url: 'https://github.com/vinohts/kvs-platform-app.git'
            ]]
        ])

    }

    echo "Repository  : kvs-platform-app"
    echo "Branch      : ${branchName}"
    echo "Status      : Checkout Successful"

}

return this