CSYE6225-Cloud Computing Assignment 1

Web application - Spring Boot

Build Instructions

Clone this repository into the local system
Open the CLI
mvn clean install
mvn spring-boot:run
Run the test

Assignment:

Create a private Organization and repository. repository name must be webapp.
Fork the GitHub repository in your namespace and do all development work on your fork.
On completion of feature development sync the repo.
Students to demo the web application from their laptops.
APIs can be demoed using any Postman or Restlet or some other REST client but not via the browser.
Check for UI. The application cannot have UI.
Verify passwords are encrypted with BCrypt hashing and salt in the database.
Verify that authentication is done via basic auth (token-based) and not session-based. JWT is not allowed.
Check the response payload to make sure it meets the assignment objective. The Password field should not be part of the response payload.
Test for duplicate account creation in the application.
The application should NOT allow multiple accounts with the same email address.
Test updating fields such as account_created and account_updated.
Users should never be able to set values for them. These fields are set by the application.
Verify non-email username cannot be used for account creation.

Endpoint - 'http://localhost:3000/healthz'

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
