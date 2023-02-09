package com.example.demo.Validator;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.passay.*;
import java.util.Arrays;

public class UserValidator implements Validator {
    @Autowired
    private UserService userService;
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "first_name", "Firstname required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "last_name", "Lastname required");

        if(errors.hasErrors()) return;

        User user = (User) target;

        if(!user.getUsername().matches("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) errors.rejectValue("username", "username format is wrong");
        //if(user.getId()!=0)errors.rejectValue("id","id can not added by user");
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(9, 30),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()));
        RuleResult result = validator.validate(new PasswordData(user.getPassword()));
        if(!result.isValid()) errors.rejectValue("password", "Password must 9-30 characters long and must have Uppercase, Lowercase, Special characters and Digits");

        if(errors.hasErrors()) return;

        if(userService.isEmailPresent(user.getUsername())) errors.rejectValue("username", "account already exists");
    }
}
