provider "aws" {
  region = "eu-west-1"
}

module deployment {
  source      = "../deployment-module"
  environment = "blue"
  branch      = "test"
}

output "availability_zone" {
#  description = "Availability Zone"
  value       = module.deployment.availability_zone
}

output "public_ip" {
#  description = "Instance Public IP"
  value       = module.deployment.public_ip
}

output "application_url" {
#  description = "Application URL"
  value       = module.deployment.application_url
}

output "cloud_init_log_command" {
#  description = "Cloud Init tail command"
  value       = module.deployment.cloud_init_log_command
}
