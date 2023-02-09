package com.example.demo.service;

import com.example.demo.ErrorHandeling.UserStatus;
import com.example.demo.Repository.UserRepository;
import com.example.demo.exception.*;
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


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

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

    public void updateUser(User user, int id) {
        user.setPassword(PassEncoder().encode(user.getPassword()));
        user.setAccount_updated(LocalDateTime.now());
        userRepository.setUserInfoById(user.getFirst_name(),user.getLast_name(), user.getPassword() ,user.getAccount_updated(),id);
    }
    public Optional<User> fetchUserbyId(int id){
        return userRepository.findById(id);
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

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
   }

    public boolean isAuthorisedForGet(int userId,String tokenEnc) throws DataNotFoundExeception, UserAuthrizationExeception, ForbidenAccess {
        User user=getUserDetailsAuth(userId);
        byte[] token = Base64.getDecoder().decode(tokenEnc);
        String decodedStr = new String(token, StandardCharsets.UTF_8);
        String userName = decodedStr.split(":")[0];
        String passWord = decodedStr.split(":")[1];
        authUsernamePwd(userName,passWord);
        System.out.println("Value of Token" + " "+ decodedStr);
        if(!(user.getUsername().equals(userName)) || !(PassEncoder().matches(passWord,user.getPassword()))){
            throw new ForbidenAccess("Forbidden to access");
        }
        return true;
    }
    public boolean isAuthorisedForPut(int userId,String tokenEnc, User userRequest) throws DataNotFoundExeception, UserAuthrizationExeception,InvalidInputException,ForbidenAccess {
        byte[] token = Base64.getDecoder().decode(tokenEnc);
        String decodedStr = new String(token, StandardCharsets.UTF_8);
        String userName = decodedStr.split(":")[0];
        String passWord = decodedStr.split(":")[1];
        authUsernamePwd(userName,passWord);
        System.out.println("Value of Token" + " "+ decodedStr);
        if(!userRequest.getUsername().equals(userName)){
            throw new InvalidInputException("Enter correct username");
        }
        User user=getUserDetailsAuth(userId);
        if(!(user.getUsername().equals(userName)) || !(PassEncoder().matches(passWord,user.getPassword()))){
            throw new ForbidenAccess("Forbidden to access");
        }
        return true;
    }
    public User getUserDetailsAuth(int userId) throws DataNotFoundExeception{
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }
        throw new DataNotFoundExeception("User Not Found");
    }
    public Boolean authUsernamePwd(String username, String pwd) throws DataNotFoundExeception{
        try {
            User user = userRepository.findByUsername(username);
            if (!(PassEncoder().matches(pwd,user.getPassword()))) {
                throw new DataNotFoundExeception("Invalid Username/password");
            }
        }catch (Exception e){
            throw new DataNotFoundExeception("Username not found");
        }
        return null;
    }
    public BCryptPasswordEncoder PassEncoder() {
        return new BCryptPasswordEncoder();
    }
}
