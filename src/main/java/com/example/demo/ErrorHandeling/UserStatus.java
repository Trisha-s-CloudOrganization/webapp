package com.example.demo.ErrorHandeling;

//public class UserStatus {
//}

public class UserStatus {
    private String usernameError;
    private String passwordError;

    private String firstNameError;
    private String lastNameError;

    public UserStatus() {
        usernameError = "-";
        passwordError = "-";
        firstNameError= "-";
        lastNameError = "-";
    }

    public UserStatus(String usernameError, String passwordError, String firstNameError, String lastNameError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.firstNameError = firstNameError;
        this.lastNameError = lastNameError;
    }

    public String getUsernameError() {
        return usernameError;
    }

    public void setUsernameError(String usernameError) {
        this.usernameError = usernameError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getFirstNameError() {
        return firstNameError;
    }

    public void setFirstNameError(String firstNameError) {
        this.firstNameError = firstNameError;
    }

    public String getLastNameError() {
        return lastNameError;
    }

    public void setLastNameError(String lastNameError) {
        this.lastNameError = lastNameError;
    }
}