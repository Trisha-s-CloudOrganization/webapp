#!/bin/bash
sudo yum update
sudo yum upgrade
echo Start Java Installation
sudo yum install java-17-amazon-corretto -y
echo "export JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto.x86_64" >>~/.bashrc
echo "export PATH=$PATH:$JAVA_HOME/bin" >>~/.bashrc
echo Java Location
java --version
echo completed Java Installation
sudo yum install -y tomcat - y
sudo systemctl start tomcat
sudo systemctl enable tomcat

echo "Installing Cloudwatch Agent"
sudo yum install amazon-cloudwatch-agent -y
#sudo curl https://s3.amazonaws.com/amazoncloudwatch-agent/amazon_linux/amd64/latest/amazon-cloudwatch-agent.rpm -O
#wget download-link
#sudo rpm -U ./amazon-cloudwatch-agent.rpm

#sudo curl -o /root/amazon-cloudwatch-agent.deb https://s3.amazonaws.com/amazoncloudwatch-agent/debian/amd64/latest/amazon-cloudwatch-agent.deb
#sudo dpkg -i -E /root/amazon-cloudwatch-agent.deb

echo "Configuring Cloudwatch Agent - do it when server start"
#sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/ec2-user/cloudwatch-config.json -s