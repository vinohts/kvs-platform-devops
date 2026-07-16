/*
==============================================================================
Virtual Private Cloud (VPC)
Purpose:
Creates the primary VPC for the KVS Platform infrastructure.
==============================================================================
*/

resource "aws_vpc" "main" {

  cidr_block = "10.0.0.0/16"

  enable_dns_support = true

  enable_dns_hostnames = true

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-vpc"

    }

  )

}
