package com.example.demo.controller;

import com.example.demo.ErrorHandeling.UserStatus;
import com.example.demo.Validator.UserValidator;
import com.example.demo.constant.UserConstants;
import com.example.demo.exception.DataNotFoundExeception;
import com.example.demo.exception.ForbidenAccess;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.UserAuthrizationExeception;
import com.example.demo.service.UserService;
import com.example.demo.model.User;
import com.timgroup.statsd.StatsDClient;
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
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
//@RequestMapping(path = "api/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private StatsDClient statsDClient;
    @Autowired
    private UserService userService;
    @Autowired
    private UserValidator userValidator;
    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }
    @PostMapping("/v1/user")
    public ResponseEntity<?>  addUser(@Valid @RequestBody User user , BindingResult errors){
        UserStatus userStatus;
        statsDClient.incrementCounter("v1.user.post");
        try {
            if (user.getId() != 0 || user.getAccount_created() != null || user.getAccount_updated() != null)
                throw new InvalidInputException("Id, creation date, last modified date can not be modified");
            if (errors.hasErrors()) {
                userStatus = userService.getUserStatus(errors);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userStatus);
            } else {
                LOGGER.info("Valid Data received to upload for user:{ "+user+"} in S3 at UserController:addUser");
                return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
            }
        } catch (InvalidInputException e) {
            LOGGER.warn("Invalid Input Exception for user:"+user+" in UserController:addUser. Exception: "+e);
            return new ResponseEntity<>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
            LOGGER.error("Internal server error in UserController:addUser. Exception = "+e);
            return new ResponseEntity<>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/v1/user/t")
    public ResponseEntity<?>  addUserT(@Valid @RequestBody User user , BindingResult errors){
        UserStatus userStatus;
        statsDClient.incrementCounter("v1.user.post");
        try {
            if (user.getId() != 0 || user.getAccount_created() != null || user.getAccount_updated() != null)
                throw new InvalidInputException("Id, creation date, last modified date can not be modified");
            if (errors.hasErrors()) {
                userStatus = userService.getUserStatus(errors);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userStatus);
            } else {
                LOGGER.info("Valid Data received to upload for user:{ "+user+"} in S3 at UserController:addUser");
                return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
            }
        } catch (InvalidInputException e) {
            LOGGER.warn("Invalid Input Exception for user:"+user+" in UserController:addUser. Exception: "+e);
            return new ResponseEntity<>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
            LOGGER.error("Internal server error in UserController:addUser. Exception = "+e);
            return new ResponseEntity<>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //update user by UUID id
    @RequestMapping(path = "/v1/user/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable int userId , @RequestBody User user ,HttpServletRequest request, Errors error) {
        statsDClient.incrementCounter("v1.user.userId.put");
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
            LOGGER.info("Authentication Successful for user:"+user+" in ProductController:updateUser");
            userService.updateUser(user,userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User Updated");
        }catch (DataNotFoundExeception e) {
            LOGGER.warn("DataNotFoundException for user:"+user+" in ProductController:updateUser. Exception: "+e);
            return new ResponseEntity<>( e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (InvalidInputException e) {
            LOGGER.warn("Invalid Input Exception for user:"+user+" in ProductController:updateUser. Exception: "+e);
            return new ResponseEntity<>( e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (UserAuthrizationExeception e) {
            LOGGER.warn("UserAuthorizationException for user:"+user+" in ProductController:updateUser. Exception: "+e);
            return new ResponseEntity<>( e.getMessage(),HttpStatus.UNAUTHORIZED);
        }catch (ForbidenAccess e){
            LOGGER.warn("ForbiddenAccess for user:"+user+" in ProductController:updateUser. Exception: "+e);
            return new ResponseEntity<>( e.getMessage(),HttpStatus.FORBIDDEN);
        } catch(Exception e) {
            LOGGER.error("Internal server error in ProductController:updateUser. Exception = "+e);
            return new ResponseEntity<>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/v1/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> fetchUserByID(@PathVariable int userId, HttpServletRequest request) {
        statsDClient.incrementCounter("v1.user.userId.get");
        try {
            if(userId == 0) {
                throw new InvalidInputException("Enter Valid User Id");
            }
            userService.isAuthorisedForGet(userId,request.getHeader("Authorization").split(" ")[1]);
            LOGGER.info("Authentication Successful for userId:"+userId+" in UserController:fetchUserByID");
            return ResponseEntity.status(HttpStatus.OK).body(userService.fetchUserbyId(userId));
        } catch (DataNotFoundExeception e) {
            LOGGER.warn("DataNotFoundException for userId:"+userId+" in UserController:fetchUserByID. Exception: "+e);
            return new ResponseEntity<>( e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (InvalidInputException e) {
            LOGGER.warn("Invalid Input Exception for userId:"+userId+" in UserController:fetchUserByID. Exception: "+e);
            return new ResponseEntity<>( e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (UserAuthrizationExeception e) {
            LOGGER.warn("UserAuthorizationException for userId:"+userId+" in UserController:fetchUserByID. Exception: "+e);
            return new ResponseEntity<>( e.getMessage(),HttpStatus.UNAUTHORIZED);
        }catch (ForbidenAccess e){
            LOGGER.warn("ForbiddenAccess for userId:"+userId+" in UserController:fetchUserByID. Exception: "+e);
            return new ResponseEntity<>( e.getMessage(),HttpStatus.FORBIDDEN);
        } catch(Exception e) {
            LOGGER.error("Internal server error in UserController:fetchUserByID. Exception = "+e);
            return new ResponseEntity<>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(path = "/healthz", method = RequestMethod.GET)
    public void healthZ() {}
    
    @RequestMapping(path = "/healthc", method = RequestMethod.GET)
    public void healthNew() {}
}
