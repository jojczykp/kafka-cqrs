provider "aws" {
  region = "eu-west-1"
}

module deployment {
  source      = "../deployment-module"
  environment = "green"
}

output "availability_zone" {
  value = module.deployment.availability_zone
}

output "public_ip" {
  value = module.deployment.public_ip
}

output "application_url" {
  value = module.deployment.application_url
}

output "cloud_init_log_command" {
  value = module.deployment.cloud_init_log_command
}
