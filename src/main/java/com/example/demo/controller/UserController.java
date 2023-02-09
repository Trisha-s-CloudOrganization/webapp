package com.example.demo.controller;

import com.example.demo.ErrorHandeling.UserStatus;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Validator.UserValidator;
import com.example.demo.constant.UserConstants;
import com.example.demo.exception.DataNotFoundExeception;
import com.example.demo.exception.ForbidenAccess;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.UserAuthrizationExeception;
import com.example.demo.service.UserService;
import com.example.demo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
//@RequestMapping(path = "api/user")
public class UserController {
    @Autowired
    private UserService userService;
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
        UserStatus userStatus;
        try {
            if (user.getId() != 0 || user.getAccount_created() != null || user.getAccount_updated() != null)
                throw new InvalidInputException("Id, creation date, last modified date can not be modified");
            if (errors.hasErrors()) {
                userStatus = userService.getUserStatus(errors);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userStatus);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
            }
        } catch (InvalidInputException e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
            return new ResponseEntity<String>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //update user by UUID id
    @RequestMapping(path = "/v1/user/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable int userId , @RequestBody User user ,HttpServletRequest request, Errors error) {
        try {
            if(userId == 0) {
                throw new InvalidInputException("Enter Valid User Id");
            }
            if(user.getId() != 0 || user.getAccount_created()!=null || user.getAccount_updated()!=null)
                throw new InvalidInputException("Id, creation date, last modified date can not be modified");
            userService.isAuthorisedForPut(userId,request.getHeader("Authorization").split(" ")[1],user);
            if(error.hasErrors()) {
                String response = error.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(","));
                throw new InvalidInputException(response);
            }
            userService.updateUser(user,userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User Updated");
        }catch (DataNotFoundExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (InvalidInputException e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (UserAuthrizationExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.UNAUTHORIZED);
        }catch (ForbidenAccess e){
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        } catch(Exception e) {
            return new ResponseEntity<String>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/v1/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> fetchUserByID(@PathVariable int userId, HttpServletRequest request) {
        try {
            if(userId == 0) {
                throw new InvalidInputException("Enter Valid User Id");
            }
            userService.isAuthorisedForGet(userId,request.getHeader("Authorization").split(" ")[1]);
            return ResponseEntity.status(HttpStatus.OK).body(userService.fetchUserbyId(userId));
        } catch (DataNotFoundExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (InvalidInputException e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (UserAuthrizationExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.UNAUTHORIZED);
        }catch (ForbidenAccess e){
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        } catch(Exception e) {
            return new ResponseEntity<String>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/healthz", method = RequestMethod.GET)
    public void healthZ() {}
}
