package com.example.demo.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.constant.UserConstants;
import com.example.demo.exception.DataNotFoundExeception;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.UserAuthrizationExeception;
import com.example.demo.model.Products;
import com.example.demo.service.ImageService;
import com.example.demo.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class ImageController {

    @Autowired
    ImageService imageService;
    @Autowired
    ProductService productService;
    @Autowired
    private AmazonS3 s3client;

    //get all images under productid
    @RequestMapping(path = "/v1/product/{productId}/image", method = RequestMethod.GET)
    public ResponseEntity<?> getImagesByProductID(@PathVariable int productId, HttpServletRequest request) {
        try {
            if(productId == 0) {
                throw new InvalidInputException("Enter Valid Product Id");
            }
            productService.isAuthorisedForPut(productId,request.getHeader("Authorization").split(" ")[1], null);
            return new ResponseEntity<>(imageService.fetchAllImageByProductId(productId), HttpStatus.OK);
        }catch (UserAuthrizationExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        }catch (DataNotFoundExeception e) {
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

    //post image
    @PostMapping("/v1/product/{productId}/image")
    public ResponseEntity<?> addImage(@PathVariable int productId, @RequestParam(value = "file") MultipartFile file, HttpServletRequest request) throws Exception {
        try {
            if(productId == 0 || file == null || file.equals(null) || file.isEmpty()) {
                throw new InvalidInputException("Enter Valid required data");
            }
            productService.isAuthorisedForPut(productId,request.getHeader("Authorization").split(" ")[1], null);
            return new ResponseEntity<>(imageService.addFile(productId, file), HttpStatus.CREATED);
        }catch (UserAuthrizationExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        }catch (DataNotFoundExeception e) {
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

    //get image by product and image id
    @RequestMapping(path = "/v1/product/{productId}/image/{image_id}", method = RequestMethod.GET)
    public ResponseEntity<?> fetchImageByID(@PathVariable int productId, @PathVariable int image_id, HttpServletRequest request) {
        try{
            if(productId == 0 || image_id == 0) {
                throw new InvalidInputException("Enter Valid data");
            }
            productService.isAuthorisedForPut(productId,request.getHeader("Authorization").split(" ")[1], null);
            return new ResponseEntity<>(imageService.fetchImageByID(productId, image_id), HttpStatus.OK);
        }catch (UserAuthrizationExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        }catch (DataNotFoundExeception e) {
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
    @RequestMapping(path = "/v1/product/{productId}/image/{image_id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteImageByProductImageID(@PathVariable int productId, @PathVariable int image_id, HttpServletRequest request) {
        try{
            if(productId == 0) {
                throw new InvalidInputException("Enter Valid Product Id");
            }
            productService.isAuthorisedForPut(productId,request.getHeader("Authorization").split(" ")[1], null);
            return new ResponseEntity<>(imageService.deleteFile(image_id), HttpStatus.NO_CONTENT);
        }catch (UserAuthrizationExeception e) {
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        }catch (DataNotFoundExeception e) {
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
}
