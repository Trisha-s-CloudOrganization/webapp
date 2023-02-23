#!/bin/bash
sudo yum update
sudo yum upgrade
echo Start Java Installation
sudo yum install java-17-amazon-corretto -yecho 
"export JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto.x86_64" 
>>~/.bashrcecho "export PATH=$PATH:$JAVA_HOME/bin" 
>>~/.bashrcecho Java Locationjava --version
sudo yum install maven -y
echo completed Java Installation
sudo yum install -y tomcat - y
sudo systemctl start tomcat
sudo systemctl enable tomcat
sudo amazon-linux-extras install -y epel
sudo yum install -y https://dev.mysql.com/get/mysql80-community-release-el7-5.noarch.rpm
sudo yum install -y mysql-community-server
sudo systemctl start mysqld
sudo systemctl enable mysqld
passwords=$(sudo grep 'temporary password' /var/log/mysqld.log | awk {'print $13'})
mysql -uroot -p$passwords --connect-expired-password -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'Trisha@16';"
mysql -u root -pTrisha@16 -e "create database sonoo;"
