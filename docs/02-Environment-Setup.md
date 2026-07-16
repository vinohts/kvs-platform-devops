# KVS Platform

# Environment Setup

Version : 1.0

---

# Purpose

This document describes the development environment used for the KVS Platform project before building the CI/CD pipeline.

---

# Development Machine

| Component | Value |
|-----------|-------|
| Operating System | Windows 11 |
| Workspace Root | D:\KVS-July |
| Jenkins | Installed |
| Git | Installed |
| AWS CLI | Installed |
| Packer | Installed |
| Visual Studio Code | Installed |

---

# Repository Structure

| Repository | Local Path |
|------------|------------|
| kvs-platform-devops | D:\KVS-July\kvs-platform-devops |
| kvs-platform-app | D:\KVS-July\kvs-platform-app |

---

# Branch Strategy

Application Repository

- develop
- release

DevOps Repository

- main

---

# AWS Environments

| Environment | Region |
|------------|--------|
| Development | ap-northeast-1 (Tokyo) |
| Production | ap-southeast-2 (Sydney) |

---

# Artifact Storage

| Component | Value |
|-----------|-------|
| Bucket Name | kvs-platform-artifacts |
| Region | ap-northeast-1 |
| Versioning | Disabled |
| Encryption | Default AWS |

---

# AWS Account

| Item | Value |
|------|-------|
| AWS Account ID | 350025135544 |

---