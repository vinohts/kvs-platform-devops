# 12 - AWS Identity and Access Management (IAM)

> **Project:** KVS Platform DevOps
>
> **Document Version:** 1.0
>
> **Author:** Vinoth Kumar
>
> **Service:** AWS Identity and Access Management (IAM)
>
> **Status:** Completed

---

# Document Objective

The objective of this document is to understand AWS Identity and Access Management (IAM), how AWS controls access to resources, and how IAM Roles, Policies, and Instance Profiles are implemented using Terraform.

After completing this document, you should be able to:

- Understand AWS IAM
- Explain IAM Users, Groups, Roles and Policies
- Understand Trust Relationships
- Understand Instance Profiles
- Deploy IAM resources using Terraform
- Verify IAM resources in AWS
- Follow AWS security best practices

---

# Table of Contents

1. Introduction
2. What is IAM?
3. Why Do We Need IAM?
4. IAM Components
5. IAM Users
6. IAM Groups
7. IAM Roles
8. IAM Policies
9. IAM Instance Profiles
10. Trust Relationships
11. IAM Architecture
12. KVS Platform IAM Design
13. Terraform Implementation
14. Resource Explanation
15. Terraform Workflow
16. AWS Console Verification
17. Best Practices
18. Common Mistakes
19. Interview Questions
20. Summary

---

# 1. Introduction

AWS Identity and Access Management (IAM) is the service responsible for authentication and authorization within AWS.

IAM determines:

- Who can access AWS.
- What resources they can access.
- Which actions they are allowed to perform.

IAM is one of the most important AWS services because every AWS API request is evaluated against IAM permissions.

---

# 2. What is IAM?

IAM is AWS's centralized access control system.

It allows you to securely manage identities and permissions without sharing AWS account credentials.

IAM enables:

- Secure authentication
- Fine-grained authorization
- Least privilege access
- Temporary credentials
- Role-based access control

---

# Real-Life Analogy

Imagine a company office.

```
Employee

↓

ID Card

↓

Security Guard

↓

Office Access

↓

Department Access
```

The employee's identity determines who they are.

The ID card determines what they are allowed to access.

AWS IAM works the same way.

---

# 3. Why Do We Need IAM?

Without IAM:

- Everyone would have full administrator access.
- Credentials would be shared.
- Resources would be insecure.
- Auditing would become impossible.

IAM allows organizations to securely control access to AWS resources.

---

# 4. IAM Components

AWS IAM consists of several components.

```
IAM

│

├── Users

├── Groups

├── Roles

├── Policies

└── Instance Profiles
```

Each component has a specific purpose.

---

# 5. IAM Users

An IAM User represents a person or application that requires long-term AWS credentials.

Examples:

- Administrator
- DevOps Engineer
- Developer
- CI/CD Pipeline

Users can authenticate using:

- Username & Password
- Access Keys
- MFA

Best Practice:

Avoid using IAM Users for AWS services such as EC2.

---

# 6. IAM Groups

Groups are collections of IAM Users.

Instead of assigning permissions to individual users, permissions are assigned to Groups.

Example

```
Developers

↓

Developer1

Developer2

Developer3
```

The entire group receives the same permissions.

---

# 7. IAM Roles

IAM Roles provide temporary AWS credentials.

Unlike Users, Roles do not have:

- Passwords
- Access Keys

Roles are assumed by trusted entities.

Examples:

- EC2
- Lambda
- ECS
- AWS Services
- Cross Account Access

Example

```
EC2 Instance

↓

Assume IAM Role

↓

Receive Temporary Credentials
```

---

# 8. IAM Policies

Policies define permissions.

A Policy answers the question:

"What actions are allowed?"

Example permissions:

- Read S3
- Write CloudWatch Logs
- Read Secrets Manager
- Access Systems Manager

Policies are JSON documents.

Example

```json
{
    "Effect": "Allow",
    "Action": "s3:GetObject",
    "Resource": "*"
}
```

Policies can be:

- AWS Managed Policies
- Customer Managed Policies
- Inline Policies

---

# 9. IAM Instance Profiles

An Instance Profile is a container for an IAM Role.

EC2 instances cannot directly attach an IAM Role.

Instead, AWS uses an Instance Profile.

Relationship

```
IAM Policy

↓

IAM Role

↓

Instance Profile

↓

EC2 Instance
```

Terraform requires the Instance Profile to attach permissions to an EC2 instance.

---

# 10. Trust Relationships

A Trust Relationship defines who is allowed to assume a Role.

Example

