/*
==============================================================================
Internet Gateway (IGW)

Purpose:
Provides Internet connectivity for resources in the public subnets.
==============================================================================
*/

resource "aws_internet_gateway" "main" {

  vpc_id = aws_vpc.main.id

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-igw"

    }

  )

}