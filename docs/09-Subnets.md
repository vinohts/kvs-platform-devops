# 09 - Amazon Subnets

> **Project:** KVS Platform DevOps
>
> **Document Version:** 1.0
>
> **Author:** Vinoth Kumar
>
> **Service:** Amazon VPC - Subnets
>
> **Status:** Completed

---

# Document Objective

The objective of this document is to understand Amazon VPC Subnets, their purpose, the difference between Public and Private Subnets, and how they are provisioned using Terraform.

After completing this document, you should be able to:

- Understand what a Subnet is
- Explain the difference between Public and Private Subnets
- Understand Availability Zones
- Plan subnet CIDR ranges
- Deploy subnets using Terraform
- Verify subnet configuration in AWS
- Follow AWS networking best practices

---

# Table of Contents

1. Introduction
2. What is a Subnet?
3. Why Do We Need Subnets?
4. Public vs Private Subnets
5. Availability Zones
6. CIDR Planning
7. KVS Platform Network Design
8. Terraform Implementation
9. Resource Explanation
10. Terraform Workflow
11. AWS Console Verification
12. Best Practices
13. Common Mistakes
14. Interview Questions
15. Summary

---

# 1. Introduction

A subnet is a logical subdivision of a Virtual Private Cloud (VPC).

Instead of placing all AWS resources in one large network, the VPC is divided into multiple smaller networks called subnets.

This improves:

- Organization
- Security
- High Availability
- Scalability

Every AWS resource such as EC2, RDS and Load Balancer is deployed inside a subnet.

---

# 2. What is a Subnet?

A subnet is a smaller network created from the VPC's CIDR block.

Example

```
VPC

10.0.0.0/16
```

Split into

```
Public Subnet A

10.0.1.0/24

Public Subnet C

10.0.2.0/24

Private Subnet A

10.0.11.0/24

Private Subnet C

10.0.12.0/24
```

Each subnet has its own IP address range.

---

# Real-Life Analogy

Imagine a large apartment complex.

```
Apartment Complex

(VPC)

│

├── Block A

├── Block B

├── Block C

└── Block D
```

Each apartment block represents a subnet.

Although all blocks belong to the same apartment complex, each has its own residents and facilities.

Similarly, subnets divide a VPC into multiple logical networks.

---

# 3. Why Do We Need Subnets?

Subnets provide:

- Better organization
- Network isolation
- Security
- High Availability
- Easy management
- Scalability

Instead of placing everything in one network:

```
Web Servers

Application Servers

Databases

Bastion Host
```

They are separated into dedicated networks.

---

# 4. Public vs Private Subnets

## Public Subnet

A public subnet has a route to an Internet Gateway.

Resources inside a public subnet can communicate with the Internet.

Typical resources:

- Bastion Host
- Application Load Balancer
- NAT Gateway

---

## Private Subnet

A private subnet does not have a direct route to an Internet Gateway.

Resources inside a private subnet cannot be accessed directly from the Internet.

Typical resources:

- Application Servers
- Databases
- Internal Services

---

# Public vs Private

| Feature | Public Subnet | Private Subnet |
|----------|---------------|----------------|
| Internet Access | Yes | No |
| Internet Gateway Route | Yes | No |
| Public IP | Usually Yes | Usually No |
| Typical Resources | ALB, Bastion | EC2, RDS |

---

# 5. Availability Zones

Availability Zones (AZs) are physically separate data centers within an AWS Region.

Example

```
Tokyo Region

↓

ap-northeast-1a

ap-northeast-1c
```

To improve availability, resources should be distributed across multiple Availability Zones.

---

# 6. CIDR Planning

Our VPC

```
10.0.0.0/16
```

Subnet allocation

```
Public Subnet A

10.0.1.0/24

Public Subnet C

10.0.2.0/24

Private Subnet A

10.0.11.0/24

Private Subnet C

10.0.12.0/24
```

