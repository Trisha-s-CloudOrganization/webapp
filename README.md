# CSYE6225-Cloud Computing Assignment 1

Web application - Spring Boot

Prerequisites
JAVA: jdk 17, SpringBoot version 3.x.x
mysql database

Build Instructions

```bash

Clone this repository into the local system

Open the CLI

mvn clean install

mvn spring-boot:run
```


##Assignment:

Create a private Organization and repository. repository name must be webapp.

(Continou Integration) Fork the GitHub repository in your namespace and do all development work on your fork.

On completion of feature development sync the repo.

Create a api:
Create a new user
As a user, I want to create an account by providing the following information.
Email Address
Password
First Name
Last Name
account_created field for the user should be set to the current time when user creation is successful.
Users should not be able to set values for account_created and account_updated. Any value provided for these fields must be ignored.
Password should never be returned in the response payload.
As a user, I expect to use my email address as my username.
Application must return 400 Bad Request HTTP response code when a user account with the email address already exists.
As a user, I expect my password to be stored securely using the BCrypt password hashing scheme Links to an external site. with salt Links to an external site..
Update user information
As a user, I want to update my account information. I should only be allowed to update the following fields.
First Name
Last Name
Password
Attempt to update any other field should return 400 Bad Request HTTP response code.
account_updated field for the user should be updated when the user update is successful.
A user can only update their own account information.
Get user information
As a user, I want to get my account information. Response payload should return all fields for the user except for password.

API - 
'http://localhost:8080/v1/user'
'http://localhost:8080/v1/user/{userid}'
'http://localhost:3000/healthz'

Some GitHub Commands

git status
git init
git clone 
git add .
git commit -m 'COMMENT'
git push rk av01
Pull request from Namespace - Personal workspace to Organization repo

Name	NEU ID	Email Address
Trisha Ghorpade 002920687   ghorpade.t@northeastern.edu
