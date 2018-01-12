# Prerequisites
- Terraform
- AWS account


# Run
## Terraform
- cd terraform
- ssh-keygen -t rsa -C "kafka-key" -P '' -f ssh-keys/kafka-key
- terraform init
- terraform plan
- terraform apply
- terraform show
- cd ..

# Inspect
cd terraform
ssh -i ssh-keys/kafka-key ubuntu@\<IP\>
cd ..

## Application
- gradle init


# Shutdown
- cd terraform
- terraform destroy
- rm ssh-keys/*
- cd ..

# References
- https://github.com/hashicorp/atlas-examples/blob/master/setup/ssh.md
