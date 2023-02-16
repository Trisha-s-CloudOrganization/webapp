package com.example.demo.controller;

import com.example.demo.constant.UserConstants;
import com.example.demo.exception.*;
import com.example.demo.model.Products;
import com.example.demo.model.User;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;
    //post
    @PostMapping("/v1/product")
    public ResponseEntity<?> addProduct(@Valid @RequestBody Products products, HttpServletRequest request, Errors error) throws Exception{
        if(error.hasErrors()) {
            String responses = error.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(","));
            throw new InvalidInputException(responses);
        }else {
            try {
                if(products.getId() != 0 || products.getDate_added()!=null || products.getDate_last_updated()!=null)
                    throw new InvalidInputException("Id, creation date, last modified date can not be added");
                validateProductRequest(products);
                User authuser = productService.authCredential(request.getHeader("Authorization").split(" ")[1]);
                products.setOwner_user_id(authuser.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(products));
            }catch (InvalidInputException e) {
                return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
            } catch (UserAuthrizationExeception e) {
                return new ResponseEntity<String>( e.getMessage(),HttpStatus.UNAUTHORIZED);
            }catch(Exception e) {
                if (e instanceof DataIntegrityViolationException) {
                    //handle Internal server error 500
                    return new ResponseEntity<String>( "Enter Valid data. Make sure SKU is unique",HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<String>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    //put
    @RequestMapping(path = "/v1/product/{productId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateProduct(@PathVariable int productId, @Valid @RequestBody Products products, HttpServletRequest request, Errors error) {
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
            validateProductRequest(products);
            productService.updateProduct(products,productId);
            return ResponseEntity.status(HttpStatus.CREATED).body("User Updated");
        }catch (DataNotFoundExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (InvalidInputException e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (UserAuthrizationExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.UNAUTHORIZED);
        }catch (ForbidenAccess e){
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        }catch(Exception e) {
            if (e instanceof DataIntegrityViolationException) {
                //handle Internal server error 500
                return new ResponseEntity<String>( "Enter Valid data. Make sure SKU is unique",HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<String>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //get
    @RequestMapping(path = "/v1/product/{productId}", method = RequestMethod.GET)
    public ResponseEntity<?> fetchProductByID(@PathVariable int productId) {
        try {
            if(productId == 0) {
                throw new InvalidInputException("Enter Valid Product Id");
            }
            //productService.isAuthorisedForGet(productId,request.getHeader("Authorization").split(" ")[1]);
            Products p = productService.getProductById(productId);
            return ResponseEntity.status(HttpStatus.OK).body(p);
        }
        catch (InvalidInputException e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
//        catch (UserAuthrizationExeception e) {
//            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
//        }
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
            if(products.getProduct_qty() !=0) {
                if (products.getProduct_qty() < 1 || products.getProduct_qty() > 100) {
                    throw new InvalidInputException(" Product Quantity should be between 1 to 100");
                }
            }
            productService.isAuthorisedForPut(productId,request.getHeader("Authorization").split(" ")[1], products);
            if(error.hasErrors()) {
                String response = error.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(","));
                throw new InvalidInputException(response);
            }
            Products p = productService.getProductById(productId);
            if((products.getProduct_name()!=null) && !products.getProduct_name().isEmpty()) p.setProduct_name(products.getProduct_name());
            if((products.getDescription()!=null) && !products.getDescription().isEmpty()) p.setDescription(products.getDescription());
            if((products.getSku()!=null) && !products.getSku().isEmpty()) p.setSku(products.getSku());
            if((products.getManufacturer()!=null) && !products.getManufacturer().isEmpty()) p.setManufacturer(products.getManufacturer());
            if(products.getProduct_qty() !=0) p.setProduct_qty(products.getProduct_qty());
            productService.updateProduct(p,productId);
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
            if (e instanceof DataIntegrityViolationException) {
                //handle Internal server error 500
                return new ResponseEntity<String>( "Enter Valid data. Make sure SKU is unique",HttpStatus.BAD_REQUEST);
            }
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

    private void validateProductRequest(Products products) throws InvalidInputException, InvalidProductQty{
        if(products.getProduct_name()==null || products.getDescription()==null || products.getSku()==null || products.getManufacturer()==null || products.getProduct_qty() == 0){
            throw new InvalidInputException("Please enter Product name, description, sku, manufacturer, quantity");
        }
        if(products.getProduct_name().isEmpty() || products.getDescription().isEmpty() || products.getSku().isEmpty() || products.getManufacturer().isEmpty() || products.getProduct_qty() == 0){
            throw new InvalidInputException("Please enter Product name, description, sku, manufacturer, quantity");
        }
        if(products.getProduct_qty() < 1 || products.getProduct_qty() > 100){
            throw new InvalidInputException(" Product Quantity should be between 1 to 100");
        }
    }
}