```json
{
  "Version":"2012-10-17",
  "Statement":[
    {
      "Effect":"Allow",
      "Principal":{
        "Service":"ec2.amazonaws.com"
      },
      "Action":"sts:AssumeRole"
    }
  ]
}
```

This allows EC2 instances to assume the Role.

Without a Trust Relationship, no service can use the Role.

---

# 11. IAM Architecture

```
               IAM Policy

           (Permissions)

                    │

                    ▼

              IAM Role

            (Identity)

                    │

                    ▼

        IAM Instance Profile

                    │

                    ▼

             EC2 Instance

                    │

                    ▼

          AWS Services (S3, SSM,
         CloudWatch, Secrets Manager)
```

---

# 12. KVS Platform IAM Design

For this project we created:

### IAM Role

```
kvs-platform-dev-ec2-role
```

Purpose

Provides an identity for EC2 instances.

---

### AWS Managed Policy

```
AmazonSSMManagedInstanceCore
```

Purpose

Allows EC2 instances to communicate securely with AWS Systems Manager.

This enables Session Manager access without requiring SSH.

---

### Instance Profile

```
kvs-platform-dev-instance-profile
```

Purpose

Allows the EC2 instance to use the IAM Role.

---

# 13. Terraform Implementation

## IAM Role

```hcl
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

}
```

---

## Policy Attachment

```hcl
resource "aws_iam_role_policy_attachment" "ssm" {

  role = aws_iam_role.ec2.name

  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"

}
```

---

## Instance Profile

```hcl
resource "aws_iam_instance_profile" "ec2" {

  name = "${local.project_name}-${var.environment}-instance-profile"

  role = aws_iam_role.ec2.name

}
```

---

# 14. Resource Explanation

## aws_iam_role

Creates an IAM Role.

Represents the identity of the EC2 instance.

---

## assume_role_policy

Defines who can assume the Role.

In our project:

```
EC2

↓

Can Assume Role
```

---

## aws_iam_role_policy_attachment

Attaches permissions to the IAM Role.

---

## AmazonSSMManagedInstanceCore

AWS Managed Policy that allows:

- Session Manager
- Systems Manager
- Inventory
- Patch Manager

---

## aws_iam_instance_profile

Creates an Instance Profile.

This is later attached to EC2.

Example

```hcl
iam_instance_profile = aws_iam_instance_profile.ec2.name
```

---

# 15. Terraform Workflow

```bash
terraform fmt

terraform validate

terraform plan

terraform apply
```

Terraform automatically:

- Creates IAM Role
- Creates Trust Relationship
- Attaches Policy
- Creates Instance Profile
- Stores resources in Terraform State

---

# 16. AWS Console Verification

Navigate to

```
AWS Console

↓

IAM

↓

Roles
```

Verify

✔ IAM Role exists

✔ Trust Relationship = EC2

✔ AmazonSSMManagedInstanceCore attached

---

Navigate to

```
IAM

↓

Roles

↓

Select Role
```

Verify

- Trust Relationship
- Attached Policies
- Instance Profile ARN

---

# 17. Best Practices

- Follow the Principle of Least Privilege.
- Prefer IAM Roles over IAM Users for AWS services.
- Use AWS Managed Policies where appropriate.
- Avoid embedding AWS Access Keys in applications.
- Use temporary credentials.
- Enable MFA for IAM Users.
- Manage IAM using Terraform.

---

# 18. Common Mistakes

- Using the Root Account for daily work.
- Assigning AdministratorAccess to every user.
- Hardcoding Access Keys in code.
- Creating overly permissive policies.
- Forgetting to attach an Instance Profile to EC2.
- Editing IAM resources manually after Terraform deployment.

---

# 19. Interview Questions

1. What is IAM?
2. What is the difference between an IAM User and an IAM Role?
3. What is an IAM Policy?
4. What is an Instance Profile?
5. Why does EC2 require an Instance Profile?
6. What is a Trust Relationship?
7. What is the Principle of Least Privilege?
8. What is the difference between AWS Managed and Customer Managed Policies?
9. Why should IAM Roles be used instead of Access Keys?
10. How does Terraform attach an IAM Role to an EC2 instance?

---

# 20. Summary

In this document we learned:

- AWS IAM
- IAM Users
- IAM Groups
- IAM Roles
- IAM Policies
- IAM Instance Profiles
- Trust Relationships
- Terraform Implementation
- AWS Verification
- Best Practices
- Common Mistakes

IAM is the foundation of AWS security. It controls authentication and authorization across AWS services. In the KVS Platform, we use an IAM Role with an Instance Profile to provide secure, temporary credentials to EC2 instances without storing AWS access keys, following AWS security best practices.

---
