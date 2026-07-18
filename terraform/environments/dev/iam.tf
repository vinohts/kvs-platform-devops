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
S3 Read Access for Deployment Artifacts
==============================================================================
*/

resource "aws_iam_role_policy" "s3_artifacts" {

  name = "${local.project_name}-${var.environment}-s3-artifacts"

  role = aws_iam_role.ec2.id

  policy = jsonencode({

    Version = "2012-10-17"

    Statement = [

      {

        Effect = "Allow"

        Action = [

          "s3:GetObject"

        ]

        Resource = [

          "arn:aws:s3:::kvs-platform-artifacts-develop/*",
          "arn:aws:s3:::kvs-platform-artifacts-release/*"

        ]

      }

    ]

  })

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