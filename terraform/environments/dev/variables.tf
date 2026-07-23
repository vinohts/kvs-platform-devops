/*
==============================================================================
KVS Platform
Terraform Variables
==============================================================================
*/

/*
==============================================================================
AWS Region
==============================================================================
*/

variable "aws_region" {

  description = "AWS Region for resource deployment."

  type = string

}

/*
==============================================================================
Environment Name
==============================================================================
*/

variable "environment" {

  description = "Deployment environment."

  type = string

}

/*
==============================================================================
Database Username
==============================================================================
*/

variable "db_username" {

  description = "Master username for the RDS PostgreSQL instance."

  type = string

}

/*
==============================================================================
Database Password
==============================================================================
*/

variable "db_password" {

  description = "Master password for the RDS PostgreSQL instance."

  type = string

  sensitive = true

}