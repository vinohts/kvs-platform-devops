# 07 - Amazon Virtual Private Cloud (VPC)

> **Project:** KVS Platform DevOps
>
> **Document Version:** 1.0
>
> **Author:** Vinoth Kumar
>
> **Service:** Amazon VPC
>
> **Status:** Completed

---

# Document Objective

The objective of this document is to understand Amazon Virtual Private Cloud (Amazon VPC), why it is required, how it works, and how it is provisioned using Terraform.

After completing this document, you should be able to:

- Understand the purpose of a VPC
- Explain CIDR blocks
- Understand DNS Support and DNS Hostnames
- Deploy a VPC using Terraform
- Verify the deployment in AWS
- Follow AWS best practices

---

# Table of Contents

1. Introduction
2. What is Amazon VPC?
3. Why Do We Need a VPC?
4. VPC Components
5. CIDR Block
6. DNS Support
7. DNS Hostnames
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

Amazon Virtual Private Cloud (Amazon VPC) is a logically isolated virtual network inside AWS.

Every AWS resource such as EC2, Load Balancer, RDS and ECS is deployed inside a VPC.

A VPC provides complete control over:

- IP Address Range
- Routing
- Internet Connectivity
- Security
- Network Isolation

---

# 2. What is Amazon VPC?

A VPC is similar to a private data center inside AWS.

Think of AWS as a large city.

Inside that city, you purchase your own land.

That land is your Virtual Private Cloud.

Only resources created inside your VPC belong to you.

---

# Real-Life Analogy

Imagine building a gated community.

```
AWS Cloud

---------------------------------------------------

Your Land (VPC)

---------------------------------------------------

Roads

Buildings

Security Gates

Visitors

Residents
```

Your land is isolated from everyone else's land.

Similarly,

A VPC isolates your AWS resources from other AWS customers.

---

# 3. Why Do We Need a VPC?

Without a VPC, there would be no logical boundary for our infrastructure.

A VPC provides:

- Network Isolation
- Private IP Addressing
- Routing Control
- Internet Connectivity
- Security Boundaries
- High Availability

---

# 4. VPC Components

A VPC acts as the foundation for networking.

```
VPC

│

├── Public Subnets

├── Private Subnets

├── Route Tables

├── Internet Gateway

├── NAT Gateway

├── Security Groups

└── Network ACLs
```

Everything begins with the VPC.

---

# 5. CIDR Block

Every VPC requires an IP address range.

For this project

```
10.0.0.0/16
```

Meaning

Network

```
10.0.x.x
```

Available IP Addresses

```
65,536
```

CIDR is used to divide the network into smaller subnetworks.

Example

```
10.0.1.0/24

10.0.2.0/24

10.0.3.0/24

10.0.4.0/24
```

---

# 6. DNS Support

Terraform Configuration

```hcl
enable_dns_support = true
```

Purpose

Allows AWS to resolve DNS names inside the VPC.

Without DNS Support

- Private DNS Resolution fails
- Internal communication becomes difficult

---

# 7. DNS Hostnames

Terraform Configuration

```hcl
enable_dns_hostnames = true
```

Purpose

Automatically assigns DNS hostnames to EC2 instances that have public IP addresses.

Example

```
ec2-54-12-34-56.ap-northeast-1.compute.amazonaws.com
```

---

# 8. Terraform Implementation

Terraform Resource

```hcl
resource "aws_vpc" "main" {

  cidr_block = "10.0.0.0/16"

  enable_dns_support = true

  enable_dns_hostnames = true

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-vpc"

    }

  )

}
```

---

# 9. Resource Explanation

### Resource Type

```
aws_vpc
```

Creates an Amazon VPC.

---

### Logical Name

```
main
```

Terraform's internal reference.

Example

```hcl
aws_vpc.main.id
```

---

### CIDR Block

```
10.0.0.0/16
```

Defines the IP address range of the VPC.

---

### DNS Support

```
enable_dns_support = true
```

Enables internal DNS resolution.

---

### DNS Hostnames

```
enable_dns_hostnames = true
```

Allows EC2 instances to receive DNS hostnames.

---

### Tags

Tags are used for:

- Resource Identification
- Cost Allocation
- Automation
- Resource Management

Example

```
Project

Environment

ManagedBy

Name
```

---

# 10. Terraform Workflow

```
terraform fmt

↓

terraform validate

↓

terraform plan

↓

terraform apply
```

Terraform automatically performs:

- Create VPC
- Store State
- Track Resource ID

---

# 11. AWS Console Verification

Navigate to

```
AWS Console

↓

VPC

↓

Your VPCs
```

Verify

✔ CIDR Block

✔ DNS Resolution

✔ DNS Hostnames

✔ Tags

✔ Resource Name

---

# 12. Best Practices

Use RFC1918 Private IP Ranges.

Enable DNS Support.

Enable DNS Hostnames.

Tag every resource.

Use Infrastructure as Code.

Use separate environments for Development and Production.

---

# 13. Common Mistakes

Using overlapping CIDR ranges.

Disabling DNS Support.

Creating resources manually.

Using public subnets for databases.

Not tagging resources.

---

# 14. Interview Questions

What is Amazon VPC?

Why do we need a VPC?

What is CIDR?

Difference between /16 and /24?

Why enable DNS Support?

Why enable DNS Hostnames?

Can two VPCs use the same CIDR?

How do VPCs communicate?

---

# 15. Summary

In this document we learned:

- Amazon VPC
- Network Isolation
- CIDR Planning
- DNS Support
- DNS Hostnames
- Terraform VPC Resource
- AWS Verification
- Best Practices

The VPC is the networking foundation of every AWS infrastructure. All networking components, compute resources, and managed services are deployed within a VPC, making it one of the first and most important resources created in any cloud environment.

---

# Next Document

**08 - Internet Gateway (IGW)**

Topics Covered

- Internet Connectivity
- Public Internet Access
- VPC Attachment
- Terraform Implementation
- AWS Verification
- Best Practices