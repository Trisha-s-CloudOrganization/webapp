package com.example.demo.controller;

import com.example.demo.constant.UserConstants;
import com.example.demo.exception.DataNotFoundExeception;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.InvalidProductQty;
import com.example.demo.exception.UserAuthrizationExeception;
import com.example.demo.model.Products;
import com.example.demo.model.User;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;
    //post
    @PostMapping("/v1/product")
    public ResponseEntity<?> addProduct(@RequestBody Products products, HttpServletResponse response,HttpServletRequest request, Errors error) throws Exception{
//      public User  addUser(@Valid @RequestBody User user) throws Exception{
        if(products.getId() != 0 || products.getDate_added()!=null || products.getDate_last_updated()!=null)
            throw new InvalidInputException("Id, creation date, last modified date can not be added");
        if(error.hasErrors()) {
            String responses = error.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(","));
            throw new InvalidInputException(responses);
        }else {
            try {
                validateProductQty(products.getProduct_qty());
                User authuser = productService.authCredential(request.getHeader("Authorization").split(" ")[1]);
                products.setOwner_user_id(authuser.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(products));
            }catch (UsernameNotFoundException e){
                return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
            }catch (UserAuthrizationExeception e){
                return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
            }
            catch(Exception e) {
                return new ResponseEntity<String>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    //put
    @RequestMapping(path = "/v1/product/{productId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateProduct(@PathVariable int productId , @RequestBody Products products, HttpServletRequest request, Errors error) {
        try {
            if(productId == 0) {
                throw new InvalidInputException("Enter Valid User Id");
            }
            if(products.getId() != 0 || products.getDate_added()!=null || products.getDate_last_updated()!=null)
                throw new InvalidInputException("Id, creation date, last modified date can not be added");
            productService.isAuthorisedForPut(productId,request.getHeader("Authorization").split(" ")[1], products);
            if(error.hasErrors()) {
                String response = error.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(","));
                throw new InvalidInputException(response);
            }
            validateProductQty(products.getProduct_qty());
            productService.updateProduct(products,productId);
            return ResponseEntity.status(HttpStatus.CREATED).body("User Updated");
        }
        catch (UserAuthrizationExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        }
        catch (InvalidProductQty e){
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.NO_CONTENT);
        }
        catch (DataNotFoundExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (InvalidInputException e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
            return new ResponseEntity<String>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //get
    @RequestMapping(path = "/v1/product/{productId}", method = RequestMethod.GET)
    public ResponseEntity<?> fetchProductByID(@PathVariable int productId, HttpServletRequest request) {
        try {
            if(productId == 0) {
                throw new InvalidInputException("Enter Valid Product Id");
            }
            productService.isAuthorisedForGet(productId,request.getHeader("Authorization").split(" ")[1]);
            return ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(productId));
        } catch (InvalidInputException e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (UserAuthrizationExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        }
        catch (DataNotFoundExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch(Exception e) {
            return new ResponseEntity<String>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //return ResponseEntity.status(HttpStatus.OK).body(userService.fetchUserbyId(userId));
    }

    //patch
    @RequestMapping(path = "/v1/product/{productId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateProductPatch(@PathVariable int productId , @RequestBody Products products, HttpServletRequest request, Errors error) {
        try {
            if(productId == 0) {
                throw new InvalidInputException("Enter Valid User Id");
            }
            if(products.getId() != 0 || products.getDate_added()!=null || products.getDate_last_updated()!=null)
                throw new InvalidInputException("Id, creation date, last modified date can not be added");
            productService.isAuthorisedForPut(productId,request.getHeader("Authorization").split(" ")[1], products);
            if(error.hasErrors()) {
                String response = error.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(","));
                throw new InvalidInputException(response);
            }
            productService.updateProduct(products,productId);
            return ResponseEntity.status(HttpStatus.CREATED).body("User Updated");
        }
        catch (UserAuthrizationExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        }
        catch (DataNotFoundExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (InvalidInputException e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
            return new ResponseEntity<String>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //delete
    @RequestMapping(path = "/v1/product/{productId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProductByID(@PathVariable int productId, HttpServletRequest request) {
        try {
            if(productId == 0) {
                throw new InvalidInputException("Enter Valid Product Id");
            }
            productService.isAuthorisedForGet(productId,request.getHeader("Authorization").split(" ")[1]);
            productService.deleteProduct(productId);
            return ResponseEntity.status(HttpStatus.OK).body("Product Delete Successfully");
        } catch (InvalidInputException e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (UserAuthrizationExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        }
        catch (DataNotFoundExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch(Exception e) {
            return new ResponseEntity<String>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean validateProductQty(int prod_qty) throws InvalidInputException, InvalidProductQty{
        if(prod_qty == 0){
            throw new InvalidProductQty(" Enter Valid Product Quantity");
        }
        if(prod_qty < 1 || prod_qty > 2147483647){
            throw new InvalidInputException(" Product Quantity should be between 1 to 2147483647");
        }
        return true;
    }
}