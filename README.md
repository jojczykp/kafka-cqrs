# Prerequisites
- AWS account (https://aws.amazon.com/premiumsupport/knowledge-center/create-and-activate-aws-account/)
- Terraform (https://www.terraform.io/intro/getting-started/install.html)


# Setup Infrastructure

## Apply
- `cd terraform`
- `ssh-keygen -t rsa -C "kafka-key" -P '' -f ssh-keys/kafka-key`
- `terraform init`
- `terraform plan`
- `terraform apply`
- `terraform show`
- `cd ..`
- Keep IP printed out by terraform

## Inspect
- `cd terraform`
- `ssh -i ssh-keys/kafka-key ubuntu@<IP>` (exit)
- `telnet <IP> 9092` (^], quit)
- `cd ..`


# Run Application
- `gradle init`


# Cleanup
- `cd terraform`
- `terraform destroy`
- `rm ssh-keys/*`
- `cd ..`


# References
- https://github.com/hashicorp/atlas-examples/blob/master/setup/ssh.md
