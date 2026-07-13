/**
 * ============================================================================
 * KVS Platform
 * Application Build Stage
 * ============================================================================
 */

def call(Map buildInfo) {

    echo "--------------------------------------------------"
    echo "Application Build"
    echo "--------------------------------------------------"

    dir("kvs-platform-app") {

        echo "Application Repository : kvs-platform-app"
        echo "Workspace              : ${pwd()}"

        if (!fileExists("README.md")) {
            error "README.md not found. Invalid application repository."
        }

        echo "Repository validation successful."

        echo ""
        echo "Application Contents"
        echo "------------------------------"

        if (isUnix()) {
            sh "ls -la"
        } else {
            bat "dir"
        }

    }

    echo ""
    echo "Build completed successfully."

}

return this