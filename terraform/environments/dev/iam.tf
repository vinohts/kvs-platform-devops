/*
==============================================================================
KVS Platform
IAM Configuration
==============================================================================
*/

resource "aws_iam_role" "ec2" {

  name = "${local.project_name}-${var.environment}-ec2-role"

  assume_role_policy = jsonencode({

    Version = "2012-10-17"

    Statement = [

      {

        Effect = "Allow"

        Principal = {

          Service = "ec2.amazonaws.com"

        }

        Action = "sts:AssumeRole"

      }

    ]

  })

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-ec2-role"

    }

  )

}
/*
==============================================================================
Attach AmazonSSMManagedInstanceCore Policy
==============================================================================
*/

resource "aws_iam_role_policy_attachment" "ssm" {

  role = aws_iam_role.ec2.name

  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"

}
/*
==============================================================================
EC2 Instance Profile
==============================================================================
*/

resource "aws_iam_instance_profile" "ec2" {

  name = "${local.project_name}-${var.environment}-instance-profile"

  role = aws_iam_role.ec2.name

}