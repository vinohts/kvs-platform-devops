# Jenkins Configuration

---

# Jenkins Environment Variables

| Name | Value |
|------|-------|
| WORKSPACE_ROOT | D:\KVS-July |
| APP_REPO | kvs-platform-app |
| DEVOPS_REPO | kvs-platform-devops |
| DEV_REGION | ap-northeast-1 |
| PROD_REGION | ap-southeast-2 |

---

# Jenkins Credentials

## GitHub

| ID | Description |
|----|-------------|
| github-pat | GitHub Personal Access Token |

---

## AWS Credentials

| Credential ID | IAM User |
|---------------|----------|
| aws-dev | kvs-dev-user |
| aws-prod | kvs-prod-user |
| aws-admin | kvs-admin |

---

# Jenkins Jobs

| Job |
|-----|
| app-ci |
| app-deploy |

---