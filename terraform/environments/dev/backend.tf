terraform {
  backend "s3" {
    bucket = "kvs-platform-dev-tfstate"
    key    = "dev/terraform.tfstate"
    region = "ap-northeast-1"
  }
}