# KVS Platform - DevOps Repository

## Phase 1 - Enterprise CI/CD Foundation

**Version:** 1.0
**Author:** Vinoth Kumar
**Project:** KVS Platform

---

# Project Overview

The KVS Platform is designed using an enterprise DevOps architecture where the Application source code and DevOps automation are maintained in separate repositories.

The objective is to implement a real-world CI/CD platform capable of:

* Continuous Integration (CI)
* Continuous Deployment (CD)
* Artifact Management
* Infrastructure Automation
* Immutable Deployments
* Multi-Environment Support
* Future Infrastructure as Code (IaC)

---

# Repository Architecture

## KVS Application Repository

```
Repository

kvs-platform-app
```

Purpose

* Application Source Code
* Business Logic
* Developers work here

Structure

```
kvs-platform-app
│
├── lambda/
├── portal/
├── shared/
├── worker/
└── README.md
```

---

## DevOps Repository

```
Repository

kvs-platform-devops
```

Purpose

* Jenkins Pipelines
* Infrastructure Automation
* Build Automation
* Deployment Automation
* Packer
* Ansible
* Terraform (Future)

---

# AWS Infrastructure

## Development Environment

| Component      | Value          |
| -------------- | -------------- |
| Environment    | Development    |
| AWS Region     | ap-northeast-1 |
| Location       | Tokyo          |
| AWS Credential | aws-dev        |
| Branch         | develop        |

---

## Production Environment

| Component      | Value          |
| -------------- | -------------- |
| Environment    | Production     |
| AWS Region     | ap-southeast-2 |
| Location       | Sydney         |
| AWS Credential | aws-prod       |
| Branch         | release        |

---

# Enterprise Branch Strategy

## Application Repository

```
develop
        │
        ▼
Development Environment

release
        │
        ▼
Production Environment
```

---

## DevOps Repository

```
main
```

The DevOps repository maintains pipeline automation and infrastructure code.

---

# CI/CD Architecture

```
                     GitHub

         +--------------------------+
         |                          |
         |                          |
         ▼                          ▼

kvs-platform-devops          kvs-platform-app

        │                           │

        │                           │

        └──────── Checkout ──────────┘

                    │

                    ▼

             CI Pipeline

                    │

                    ▼

              ZIP Artifact

                    │

                    ▼

              Amazon S3

                    │

                    ▼

             CD Pipeline

                    │

                    ▼

             Deploy Application
```

---

# Build Once - Deploy Many

The platform follows the enterprise deployment model.

```
Build Once

↓

Create Artifact

↓

Store in Amazon S3

↓

Deploy Same Artifact

↓

Development

↓

Testing

↓

Production
```

The deployment pipeline never rebuilds the application.

---

# Current Amazon S3

Artifact Bucket

```
kvs-platform-artifacts
```

Current Artifact Structure

```
kvs-platform-artifacts

develop/

    develop-12/

        kvs-platform-develop-12.zip

    develop-13/

        kvs-platform-develop-13.zip
```

---

# Devops Repository Structure

```
kvs-platform-devops
│
├── ansible/
├── jobs/
├── packer/
├── scripts/
│
└── jenkins/
    │
    ├── common/
    │   ├── constants.groovy
    │   ├── logger.groovy
    │   └── utils.groovy
    │
    ├── config/
    │   ├── credentials.groovy
    │   ├── environments.groovy
    │   └── globals.groovy
    │
    ├── library/
    │
    ├── pipeline/
    │   ├── app-ci.groovy
    │   ├── app-deploy.groovy
    │   ├── infra.groovy
    │   └── rollback.groovy
    │
    └── stages/
        ├── artifact.groovy
        ├── branch.groovy
        ├── build.groovy
        ├── checkout.groovy
        ├── cleanup.groovy
        ├── deploy.groovy
        ├── initialize.groovy
        └── package.groovy
```

---

# Folder Explanation

## ansible/

Contains Ansible playbooks used for server configuration and application deployment.

**Status:** Phase 2

---

## jobs/

Stores Jenkins Job DSL or Jenkins job configuration files.

**Status:** Future

---

## packer/

Contains Packer templates for Golden AMI creation.

**Status:** Phase 2

---

## scripts/

Reusable PowerShell and Shell scripts used by Jenkins pipelines.

**Status:** Future

---

# Jenkins Common

## constants.groovy

Stores constant values shared across the pipelines.

Examples

* Project Name
* Default Bucket
* Default Regions

---

## logger.groovy

Provides standardized logging functions.

---

## utils.groovy

Contains reusable helper functions.

Examples

* File Utilities
* Common Functions
* String Utilities

---

# Jenkins Config

## credentials.groovy

Stores Jenkins Credential IDs.

