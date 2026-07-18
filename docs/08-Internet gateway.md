# 08 - Internet Gateway (IGW)

> **Project:** KVS Platform DevOps
>
> **Document Version:** 1.0
>
> **Author:** Vinoth Kumar
>
> **Service:** Amazon Internet Gateway (IGW)
>
> **Status:** Completed

---

# Document Objective

The objective of this document is to understand the purpose of an Internet Gateway (IGW), how it enables internet connectivity for resources inside a Virtual Private Cloud (VPC), and how to provision it using Terraform.

After completing this document, you should be able to:

- Understand what an Internet Gateway is
- Explain why an Internet Gateway is required
- Understand how an Internet Gateway is attached to a VPC
- Deploy an Internet Gateway using Terraform
- Verify the deployment in AWS
- Follow AWS networking best practices

---

# Table of Contents

1. Introduction
2. What is an Internet Gateway?
3. Why Do We Need an Internet Gateway?
4. How Internet Connectivity Works
5. Internet Gateway Architecture
6. Terraform Implementation
7. Resource Explanation
8. Terraform Workflow
9. AWS Console Verification
10. Best Practices
11. Common Mistakes
12. Interview Questions
13. Summary

---

# 1. Introduction

An Internet Gateway (IGW) is a highly available and horizontally scalable AWS-managed component that enables communication between resources inside a VPC and the public Internet.

Without an Internet Gateway, resources inside a VPC cannot send or receive internet traffic, even if they have a public IP address.

---

# 2. What is an Internet Gateway?

An Internet Gateway is a gateway that connects a VPC to the Internet.

It acts as the entry and exit point for internet traffic.

Think of it as the **main gate** of a building.

Resources inside the building cannot communicate with the outside world unless they use the main gate.

---

# Real-Life Analogy

Imagine a gated apartment complex.

```
Internet

        │

        ▼

==========================
 Main Entrance Gate (IGW)
==========================

        │

        ▼

Apartment Complex (VPC)

        │

        ├── Building A
        ├── Building B
        └── Building C
```

Residents can leave and visitors can enter only through the main entrance.

Similarly,

Resources inside a VPC communicate with the Internet through the Internet Gateway.

---

# 3. Why Do We Need an Internet Gateway?

Without an Internet Gateway:

- EC2 instances cannot access the Internet.
- Users cannot access public web servers.
- Software updates cannot be downloaded.
- External users cannot access hosted applications.

With an Internet Gateway:

- Public web servers become accessible.
- EC2 instances can download software updates.
- Users can connect to public-facing applications.
- Internet communication becomes possible.

---

# 4. How Internet Connectivity Works

Internet connectivity requires three components working together.

```
Public EC2

        │

        ▼

Route Table

        │

        ▼

Internet Gateway

        │

        ▼

Internet
```

Important:

Having only an Internet Gateway is **not enough**.

A public subnet also requires:

- A Route Table
- A Route to the Internet Gateway
- A Public IP Address (or Elastic IP)

Only then can an EC2 instance communicate with the Internet.

---

# 5. Internet Gateway Architecture

```
                    Internet
                        │
                        │
                +----------------+
                | Internet       |
                | Gateway (IGW)  |
                +----------------+
                        │
                        │
        +--------------------------------+
        |          Amazon VPC            |
        |                                |
        |  Public Subnet                 |
        |   ┌──────────────┐             |
        |   │ EC2 Instance │             |
        |   └──────────────┘             |
        |                                |
        |  Private Subnet                |
        |   ┌──────────────┐             |
        |   │ EC2 Instance │             |
        |   └──────────────┘             |
        +--------------------------------+
```

Only resources in the **public subnet** can access the Internet when routing is configured correctly.

Private subnet resources do not communicate directly with the Internet.

---

# 6. Terraform Implementation

Terraform Resource

```hcl
resource "aws_internet_gateway" "main" {

  vpc_id = aws_vpc.main.id

  tags = merge(

    local.common_tags,

    {

      Name = "${local.project_name}-${var.environment}-igw"

    }

  )

}
```

---

# 7. Resource Explanation

### Resource Type

```
aws_internet_gateway
```

Creates an Internet Gateway.

---

### Logical Name

```
main
```

Terraform's internal reference.

Example

```hcl
aws_internet_gateway.main.id
```

---

### VPC Attachment

```hcl
vpc_id = aws_vpc.main.id
```

This attaches the Internet Gateway to the VPC.

Terraform automatically understands the dependency because the VPC ID is referenced.

The VPC must exist before the Internet Gateway can be attached.

---

### Tags

Tags help identify and organize resources.

Example

```
Project

Environment

ManagedBy

Name
```

---

# 8. Terraform Workflow

Deploy the Internet Gateway using the standard Terraform workflow.

```bash
terraform fmt

terraform validate

terraform plan

terraform apply
```

Terraform automatically performs:

- Create Internet Gateway
- Attach Internet Gateway to the VPC
- Update Terraform State

---

# 9. AWS Console Verification

Navigate to:

```
AWS Console

↓

VPC

↓

Internet Gateways
```

Verify:

✔ Internet Gateway exists

✔ State = Attached

✔ Correct VPC ID

✔ Resource Name

✔ Correct Tags

You can also open the VPC and verify that the Internet Gateway is attached.

---

# 10. Best Practices

- Use only one Internet Gateway per VPC.
- Keep Internet Gateways attached only to the required VPC.
- Place public-facing resources in public subnets.
- Keep databases and sensitive workloads in private subnets.
- Tag all networking resources consistently.
- Manage Internet Gateways using Infrastructure as Code.

---

# 11. Common Mistakes

- Creating an Internet Gateway but forgetting to attach it to the VPC.
- Assuming an Internet Gateway alone provides Internet access.
- Forgetting to add a default route (0.0.0.0/0) in the Route Table.
- Launching EC2 instances without public IP addresses.
- Placing private resources in public subnets unnecessarily.

---

# 12. Interview Questions

1. What is an Internet Gateway?
2. Why is an Internet Gateway required?
3. Can a VPC have multiple Internet Gateways?
4. Does an Internet Gateway provide Internet access by itself?
5. What other components are required for Internet connectivity?
6. Can private subnets directly use an Internet Gateway?
7. What is the relationship between a Route Table and an Internet Gateway?
8. How does Terraform know which VPC to attach the Internet Gateway to?

---

# 13. Summary

In this document we learned:

- What an Internet Gateway is
- Why an Internet Gateway is required
- How it connects a VPC to the Internet
- Internet connectivity workflow
- Terraform implementation
- Resource explanation
- AWS verification steps
- Best practices
- Common mistakes

An Internet Gateway is the entry and exit point between an Amazon VPC and the public Internet. However, it works together with Route Tables, Public Subnets, and Public IP addresses to provide complete Internet connectivity.

---
