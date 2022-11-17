provider "aws" {
  region = "eu-west-1"
}

data "aws_availability_zones" "available" {
  state = "available"
}

data "aws_availability_zone" "selected" {
  zone_id = data.aws_availability_zones.available.zone_ids[0]
}

resource "aws_instance" "instance" {
  ami               = "ami-08bac620dc84221eb"  // Ubuntu 20.04 LTS
  availability_zone = data.aws_availability_zone.selected.name
  key_name          = var.ssh_key_name
  instance_type     = "t3a.medium"
  security_groups   = [aws_security_group.kafka-cqrs.name]
  user_data         = file("${path.module}/user-data.sh")

  root_block_device {
    volume_size = 10
  }

  tags = {
    Name = "kafka-cqrs (${var.environment})"
  }
}

resource "aws_security_group" "kafka-cqrs" {
  name        = "kafka-cqrs (${var.environment})"
  description = "kafka-cqrs (${var.environment})"

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
