/*
==============================================================================
Terraform State Bucket
==============================================================================
*/

resource "aws_s3_bucket" "tfstate" {

  bucket = "${local.project_name}-${var.environment}-tfstate"

  tags = merge(

    local.common_tags,

    {

      Name    = "${local.project_name}-${var.environment}-tfstate"
      Purpose = "terraform-state"

    }

  )

}
/*
==============================================================================
State Bucket Versioning
==============================================================================
*/

resource "aws_s3_bucket_versioning" "tfstate" {

  bucket = aws_s3_bucket.tfstate.id

  versioning_configuration {

    status = "Enabled"

  }

}
/*
==============================================================================
State Bucket Encryption
==============================================================================
*/

resource "aws_s3_bucket_server_side_encryption_configuration" "tfstate" {

  bucket = aws_s3_bucket.tfstate.id

  rule {

    apply_server_side_encryption_by_default {

      sse_algorithm = "AES256"

    }

  }

}
/*
==============================================================================
State Bucket Public Access Block
==============================================================================
*/

resource "aws_s3_bucket_public_access_block" "tfstate" {

  bucket = aws_s3_bucket.tfstate.id

  block_public_acls = true

  block_public_policy = true

  ignore_public_acls = true

  restrict_public_buckets = true

}