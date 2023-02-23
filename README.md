# CSYE6225 - Cloud Computing Assignment 1

Web application- SpringBoot<br/>

Prerequisites<br/>
JAVA: jdk 17, SpringBoot version 3.x.x<br/>
mysql database<br/>

Build Instructions<br/>

```bash

Clone this repository into the local system

Open the CLI

mvn clean install

mvn spring-boot:run
```


## Assignment:

Create a private Organization and repository. repository name must be webapp.<br/>
(Continou Integration) Fork the GitHub repository in your namespace and do all development work on your fork.<br/>
On completion of feature development sync the repo.<br/>

Create a api:<br/>
Create a new user<br/>
A user want to create an account by providing the following information.<br/>
Email Address<br/>
Password<br/>
First Name<br/>
Last Name<br/>
account_created field for the user should be set to the current time when user creation is successful.<br/>
Users should not be able to set values for account_created and account_updated. Any value provided for these fields must be ignored.<br/>
Password should never be returned in the response payload.<br/>
As a user,  I expect to use my email address as my username.<br/>
Application must return 400 Bad Request HTTP response code when a user account with the email address already exists.<br/>
As a user, I expect my password to be stored securely using the BCrypt password hashing scheme Links to an external site. with salt Links to an external site..<br/>
Update user inf ormation<br/>
As a user, I want to update my account information. I should only be allowed to update the following fields.<br/>
First Name<br/>
Last Name<br/>
Password<br/>
Attempt to update any other field should return 400 Bad Request HTTP response code.<br/>
account_updated field for the user should be updated when the user update is successful.<br/>
A user can only update their own account information.<br/>
Get User Information<br/>
As a user, I want to get my account information. Response payload should return all fields for the user except for password.<br/>

API - <br/>
'http://localhost:8080/v1/user'<br/>
'http://localhost:8080/v1/user/{userid}'<br/>
'http://localhost:3000/healthz'<br/>
<br/>
Useful GitHub Commands<br/>

git status<br/>
git init<br/>
git clone <br/>
git add .<br/>
git commit -m 'COMMENT'<br/>
git push rk av01<br/>
Pull request from Namespace - Personal workspace to Organization repo<br/>

|Name	         | NEU ID	 |   Email Address              |
|----------------|-----------|------------------------------|
|Trisha Ghorpade |002920687  | ghorpade.t@northeastern.edu  |
