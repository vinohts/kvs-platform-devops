/*
==============================================================================
RDS Subnet Group
==============================================================================
*/

resource "aws_db_subnet_group" "main" {

  name = "${local.project_name}-${var.environment}-db-subnet-group"

  subnet_ids = [
    aws_subnet.private_a.id,
    aws_subnet.private_c.id
  ]

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-db-subnet-group"

    }

  )

}
/*
==============================================================================
RDS Security Group
==============================================================================
*/

resource "aws_security_group" "rds" {

  name = "${local.project_name}-${var.environment}-rds-sg"

  description = "Security Group for RDS PostgreSQL"

  vpc_id = aws_vpc.main.id

  ingress {

    description = "PostgreSQL from App"

    from_port = 5432

    to_port = 5432

    protocol = "tcp"

    security_groups = [

      aws_security_group.app.id

    ]

  }

  egress {

    from_port = 0

    to_port = 0

    protocol = "-1"

    cidr_blocks = ["0.0.0.0/0"]

  }

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-rds-sg"

    }

  )

}
/*
==============================================================================
RDS PostgreSQL Instance
==============================================================================
*/

resource "aws_db_instance" "main" {

  identifier = "${local.project_name}-${var.environment}-db"

  engine = "postgres"

  engine_version = "16"

  instance_class = "db.t3.micro"

  allocated_storage = 20

  storage_type = "gp3"

  db_name = "kvsorderhub"

  username = var.db_username

  password = var.db_password

  db_subnet_group_name = aws_db_subnet_group.main.name

  vpc_security_group_ids = [
    aws_security_group.rds.id
  ]

  publicly_accessible = false

  multi_az = false

  skip_final_snapshot = true

  backup_retention_period = 1

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-db"

    }

  )

}
/*
==============================================================================
RDS Endpoint Output
Purpose: shown after apply, used to update deploy.groovy's connection string
==============================================================================
*/

output "rds_endpoint" {

  value = aws_db_instance.main.address

}