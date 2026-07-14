/**
 * ============================================================================
 * KVS Platform
 * Cleanup Stage
 * ============================================================================
 */

def call(Map buildInfo) {

    echo "--------------------------------------------------"
    echo "Workspace Cleanup"
    echo "--------------------------------------------------"

    /*
     * Remove Package Directory
     */
    bat """
    if exist "${env.WORKSPACE}\\package" (
        rmdir /s /q "${env.WORKSPACE}\\package"
    )
    """

    /*
     * Remove Application Repository
     */
    bat """
    if exist "${env.WORKSPACE}\\kvs-platform-app" (
        rmdir /s /q "${env.WORKSPACE}\\kvs-platform-app"
    )
    """

    echo ""
    echo "Workspace cleanup completed."

}

return this