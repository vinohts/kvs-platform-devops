/*
==============================================================================
Subnets

Purpose:
Creates public and private subnets for the KVS Platform.
==============================================================================
*/
resource "aws_subnet" "public_a" {

  vpc_id = aws_vpc.main.id

  cidr_block = "10.0.1.0/24"

  availability_zone = "ap-northeast-1a"

  map_public_ip_on_launch = true

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-public-a"

      Type = "Public"

    }

  )

}

resource "aws_subnet" "public_c" {

  vpc_id = aws_vpc.main.id

  cidr_block = "10.0.2.0/24"

  availability_zone = "ap-northeast-1c"

  map_public_ip_on_launch = true

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-public-c"

      Type = "Public"

    }

  )

}
/*
==============================================================================
Private Subnet A
==============================================================================
*/

resource "aws_subnet" "private_a" {

  vpc_id = aws_vpc.main.id

  cidr_block = "10.0.11.0/24"

  availability_zone = "ap-northeast-1a"

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-private-a"

      Type = "Private"

    }

  )

}

/*
==============================================================================
Private Subnet C
==============================================================================
*/

resource "aws_subnet" "private_c" {

  vpc_id = aws_vpc.main.id

  cidr_block = "10.0.12.0/24"

  availability_zone = "ap-northeast-1c"

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-private-c"

      Type = "Private"

    }

  )

}