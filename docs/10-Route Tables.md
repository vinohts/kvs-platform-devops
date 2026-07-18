# 10 - Amazon Route Tables

> **Project:** KVS Platform DevOps
>
> **Document Version:** 1.0
>
> **Author:** Vinoth Kumar
>
> **Service:** Amazon VPC - Route Tables
>
> **Status:** Completed

---

# Document Objective

The objective of this document is to understand Amazon Route Tables, how routing works inside a VPC, and how Route Tables determine where network traffic is forwarded.

After completing this document, you should be able to:

- Understand what a Route Table is
- Understand how routes work
- Explain Public and Private Route Tables
- Associate Route Tables with Subnets
- Deploy Route Tables using Terraform
- Verify routing configuration in AWS
- Follow AWS networking best practices

---

# Table of Contents

1. Introduction
2. What is a Route Table?
3. Why Do We Need Route Tables?
4. How Routing Works
5. Public vs Private Route Tables
6. Route Table Architecture
7. Terraform Implementation
8. Resource Explanation
9. Route Table Associations
10. Terraform Workflow
11. AWS Console Verification
12. Best Practices
13. Common Mistakes
14. Interview Questions
15. Summary

---

# 1. Introduction

A Route Table contains a set of rules called **Routes**.

These routes determine where network traffic should be forwarded.

Every subnet inside a VPC must be associated with a Route Table.

Without a Route Table, AWS resources cannot determine where packets should be sent.

---

# 2. What is a Route Table?

A Route Table is similar to a GPS navigation system.

When traffic leaves an EC2 instance, AWS checks the Route Table.

The Route Table decides where the packet should go.

Possible destinations include:

- Another subnet
- Internet Gateway
- NAT Gateway
- VPC Peering
- VPN
- Transit Gateway

---

# Real-Life Analogy

Imagine a city road network.

```
House

      │

      ▼

Road

      │

      ▼

Traffic Signal

      │

      ▼

Destination
```

The traffic signal decides which direction vehicles should travel.

Similarly,

A Route Table decides where network traffic should be forwarded.

---

# 3. Why Do We Need Route Tables?

Without Route Tables:

- Resources cannot reach other networks.
- Internet access is impossible.
- Internal communication fails.
- AWS has no path to forward packets.

Route Tables make communication possible by defining network paths.

---

# 4. How Routing Works

When an EC2 instance sends traffic:

```
EC2 Instance

        │

        ▼

Subnet

        │

        ▼

Route Table

        │

        ▼

Destination
```

AWS examines the destination IP address.

If a matching route exists, traffic is forwarded.

Otherwise, the traffic is dropped.

---

# 5. Public vs Private Route Tables

## Public Route Table

Contains a default route pointing to the Internet Gateway.

Example

```
Destination

0.0.0.0/0

↓

Internet Gateway
```

Used by:

- Bastion Host
- Application Load Balancer
- Public EC2 Instances

---

## Private Route Table

Contains only local routes.

No direct route to the Internet.

Used by:

- Application Servers
- Databases
- Internal Services

---

# Public vs Private

| Feature | Public Route Table | Private Route Table |
|----------|-------------------|---------------------|
| Internet Route | Yes | No |
| Internet Gateway | Yes | No |
| Used By | Public Subnets | Private Subnets |

---

# 6. Route Table Architecture

```
                         Internet
                             │
                             │
                    Internet Gateway
                             │
                             │
                  Public Route Table
                    0.0.0.0/0 → IGW
                             │
                  ┌──────────┴──────────┐
                  │                     │
         Public Subnet A       Public Subnet C
                  │                     │
             Bastion Host             ALB


                  Private Route Table
                   Local Routes Only
                             │
                  ┌──────────┴──────────┐
                  │                     │
        Private Subnet A      Private Subnet C
                  │                     │
            Application          Application
               Server               Server
```

---

# 7. Terraform Implementation

## Public Route Table

```hcl
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

    }

  )

}
```

---

## Private Route Table

```hcl
resource "aws_route_table" "private" {

  vpc_id = aws_vpc.main.id

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-private-rt"

    }

  )

}
```

---

# 8. Resource Explanation

## Resource Type

```
aws_route_table
```

Creates a Route Table inside the VPC.

---

## VPC ID

```hcl
vpc_id = aws_vpc.main.id
```

Associates the Route Table with the VPC.

---

## Route Block

```hcl
route {

    cidr_block = "0.0.0.0/0"

    gateway_id = aws_internet_gateway.main.id

}
```

Defines the routing rule.

Meaning:

All traffic destined outside the VPC should use the Internet Gateway.

---

## Tags

Tags identify the Route Table.

Example

```
Project

Environment

ManagedBy

Name
```

---

# 9. Route Table Associations

Creating a Route Table is not enough.

It must be associated with one or more subnets.

Example

```hcl
resource "aws_route_table_association" "public_a" {

  subnet_id = aws_subnet.public_a.id

  route_table_id = aws_route_table.public.id

}
```

Terraform automatically links the subnet with the Route Table.

---

# 10. Terraform Workflow

```bash
terraform fmt

terraform validate

terraform plan

terraform apply
```

Terraform automatically:

- Creates Route Tables
- Creates Routes
- Associates Route Tables with Subnets
- Stores resource IDs in the Terraform State

---

# 11. AWS Console Verification

Navigate to

```
AWS Console

↓

VPC

↓

Route Tables
```

Verify:

✔ Public Route Table exists

✔ Private Route Table exists

✔ Public Route Table has:

```
0.0.0.0/0

↓

Internet Gateway
```

✔ Correct subnet associations

✔ Correct tags

---

# 12. Best Practices

- Use separate Route Tables for Public and Private Subnets.
- Associate every subnet with the correct Route Table.
- Use descriptive names.
- Keep databases in Private Route Tables.
- Manage Route Tables using Terraform.
- Review routes before deployment.

---

# 13. Common Mistakes

- Forgetting to associate the Route Table with a subnet.
- Creating an Internet Gateway route but forgetting the subnet association.
- Using one Route Table for every subnet.
- Sending database traffic through the Internet Gateway.
- Editing Route Tables manually after Terraform deployment.

---

# 14. Interview Questions

1. What is a Route Table?
2. Why do we need Route Tables?
3. What is a Route?
4. What is the purpose of 0.0.0.0/0?
5. What is the difference between Public and Private Route Tables?
6. Can multiple subnets share the same Route Table?
7. Can one subnet have multiple Route Tables?
8. What happens if no matching route exists?

---

# 15. Summary

In this document we learned:

- Amazon Route Tables
- Routes
- Public Route Tables
- Private Route Tables
- Route Table Associations
- Terraform Implementation
- AWS Verification
- Best Practices
- Common Mistakes

A Route Table acts as the routing engine of a VPC. It determines where network traffic should be forwarded, allowing resources to communicate within the VPC, access the Internet when required, and maintain secure network segmentation.

---
