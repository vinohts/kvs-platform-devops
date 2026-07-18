def get() {
    return [
        PROJECT_NAME        : "kvs-platform",
        APP_REPO_NAME       : "kvs-platform-app",
        APP_REPO_URL        : "https://github.com/vinohts/kvs-platform-app.git",
        APP_REPO_CREDENTIAL : "github-pat",
        S3_ARTIFACT_BUCKET  : "kvs-platform-artifacts",
        README_MARKER       : "README.md"
    ]
}

return this