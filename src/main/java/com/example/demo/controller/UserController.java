package com.example.demo.controller;

import com.example.demo.ErrorHandeling.UserStatus;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Validator.UserValidator;
import com.example.demo.constant.UserConstants;
import com.example.demo.exception.DataNotFoundExeception;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.UserAuthrizationExeception;
import com.example.demo.service.UserService;
import com.example.demo.model.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;
import jakarta.servlet.http.HttpServletRequest;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import com.timgroup.statsd.StatsDClient;

@RestController
//@RequestMapping(path = "api/user")
public class UserController {

    // create user
    @Autowired
    private UserService userService;

    //@Autowired
    //private StatsDClient metricsClient;


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserValidator userValidator;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

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

    //update user by UUID id
    @RequestMapping(path = "/v1/user/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable UUID userId , @RequestBody User user ,HttpServletRequest request, Errors error) {

        //raghav method start
        //metricsClient.incrementCounter("endpoint./v1/.account.id.http.put");

//        Optional<User> u = null;
//        try {
//            u = userRepository.findById(userId);
//            System.out.println(user);
//        }
//        catch (Exception e){
//            LOGGER.warn("Bad Request");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
//        }
//
//        String loggedUser = "";
//        String username = "";
//        String password = "";
//
//        try {
//            loggedUser = authenticatedUser(request);
//            username = loggedUser.split(" ")[0];
//            password = loggedUser.split(" ")[1];
//        }
//        catch(Exception e){
//            LOGGER.warn("Unauthorized to access");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication Error");
//        }
        //raghav method end

        //anurag method start
        try {
            if(userId.toString().isBlank()||userId.toString().isEmpty()) {
                throw new InvalidInputException("Enter Valid User Id");
            }
            userService.isAuthorised(userId,request.getHeader("Authorization").split(" ")[1]);
            if(error.hasErrors()) {
                String response = error.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(","));
                throw new InvalidInputException(response);
            }
            userService.updateUser(user,userId);
            return ResponseEntity.status(HttpStatus.CREATED).body("User Updated");
        }
        catch (UserAuthrizationExeception e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        }
        catch (DataNotFoundExeception e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (InvalidInputException e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
            return new ResponseEntity<String>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //anurag method end

//        userService.updateUser(user,userId);
//        return ResponseEntity.status(HttpStatus.CREATED).body("User Updated");
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
