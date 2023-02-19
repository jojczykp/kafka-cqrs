output "availability_zone" {
  description = "Availability Zone"
  value       = data.aws_availability_zones.available.names
}

output "public_ip" {
  description = "Instance Public IP"
  value       = aws_instance.instance.public_ip
}

output "cloud_init_log_command" {
  description = "Cloud Init tail command"
  value       = "ssh -i ~/.aws/${var.ssh_key_name}.pem -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null ec2-user@${aws_instance.instance.public_ip} sudo tail -f /var/log/cloud-init-output.log"
}
