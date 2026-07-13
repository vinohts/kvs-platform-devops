/**
 * ============================================================================
 * KVS Platform
 * Package Stage
 * ============================================================================
 */

def call(Map buildInfo) {

    echo "--------------------------------------------------"
    echo "Application Packaging"
    echo "--------------------------------------------------"

    def packageDirectory = "${env.WORKSPACE}\\package"

    def artifactName = "kvs-platform-${buildInfo.BUILD_VERSION}.zip"

    dir("kvs-platform-app") {

        bat """
        if not exist "${packageDirectory}" mkdir "${packageDirectory}"

        powershell Compress-Archive ^
            -Path * ^
            -DestinationPath "${packageDirectory}\\${artifactName}" ^
            -Force
        """

    }

    echo "Package Directory : ${packageDirectory}"
    echo "Artifact Name     : ${artifactName}"
    echo "Packaging Status  : SUCCESS"

    return [

        ARTIFACT_NAME : artifactName,

        ARTIFACT_PATH : "${packageDirectory}\\${artifactName}"

    ]

}

return this