package com.example.demo.controller;

import com.example.demo.ErrorHandeling.UserStatus;
import com.example.demo.Validator.UserValidator;
import com.example.demo.service.UserService;
import com.example.demo.model.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;

import javax.validation.Valid;
import java.util.List;

@RestController
//@RequestMapping(path = "api/user")
public class UserController {

    // create user
    @Autowired
    private UserService userService;
    @Autowired
    private UserValidator userValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }
    @PostMapping("/v1/user")
    public ResponseEntity<?>  addUser(@Valid @RequestBody User user , BindingResult errors, HttpServletResponse response) throws Exception{
//      public User  addUser(@Valid @RequestBody User user) throws Exception{
            UserStatus userStatus;
        if(errors.hasErrors()) {
            userStatus = userService.getUserStatus(errors);
            //LOGGER.warn("Bad Request " + registrationStatus);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userStatus);
        }else {
            //return ResponseEntity.status(HttpStatus.CREATED).body("");
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
        }

//        return userService.saveUser(user);
    }

    //delete user: confirm if needed


    //update user(fname,lname,password)



    // get all user by email
//    @JsonIgnoreProperties(value = {"password"})
    @GetMapping("/v1/user/{userId}")
    public List<User> getUsers(){
        return userService.getUsers();
    }
}
