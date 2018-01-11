// Use default credentials, i.e. from ~/.aws/credentials
//variable "access_key" {}
//variable "secret_key" {}
variable "region" {
  default = "eu-west-1"
}

provider "aws" {
//  access_key = "${var.access_key}"
//  secret_key = "${var.secret_key}"
  region     = "${var.region}"
}

resource "aws_security_group" "kafka_sg" {
  name = "kafka_sg"
  description = "Security group for kafka servers"

  ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_key_pair" "kafka_key" {
  key_name = "kafka-key"
  public_key = "${file("ssh_keys/kafka-key.pub")}"
}

resource "aws_instance" "kafka_ec2" {
  count           = 1
  ami             = "ami-c145c1b8" // Ubuntu 17.04
  instance_type   = "m3.medium"
  security_groups = ["${aws_security_group.kafka_sg.name}"]
  key_name = "${aws_key_pair.kafka_key.key_name}"
}

resource "aws_eip" "kafka_ip" {
  instance = "${aws_instance.kafka_ec2.id}"
}

output "kafka_ip" {
  value = "${aws_eip.kafka_ip.public_ip}"
}
