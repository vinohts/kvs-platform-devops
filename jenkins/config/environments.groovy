/**
 * ============================================================================
 * KVS Platform
 * Environment Configuration
 * ============================================================================
 */

def getConfigurations() {

    return [

        develop: [

            ENVIRONMENT    : "Development",
            AWS_REGION     : "ap-northeast-1",
            AWS_CREDENTIAL : "aws-dev"

        ],

        release: [

            ENVIRONMENT    : "Production",
            AWS_REGION     : "ap-southeast-2",
            AWS_CREDENTIAL : "aws-prod"

        ]

    ]

}

return this