# 06 - Terraform Fundamentals

> **Project:** KVS Platform DevOps
>
> **Document Version:** 1.0
>
> **Author:** Vinoth Kumar
>
> **Technology:** Terraform
>
> **Status:** In Progress

---

# Document Objective

The purpose of this document is to understand the core concepts of Terraform before deploying AWS infrastructure.

After completing this document, you should be able to:

- Understand Infrastructure as Code (IaC)
- Understand how Terraform works internally
- Write basic Terraform configurations
- Understand Providers, Variables, Locals and Resources
- Understand Terraform State
- Understand Drift Detection
- Understand the Terraform deployment workflow
- Follow Terraform best practices

---

# Table of Contents

1. Introduction
2. Infrastructure as Code (IaC)
3. What is Terraform?
4. Why Terraform?
5. Terraform Architecture
6. Terraform Workflow
7. Terraform Project Structure
8. Terraform Configuration Files
9. Providers
10. Variables
11. Locals
12. Resources
13. Resource References
14. Terraform State
15. Drift Detection
16. Terraform Commands
17. Best Practices
18. Common Mistakes
19. Interview Questions
20. Summary

---

# 1. Introduction

Terraform is an Infrastructure as Code (IaC) tool developed by HashiCorp.

Instead of creating infrastructure manually using the AWS Console, Terraform allows infrastructure to be written as code.

The same code can be executed repeatedly to build identical environments.

Example

Instead of manually creating

- VPC
- Subnets
- Route Tables
- Security Groups
- EC2

Terraform creates everything automatically.

---

# 2. Infrastructure as Code (IaC)

## Traditional Infrastructure

```
Engineer

↓

AWS Console

↓

Click

↓

Create Resource

↓

Save
```

Problems

- Human Error
- No Version Control
- Difficult to Reproduce
- Time Consuming

---

## Infrastructure as Code

```
Terraform Code

↓

terraform apply

↓

AWS Infrastructure
```

Advantages

- Repeatable
- Automated
- Version Controlled
- Easy Recovery
- Easy Collaboration

---

# 3. What is Terraform?

Terraform is a declarative Infrastructure as Code tool.

Instead of describing HOW to build infrastructure, we describe WHAT infrastructure should exist.

Terraform automatically determines:

- Resource Creation Order
- Dependencies
- Updates
- Deletions

---

# 4. Why Terraform?

Terraform provides several enterprise advantages.

| Feature | Benefit |
|----------|----------|
| Infrastructure as Code | Infrastructure becomes version controlled |
| Multi Cloud | AWS, Azure, GCP, OCI |
| Dependency Management | Automatically creates resources in correct order |
| State Management | Tracks deployed resources |
| Automation | Easy CI/CD integration |
| Reusability | Modules reduce duplication |

---

# 5. Terraform Architecture

```
                Terraform Code

                        │

                        ▼

               Terraform CLI

                        │

                        ▼

             AWS Provider Plugin

                        │

                        ▼

                  AWS API

                        │

                        ▼

                AWS Infrastructure
```

### Components

**Terraform CLI**

Executes Terraform commands.

**Provider**

Allows Terraform to communicate with AWS.

**Configuration**

Terraform files written in HCL.

**State File**

Stores deployed infrastructure information.

**AWS**

Creates and manages cloud resources.

---

# 6. Terraform Workflow

```
Write Code

↓

terraform fmt

↓

terraform validate

↓

terraform plan

↓

terraform apply

↓

AWS Infrastructure
```

Each command has a specific responsibility.

| Command | Purpose |
|----------|----------|
| terraform init | Download provider plugins |
| terraform fmt | Format Terraform code |
| terraform validate | Validate configuration |
| terraform plan | Preview infrastructure changes |
| terraform apply | Apply changes |
| terraform destroy | Delete infrastructure |

---

# 7. Terraform Project Structure

```text
terraform/

├── backend/
├── environments/
│   ├── dev/
│   └── prod/
├── modules/
└── variables/
```

### Purpose

**backend**

Stores backend configuration.

**environments**

Environment-specific Terraform code.

**modules**

Reusable infrastructure components.

**variables**

Shared variable definitions.

---

# 8. Terraform Configuration Files

| File | Purpose |
|------|----------|
| versions.tf | Terraform and Provider versions |
| providers.tf | AWS Provider configuration |
| variables.tf | Variable declarations |
| terraform.tfvars | Variable values |
| main.tf | Shared locals |
| vpc.tf | VPC |
| subnet.tf | Subnets |
| igw.tf | Internet Gateway |
| rt.tf | Route Tables |
| sg.tf | Security Groups |

---

# 9. Providers

Terraform communicates with cloud providers using Provider Plugins.

Example

```hcl
terraform {

  required_providers {

    aws = {

      source = "hashicorp/aws"

      version = "=6.54.0"

    }

  }

}
```

Provider configuration

```hcl
provider "aws" {

    region = var.aws_region

}
```

---

# 10. Variables

Variables allow infrastructure to become reusable.

Example

```hcl
variable "aws_region" {

  type = string

}
```

Benefits

- No hardcoding
- Environment specific
- Easy maintenance

---

# 11. Locals

Locals store reusable values.

Example

```hcl
locals {

    project_name = "kvs-platform"

}
```

Benefits

- Reduce duplication
- Improve readability
- Standardize naming

---

# 12. Resources

Every AWS service is represented as a Terraform Resource.

Example

```hcl
resource "aws_vpc" "main" {

}
```

Structure

```
resource

↓

Resource Type

↓

Logical Name

↓

Configuration
```

---

# 13. Resource References

Terraform resources communicate using references.

Example

```hcl
vpc_id = aws_vpc.main.id
```

Terraform automatically understands

```
VPC

↓

Subnet

↓

Route Table

↓

EC2
```

No manual dependency configuration is required.

---

# 14. Terraform State

Terraform stores deployed infrastructure information inside

```
terraform.tfstate
```

Purpose

- Track deployed resources
- Store Resource IDs
- Detect Infrastructure Drift
- Compare Desired vs Actual State

Architecture

```
Terraform Code

↓

Terraform State

↓

AWS Infrastructure
```

---

# 15. Drift Detection

Drift occurs when infrastructure changes outside Terraform.

Example

Delete a Security Group manually from AWS Console.

Terraform Plan

```
Terraform Code

↓

Security Group should exist

↓

AWS

↓

Security Group missing

↓

Terraform recreates resource
```

This process is called Drift Detection.

---

# 16. Best Practices

Always use Git.

Always review terraform plan.

Use Variables.

Use Locals.

Pin Provider Versions.

Never edit terraform.tfstate manually.

Avoid manual AWS Console changes.

Tag every AWS resource.

---

# 17. Common Mistakes

Skipping terraform plan.

Hardcoding values.

Deleting AWS resources manually.

Editing State Files.

Ignoring Provider Versions.

---

# 18. Interview Questions

- What is Terraform?
- What is Infrastructure as Code?
- What is Terraform State?
- What is Drift?
- Difference between Plan and Apply?
- Why use Variables?
- Why use Locals?
- What are Providers?
- How does Terraform determine resource creation order?

---

# 19. Summary

In this document we learned

- Infrastructure as Code
- Terraform Architecture
- Providers
- Variables
- Locals
- Resources
- Dependencies
- State File
- Drift Detection
- Terraform Workflow
- Best Practices

Terraform is a declarative Infrastructure as Code tool that enables repeatable, version-controlled and automated cloud infrastructure deployments.

---