Examples

* GitHub PAT
* AWS Credentials

---

## environments.groovy

Maps Git branches to deployment environments.

Example

```
develop

↓

Development

↓

Tokyo
```

```
release

↓

Production

↓

Sydney
```

Returns

* Environment
* AWS Region
* AWS Credential

---

## globals.groovy

Stores reusable global configuration.

Examples

* Organization Name
* Project Name
* Default Settings

---

# Jenkins Library

Reserved for Jenkins Shared Library implementation.

**Status:** Future

---

# Pipeline Files

## app-ci.groovy

Main Continuous Integration pipeline.

Current Stages

* Branch Validation
* Checkout
* Initialize
* Build
* Package
* Publish
* Summary

Purpose

Builds the application and uploads the artifact to Amazon S3.

---

## app-deploy.groovy

Main Continuous Deployment pipeline.

Current Stages

* Branch Validation
* Initialize
* Deploy
* Summary

Purpose

Downloads the artifact from Amazon S3 and prepares it for deployment.

---

## infra.groovy

Infrastructure provisioning pipeline.

Future Responsibilities

* Launch EC2
* Configure Security Groups
* Create Infrastructure

---

## rollback.groovy

Rollback pipeline.

Future Responsibilities

Deploy previous artifact versions.

---

# Stage Files

## branch.groovy

Performs branch validation.

Returns

* Environment
* Region
* AWS Credentials

---

## checkout.groovy

Checks out the application repository.

Repository

```
kvs-platform-app
```

Used only during CI.

---

## initialize.groovy

Initializes pipeline variables.

Generates

* Build Version
* Pipeline Name
* Workspace
* Job Name
* Environment

---

## build.groovy

Application build stage.

Current

* Repository Validation
* Build Verification

Future

* Maven
* Gradle
* npm
* .NET

---

## package.groovy

Packages the application.

Current Output

```
kvs-platform-develop-13.zip
```

---

## artifact.groovy

Uploads artifacts to Amazon S3.

Responsibilities

* Upload
* Verification
* Local Cleanup
* Return S3 Details

---

## deploy.groovy

Downloads the deployment artifact.

Responsibilities

* Workspace Cleanup
* Download ZIP
* Extract ZIP
* Prepare Deployment Folder

---

## cleanup.groovy

Reserved for future workspace cleanup.

---

# CI Pipeline Flow

```
GitHub (Application)

        │

        ▼

Branch Validation

        │

        ▼

Checkout Repository

        │

        ▼

Initialize

        │

        ▼

Build

        │

        ▼

Package ZIP

        │

        ▼

Upload to Amazon S3

        │

        ▼

Delete Local Artifact

        │

        ▼

Pipeline Summary
```

---

# CD Pipeline Flow

```
Amazon S3

        │

        ▼

Branch Validation

        │

        ▼

Initialize

        │

        ▼

Download Artifact

        │

        ▼

Extract ZIP

        │

        ▼

Prepare Deployment Folder

        │

        ▼

Pipeline Summary
```

---

# Phase 1 Achievements

Completed

* Enterprise Repository Separation
* GitHub Integration
* Jenkins Pipeline from SCM
* Environment Configuration
* Branch Validation
* Application Checkout
* Pipeline Initialization
* Build Stage
* Package Stage
* Amazon S3 Artifact Storage
* Artifact Verification
* Local Artifact Cleanup
* Deployment Pipeline
* Artifact Download
* Artifact Extraction
* Deployment Workspace Preparation
* Enterprise CI/CD Architecture

---

# Current Workflow

```
Developer

      │

      ▼

GitHub

(kvs-platform-app)

      │

      ▼

CI Pipeline

      │

      ▼

ZIP Artifact

      │

      ▼

Amazon S3

      │

      ▼

CD Pipeline

      │

      ▼

Download

      │

      ▼

Extract

      │

      ▼

Deployment Workspace
```

---

# Phase 2 Roadmap

The next phase will introduce complete infrastructure automation.

Planned Features

* Amazon EC2 Provisioning
* Infrastructure Pipeline
* Packer Golden AMI
* Ansible Configuration
* Application Deployment
* Health Checks
* Rollback Automation
* Production Deployment
* Terraform Infrastructure as Code

---

# Enterprise DevOps Principles Implemented

* Repository Separation
* Build Once, Deploy Many
* Immutable Artifacts
* Environment-Based Deployment
* Modular Jenkins Pipelines
* Reusable Groovy Stages
* Centralized Configuration
* Artifact Versioning
* S3-Based Artifact Repository
* CI/CD Separation

This document serves as the Phase 1 design and implementation guide for the KVS Platform DevOps repository and provides a solid baseline before starting the infrastructure automation phase.
