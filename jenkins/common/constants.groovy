def get() {
    return [
        PROJECT_NAME        : "kvs-platform",
        APP_REPO_NAME       : "kvs-platform-app",
        APP_REPO_URL        : "https://github.com/vinohts/kvs-platform-app.git",
        APP_REPO_CREDENTIAL : "github-pat",
        README_MARKER       : "README.md",
        S3_BUCKETS : [
            develop : "kvs-platform-artifacts-develop",
            release : "kvs-platform-artifacts-release"
        ]
    ]
}

return this