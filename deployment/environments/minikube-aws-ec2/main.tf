provider "aws" {
  region = "eu-west-1"
}

variable "ssh_key_name" {
  type    = string
  default = "initial"
}

data "aws_availability_zones" "available" {
  state = "available"
}

data "aws_availability_zone" "selected" {
  zone_id = data.aws_availability_zones.available.zone_ids[0]
}

resource "aws_instance" "instance" {
  ami               = "ami-089cc16f7f08c4457"  // Ubuntu 18.04 LTS
  availability_zone = data.aws_availability_zone.selected.name
  key_name          = var.ssh_key_name
  instance_type     = "t3a.small"
  security_groups   = [aws_security_group.kafka-cqrs.name]
  user_data         = file("user-data.sh")

  root_block_device {
    volume_size = 10
  }
}


resource "aws_security_group" "kafka-cqrs" {
  name        = "kafka-cqrs"
  description = "kafka-cqrs"

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "All"
    from_port   = 0
    to_port     = 0
    protocol    = -1
    cidr_blocks = ["0.0.0.0/0"]
  }
}

output "availability_zone" {
  description = "Availability Zone"
  value       = data.aws_availability_zones.available.names
}

output "public_ip" {
  description = "Instance Public IP"
  value       = aws_instance.instance.public_ip
}

output "application_url" {
  description = "Application URL"
  value       = "http://${aws_instance.instance.public_dns}"
}

output "cloud_init_log_command" {
  description = "Cloud Init tail command"
  value       = "ssh -i ~/.aws/${var.ssh_key_name}.pem ubuntu@${aws_instance.instance.public_ip} tail -f /var/log/cloud-init-output.log"
}

