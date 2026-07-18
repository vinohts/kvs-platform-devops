def recreateDir(String path) {
    bat """
    if exist "${path}" rmdir /S /Q "${path}"
    mkdir "${path}"
    """
}

def removeDir(String path) {
    bat """
    if exist "${path}" rmdir /S /Q "${path}"
    """
}

def timestamp() {
    return new Date().format("yyyy-MM-dd HH:mm:ss")
}

def buildVersion(String branchName, String buildNumber) {
    return "${branchName}-${buildNumber}"
}

return this