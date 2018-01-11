provider "aws" {
  //  Use default credentials, i.e. from ~/.aws/credentials
  //  access_key = "ACCESS_KEY_HERE"
  //  secret_key = "SECRET_KEY_HERE"
  region     = "us-east-1"
}

resource "aws_instance" "example" {
  ami           = "ami-80ca47e6"
  instance_type = "t2.micro"
}
