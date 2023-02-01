package com.example.demo.controller;

import com.example.demo.ErrorHandeling.UserStatus;
import com.example.demo.Repository.UserRepository;
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
import jakarta.servlet.http.HttpServletRequest;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@RestController
//@RequestMapping(path = "api/user")
public class UserController {

    // create user
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
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
    }

    @RequestMapping(path = "/v1/user/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable UUID userId , @RequestBody User user ) {
        userService.updateUser(user,userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("User Updated");
    }
    @RequestMapping(path = "/healthz", method = RequestMethod.GET)
    public void healthZ() {}

    private String authenticatedUser(HttpServletRequest request){

        String tokenEnc = request.getHeader("Authorization").split(" ")[1];
        byte[] token = Base64.getDecoder().decode(tokenEnc);
        String decodedStr = new String(token, StandardCharsets.UTF_8);

        String userName = decodedStr.split(":")[0];
        String passWord = decodedStr.split(":")[1];
        System.out.println("Value of Token" + " "+ decodedStr);

        return (userName + " " + passWord);

    }
}
