variable "region" {
  default = "eu-west-1"
}

variable "kafka_user" {
  type = "map"
  default = {
    name = "ubuntu"
    private_key_path = "ssh-keys/kafka-key"
    public_key_path = "ssh-keys/kafka-key.pub"
  }
}

variable "kafka_port" {
  default = "9092"
}

provider "aws" {
  region = "${var.region}"
}

resource "aws_key_pair" "kafka_key" {
  key_name = "kafka-key"
  public_key = "${file("${var.kafka_user["public_key_path"]}")}"
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
    from_port   = "${var.kafka_port}"
    to_port     = "${var.kafka_port}"
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

resource "aws_instance" "kafka_ec2" {
  count           = 1
  ami             = "ami-4d46d534" // Ubuntu 16.04 LTS
  instance_type   = "m3.medium"
  security_groups = ["${aws_security_group.kafka_sg.name}"]
  key_name        = "${aws_key_pair.kafka_key.key_name}"

  provisioner "file" {
    source      = "setup-scripts"
    destination = "/tmp"
    connection  = {
      user        = "${var.kafka_user["name"]}"
      private_key = "${file("${var.kafka_user["private_key_path"]}")}"
    }
  }

  provisioner "remote-exec" {
    inline = [
      "chmod +x /tmp/setup-scripts/*.sh",
      "sudo /tmp/setup-scripts/setup-java.sh",
      "sudo /tmp/setup-scripts/download.sh",
      "sudo /tmp/setup-scripts/setup-zookeeper.sh",
      "sudo /tmp/setup-scripts/setup-kafka.sh ${count.index}",
    ]
    connection  = {
      user        = "${var.kafka_user["name"]}"
      private_key = "${file("${var.kafka_user["private_key_path"]}")}"
    }
  }
}

resource "aws_eip" "kafka_ip" {
  instance = "${aws_instance.kafka_ec2.id}"
}

output "kafka_ssh" {
  value = "ssh -i ${var.kafka_user["private_key_path"]} ${var.kafka_user["name"]}@${aws_eip.kafka_ip.public_ip}"
}

output "kafka_verify" {
  value = "telnet ${aws_eip.kafka_ip.public_ip} ${var.kafka_port}"
}
