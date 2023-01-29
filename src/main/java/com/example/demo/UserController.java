package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping(path = "api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/adduser")
    public User addUser(@RequestBody User user){
        return userService.saveUser(user);
    }
//    @JsonIgnoreProperties(value = {"password"})
    @GetMapping("/getusers")
    public List<User> getUsers(){
        return userService.getUsers();
    }
}
