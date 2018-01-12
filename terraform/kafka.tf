variable "region" {
  default = "eu-west-1"
}

provider "aws" {
  region = "${var.region}"
}

resource "aws_security_group" "kafka_sg" {
  name        = "kafka_sg"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 9092
    to_port     = 9092
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_key_pair" "kafka_key" {
  key_name = "kafka-key"
  public_key = "${file("ssh-keys/kafka-key.pub")}"
}

resource "aws_instance" "kafka_ec2" {
  count           = 1
  ami             = "ami-c145c1b8" // Ubuntu 17.04
  instance_type   = "m3.medium"
  security_groups = ["${aws_security_group.kafka_sg.name}"]
  key_name        = "${aws_key_pair.kafka_key.key_name}"

  provisioner "file" {
    source      = "setup-scripts"
    destination = "/tmp"
    connection  = {
      user        = "ubuntu"
      private_key = "${file("ssh-keys/kafka-key")}"
    }
  }

  provisioner "remote-exec" {
    inline = [
      "chmod +x /tmp/setup-scripts/*.sh",
      "sudo /tmp/setup-scripts/setup-java.sh",
      "sudo /tmp/setup-scripts/setup-kafka.sh ${count.index}",
    ]
    connection  = {
      user        = "ubuntu"
      private_key = "${file("ssh-keys/kafka-key")}"
    }
  }
}

resource "aws_eip" "kafka_ip" {
  instance = "${aws_instance.kafka_ec2.id}"
}

output "kafka_ssh" {
  value = "ssh -i ssh-keys/kafka-key ubuntu@${aws_eip.kafka_ip.public_ip}"
}

output "kafka_verify" {
  value = "telnet ${aws_eip.kafka_ip.public_ip} 9092"
}
