def get() {
    return [
        GITHUB_ORG       : "vinohts",
        DEFAULT_AGENT    : "any",
        PIPELINE_TIMEOUT : 60,
        RETENTION_BUILDS : 20
    ]
}

return this