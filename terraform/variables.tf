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