Each /24 subnet provides 256 IP addresses (AWS reserves 5 addresses in every subnet).

---

# 7. KVS Platform Network Design

```
Tokyo Region

┌──────────────────────────────────────┐
│              VPC                     │
│           10.0.0.0/16                │
│                                      │
│  Public Subnet A   10.0.1.0/24       │
│  Public Subnet C   10.0.2.0/24       │
│                                      │
│  Private Subnet A  10.0.11.0/24      │
│  Private Subnet C  10.0.12.0/24      │
└──────────────────────────────────────┘
```

This design provides:

- High Availability
- Fault Tolerance
- Multi-AZ Deployment
- Production-ready architecture

---

# 8. Terraform Implementation

Example

```hcl
resource "aws_subnet" "public_a" {

  vpc_id = aws_vpc.main.id

  cidr_block = "10.0.1.0/24"

  availability_zone = "ap-northeast-1a"

  map_public_ip_on_launch = true

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-public-subnet-a"

    }

  )

}
```

Private subnet

```hcl
resource "aws_subnet" "private_a" {

  vpc_id = aws_vpc.main.id

  cidr_block = "10.0.11.0/24"

  availability_zone = "ap-northeast-1a"

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-private-subnet-a"

    }

  )

}
```

---

# 9. Resource Explanation

## Resource Type

```
aws_subnet
```

Creates a subnet inside a VPC.

---

## VPC ID

```hcl
vpc_id = aws_vpc.main.id
```

Associates the subnet with the VPC.

---

## CIDR Block

Defines the IP address range for the subnet.

Example

```
10.0.1.0/24
```

---

## Availability Zone

Determines the physical AWS data center where the subnet exists.

Example

```
ap-northeast-1a
```

---

## map_public_ip_on_launch

```
true
```

Automatically assigns a public IP address to EC2 instances launched in the subnet.

Used only for public subnets.

---

## Tags

Used for identification and automation.

Example

```
Project

Environment

ManagedBy

Name
```

---

# 10. Terraform Workflow

```bash
terraform fmt

terraform validate

terraform plan

terraform apply
```

Terraform automatically:

- Creates subnets
- Associates them with the VPC
- Stores subnet IDs in the state file

---

# 11. AWS Console Verification

Navigate to:

```
AWS Console

↓

VPC

↓

Subnets
```

Verify:

✔ Four subnets created

✔ Correct CIDR Blocks

✔ Correct Availability Zones

✔ Correct VPC

✔ Auto Assign Public IP enabled only for Public Subnets

✔ Tags

---

# 12. Best Practices

- Use multiple Availability Zones.
- Keep web servers in Public Subnets.
- Keep application servers in Private Subnets.
- Keep databases in Private Subnets.
- Use meaningful subnet names.
- Avoid overlapping CIDR ranges.
- Tag every subnet.

---

# 13. Common Mistakes

- Creating all resources in one subnet.
- Using overlapping CIDR blocks.
- Launching databases in Public Subnets.
- Forgetting to enable Auto Assign Public IP for Public Subnets.
- Using only one Availability Zone.

---

# 14. Interview Questions

1. What is a subnet?
2. Why do we divide a VPC into subnets?
3. Difference between Public and Private Subnets?
4. What is map_public_ip_on_launch?
5. Why deploy resources across multiple Availability Zones?
6. Can two subnets have the same CIDR?
7. Can an EC2 instance exist without a subnet?
8. Why are databases usually placed in Private Subnets?

---

# 15. Summary

In this document we learned:

- Amazon Subnets
- Public vs Private Subnets
- Availability Zones
- CIDR Planning
- Terraform Subnet Resources
- AWS Verification
- Best Practices
- Common Mistakes

Subnets divide a VPC into smaller logical networks, allowing resources to be organized securely and efficiently. A well-designed subnet architecture is the foundation of highly available and production-ready AWS environments.

---
