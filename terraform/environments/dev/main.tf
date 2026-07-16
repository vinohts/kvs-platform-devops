/*
==============================================================================
KVS Platform
Main Terraform Configuration
==============================================================================
*/

locals {

  project_name = "kvs-platform"

  common_tags = {

    Project     = local.project_name
    Environment = var.environment
    ManagedBy   = "Terraform"

  }

}
