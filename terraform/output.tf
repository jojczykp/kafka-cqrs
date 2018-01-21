output "kafka_ssh" {
  value = "ssh -i ${var.kafka_user["private_key_path"]} ${var.kafka_user["name"]}@${aws_eip.kafka_ip.public_ip}"
}

output "kafka_verify" {
  value = "telnet ${aws_eip.kafka_ip.public_ip} ${var.kafka_port}"
}
