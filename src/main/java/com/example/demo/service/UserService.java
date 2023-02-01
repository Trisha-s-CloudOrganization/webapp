package com.example.demo.service;

import com.example.demo.ErrorHandeling.UserStatus;
import com.example.demo.Repository.UserRepository;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user){
       user.setPassword(passwordEncoder.encode(user.getPassword()));
       return userRepository.save(user);
    }

    public void updateUser(User user, UUID id) {
        userRepository.setUserInfoById(user.getFirst_name(),user.getLast_name(), user.getPassword() ,id);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user==null) throw new UsernameNotFoundException("User with given emailId does not exist");
        else return new CustomUserDetails(user);
    }
}
