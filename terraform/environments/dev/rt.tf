/*
==============================================================================
Public Route Table
==============================================================================
*/

resource "aws_route_table" "public" {

  vpc_id = aws_vpc.main.id

  route {

    cidr_block = "0.0.0.0/0"

    gateway_id = aws_internet_gateway.main.id

  }

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-public-rt"

      Type = "Public"

    }

  )

}
/*
==============================================================================
Public Route Table Association - Public A
==============================================================================
*/

resource "aws_route_table_association" "public_a" {

  subnet_id = aws_subnet.public_a.id

  route_table_id = aws_route_table.public.id

}
/*
==============================================================================
Public Route Table Association - Public C
==============================================================================
*/

resource "aws_route_table_association" "public_c" {

  subnet_id = aws_subnet.public_c.id

  route_table_id = aws_route_table.public.id

}
/*
==============================================================================
Private Route Table
==============================================================================
*/

resource "aws_route_table" "private" {

  vpc_id = aws_vpc.main.id

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-private-rt"

      Type = "Private"

    }

  )

}
/*
==============================================================================
Private Route Table Association - Private A
==============================================================================
*/

resource "aws_route_table_association" "private_a" {

  subnet_id = aws_subnet.private_a.id

  route_table_id = aws_route_table.private.id

}
/*
==============================================================================
Private Route Table Association - Private C
==============================================================================
*/

resource "aws_route_table_association" "private_c" {

  subnet_id = aws_subnet.private_c.id

  route_table_id = aws_route_table.private.id

}