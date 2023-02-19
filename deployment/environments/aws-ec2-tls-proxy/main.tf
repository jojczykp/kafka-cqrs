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
  ami               = "ami-084025e8ff495e38b"  // Amazon Linux 2 (arm64)
  availability_zone = data.aws_availability_zone.selected.name
  key_name          = var.ssh_key_name
  instance_type     = "t4g.nano"
  security_groups   = [aws_security_group.kafka-cqrs-ssl-proxy.name]
  user_data         = templatefile("user-data.sh", {
    domain = var.domain
    email = var.email
    target_ip = var.target_ip
  })

  root_block_device {
    volume_size = 8
  }

  tags = {
    Name = "kafka-cqrs-ssl-proxy"
  }
}

resource "aws_security_group" "kafka-cqrs-ssl-proxy" {
  name        = "kafka-cqrs-ssl-proxy"
  description = "kafka-cqrs-ssl-proxy"

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

  ingress {
    description = "HTTPS"
    from_port   = 443
    to_port     = 443
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
