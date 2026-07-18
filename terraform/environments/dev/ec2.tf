/*
==============================================================================
KVS Platform
Application EC2 Instance
==============================================================================
*/

resource "aws_instance" "app" {

  ami = data.aws_ami.amazon_linux_2023.id

  instance_type = "t3.small"

  subnet_id = aws_subnet.public_a.id

  vpc_security_group_ids = [
    aws_security_group.app.id
  ]

  iam_instance_profile = aws_iam_instance_profile.ec2.name

  associate_public_ip_address = true

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-app-01"

    }

  )

}