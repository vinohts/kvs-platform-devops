# 11 - Amazon Security Groups

> **Project:** KVS Platform DevOps
>
> **Document Version:** 1.0
>
> **Author:** Vinoth Kumar
>
> **Service:** Amazon EC2 - Security Groups
>
> **Status:** Completed

---

# Document Objective

The objective of this document is to understand Amazon Security Groups, how they protect AWS resources, and how to configure them using Terraform.

After completing this document, you should be able to:

- Understand what a Security Group is
- Explain Stateful Firewall behavior
- Configure Inbound and Outbound rules
- Understand Security Group referencing
- Deploy Security Groups using Terraform
- Verify configurations in AWS
- Follow AWS security best practices

---

# Table of Contents

1. Introduction
2. What is a Security Group?
3. Why Do We Need Security Groups?
4. How Security Groups Work
5. Stateful Firewall
6. Inbound and Outbound Rules
7. Security Group Architecture
8. KVS Platform Security Design
9. Terraform Implementation
10. Resource Explanation
11. Terraform Workflow
12. AWS Console Verification
13. Best Practices
14. Common Mistakes
15. Interview Questions
16. Summary

---

# 1. Introduction

Amazon Security Groups act as virtual firewalls that control network traffic to AWS resources.

Every EC2 instance should be associated with one or more Security Groups.

Security Groups determine:

- Who can connect
- Which ports are allowed
- Which protocols are allowed
- Which destination traffic is permitted

---

# 2. What is a Security Group?

A Security Group is a virtual firewall that controls traffic entering and leaving an AWS resource.

Unlike traditional firewalls, Security Groups are attached directly to resources such as:

- EC2 Instances
- Application Load Balancers
- RDS Databases
- ECS Tasks
- Elastic Network Interfaces

Security Groups protect the resource rather than the subnet.

---

# Real-Life Analogy

Imagine an office building.

```
Office Building

↓

Security Guard

↓

Check Visitor

↓

Allow or Deny Entry
```

Only authorized visitors are allowed to enter.

Similarly,

Security Groups inspect every network request and decide whether to allow or deny it.

---

# 3. Why Do We Need Security Groups?

Without Security Groups:

- Anyone could access servers.
- Applications become vulnerable.
- Unauthorized traffic reaches resources.
- Attack surface increases.

Security Groups provide:

- Access Control
- Network Security
- Resource Isolation
- Least Privilege Access

---

# 4. How Security Groups Work

When traffic reaches an EC2 instance:

```
Internet

↓

Security Group

↓

EC2 Instance
```

AWS checks every inbound rule.

If a matching rule exists:

✅ Traffic is allowed.

Otherwise:

❌ Traffic is denied.

---

# 5. Stateful Firewall

Amazon Security Groups are **Stateful Firewalls**.

This means:

If inbound traffic is allowed,

The return traffic is automatically allowed.

Example

```
Laptop

↓

SSH Request

↓

EC2

↓

SSH Response

↓

Laptop
```

No outbound rule is required for the response.

AWS automatically allows return traffic.

---

# Stateless vs Stateful

| Feature | Security Group | Network ACL |
|----------|----------------|-------------|
| Stateful | Yes | No |
| Return Traffic | Automatic | Must be Allowed |
| Attached To | Resource | Subnet |

---

# 6. Inbound and Outbound Rules

## Inbound Rules

Control traffic entering the resource.

Examples

| Protocol | Port | Purpose |
|----------|------|----------|
| SSH | 22 | Bastion Access |
| HTTP | 80 | Web Traffic |
| HTTPS | 443 | Secure Web Traffic |
| Custom | 8080 | Application |

---

## Outbound Rules

Control traffic leaving the resource.

Common default:

```
All Traffic

↓

0.0.0.0/0
```

This allows resources to initiate outbound connections.

---

# 7. Security Group Architecture

```
                     Internet
                          │
                          │
               ALB Security Group
                    Ports 80,443
                          │
                          ▼
                Application Load Balancer
                          │
                          ▼
              Application Security Group
                     Port 8080
                          │
                          ▼
                 Application Servers


Administrator

↓

Bastion Security Group

↓

Port 22

↓

Bastion Host
```

Each layer communicates only with the required resources.

---

# 8. KVS Platform Security Design

For this project we created:

### ALB Security Group

Purpose

- Accept HTTP
- Accept HTTPS
- Internet Facing

---

### Bastion Security Group

Purpose

- Allow SSH
- Administrator Access

---

### Application Security Group

Purpose

- Accept application traffic
- Accessible only from the ALB
- Not directly accessible from the Internet

---

# 9. Terraform Implementation

Example

```hcl
resource "aws_security_group" "bastion" {

  name = "${local.project_name}-${var.environment}-bastion-sg"

  description = "Security Group for Bastion Host"

  vpc_id = aws_vpc.main.id

  ingress {

    from_port = 22

    to_port = 22

    protocol = "tcp"

    cidr_blocks = ["0.0.0.0/0"]

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

      Name = "${local.project_name}-${var.environment}-bastion-sg"

    }

  )

}
```

---

# 10. Resource Explanation

## Resource Type

```
aws_security_group
```

Creates a Security Group.

---

## VPC ID

```hcl
vpc_id = aws_vpc.main.id
```

Associates the Security Group with the VPC.

---

## Ingress

Defines inbound rules.

Example

```hcl
from_port = 22

to_port = 22

protocol = "tcp"
```

Allows SSH.

---

## Egress

Defines outbound rules.

Example

```hcl
protocol = "-1"
```

Allows all outbound traffic.

---

## Tags

Tags help identify Security Groups.

Example

```
Project

Environment

ManagedBy

Name
```

---

# 11. Terraform Workflow

```bash
terraform fmt

terraform validate

terraform plan

terraform apply
```

Terraform automatically:

- Creates Security Groups
- Creates Ingress Rules
- Creates Egress Rules
- Updates Terraform State

---

# 12. AWS Console Verification

Navigate to:

```
AWS Console

↓

EC2

↓

Security Groups
```

Verify:

✔ ALB Security Group

✔ Bastion Security Group

✔ Application Security Group

For each Security Group verify:

- Name
- VPC
- Inbound Rules
- Outbound Rules
- Tags

---

# 13. Best Practices

- Follow the Principle of Least Privilege.
- Open only required ports.
- Use Security Group references instead of CIDR blocks whenever possible.
- Avoid opening SSH (22) to the entire Internet in production.
- Use separate Security Groups for each application tier.
- Tag every Security Group.
- Manage Security Groups using Terraform.

---

# 14. Common Mistakes

- Allowing 0.0.0.0/0 on every port.
- Opening databases to the Internet.
- Using one Security Group for all resources.
- Forgetting to remove unused rules.
- Editing Security Groups manually after Terraform deployment.

---

# 15. Interview Questions

1. What is a Security Group?
2. Is a Security Group Stateful or Stateless?
3. What is the difference between Ingress and Egress?
4. Can one EC2 instance have multiple Security Groups?
5. Can multiple EC2 instances share one Security Group?
6. What is the difference between a Security Group and a Network ACL?
7. Why should databases not be publicly accessible?
8. Why is the Principle of Least Privilege important?

---

# 16. Summary

In this document we learned:

- Amazon Security Groups
- Stateful Firewall
- Inbound Rules
- Outbound Rules
- Security Group Architecture
- Terraform Implementation
- AWS Verification
- Best Practices
- Common Mistakes

Security Groups are the first layer of network security in AWS. They act as virtual firewalls, protecting individual resources by controlling inbound and outbound traffic. A well-designed Security Group strategy significantly improves the security posture of any cloud environment.

---
