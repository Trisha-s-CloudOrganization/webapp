package com.example.demo.service;

import com.example.demo.ErrorHandeling.UserStatus;
import com.example.demo.Repository.UserRepository;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user){
       user.setPassword(passwordEncoder.encode(user.getPassword()));
       return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public Boolean isEmailPresent(String username) {
        return userRepository.isEmailPresent(username) > 0 ? true : false;
    }

    public UserStatus getUserStatus(BindingResult errors) {
            FieldError usernameError = errors.getFieldError("username");
            FieldError passwordError = errors.getFieldError("password");
            FieldError firstnameError = errors.getFieldError("firstName");
            FieldError lastnameError = errors.getFieldError("lastName");
            String firstnameErrorMessage = firstnameError == null ? "-" : firstnameError.getCode();
            String lastnameErrorMessage = lastnameError == null ? "-" : lastnameError.getCode();
            String usernameErrorMessage = usernameError == null ? "-" : usernameError.getCode();
            String passwordErrorMessage = passwordError == null ? "-" : passwordError.getCode();
            return new UserStatus(usernameErrorMessage, passwordErrorMessage,firstnameErrorMessage,lastnameErrorMessage);
    }
}
