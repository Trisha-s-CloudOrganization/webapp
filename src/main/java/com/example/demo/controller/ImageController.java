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
import com.timgroup.statsd.StatsDClient;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class ImageController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ImageController.class);
    @Autowired
    ImageService imageService;
    @Autowired
    ProductService productService;
    @Autowired
    private AmazonS3 s3client;
    @Autowired
    private StatsDClient statsDClient;

    //get all images under productid
    @RequestMapping(path = "/v1/product/{productId}/image", method = RequestMethod.GET)
    public ResponseEntity<?> getImagesByProductID(@PathVariable int productId, HttpServletRequest request) {
        statsDClient.incrementCounter("v1.product.productId.image.get");
        try {
            if(productId == 0) {
                throw new InvalidInputException("Enter Valid Product Id");
            }
            productService.isAuthorisedForPut(productId,request.getHeader("Authorization").split(" ")[1], null);
            LOGGER.info("Authentication successful in ImageController:getImagesByProductID");
            return new ResponseEntity<>(imageService.fetchAllImageByProductId(productId), HttpStatus.OK);
        }catch (UserAuthrizationExeception e) {
            LOGGER.warn("UserAuthrizationExeception for productId: "+productId+" in ImageController:getImagesByProductID. Exception: "+e);
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        }catch (DataNotFoundExeception e) {
            LOGGER.warn("No Image/product found with given details and productId: "+productId+" in ImageController:getImagesByProductID. Exception: "+e);
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (InvalidInputException e) {
            LOGGER.warn("Invalid Input Exception for productid: "+productId+" in ImageController:getImagesByProductID. Exception: "+e);
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
            if (e instanceof DataIntegrityViolationException) {
                LOGGER.warn("Invalid required valid data for productId: "+productId+" in ImageController:getImagesByProductID. Exception: "+e);
                return new ResponseEntity<String>( "Enter Valid data. Make sure SKU is unique",HttpStatus.BAD_REQUEST);
            }
            LOGGER.error("Internal server error in ImageController:getImagesByProductID. Exception = {}",e);
            return new ResponseEntity<String>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //post image
    @PostMapping("/v1/product/{productId}/image")
    public ResponseEntity<?> addImage(@PathVariable int productId, @RequestParam(value = "file") MultipartFile file, HttpServletRequest request) throws Exception {
        statsDClient.incrementCounter("v1.product.productId.image.post");
        try {
            if(productId == 0 || file == null || file.equals(null) || file.isEmpty()) {
                throw new InvalidInputException("Enter Valid required data");
            }
            productService.isAuthorisedForPut(productId,request.getHeader("Authorization").split(" ")[1], null);
            LOGGER.info("Authentication successful for productId: "+productId+" in ImageController:addImage");
            return new ResponseEntity<>(imageService.addFile(productId, file), HttpStatus.CREATED);
        }catch (UserAuthrizationExeception e) {
            LOGGER.warn("UserAuthrizationExeception for productId: "+productId+" in ImageController:addImage. Exception: "+e);
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        }catch (DataNotFoundExeception e) {
            LOGGER.warn("No Image/product found with given details for productId: "+productId+" in ImageController:addImage. Exception: "+e);
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (InvalidInputException e) {
            LOGGER.warn("Invalid Input Exception for productId: "+productId+" in ImageController:addImage. Exception: "+e);
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
            if (e instanceof DataIntegrityViolationException) {
                LOGGER.warn("Enter all required valid data for productId: "+productId+" in ImageController:addImage. Exception: "+e);
                return new ResponseEntity<String>( "Enter Valid data. Make sure SKU is unique",HttpStatus.BAD_REQUEST);
            }
            LOGGER.error("Internal server error in ImageController:addImage. Exception = {}",e);
            return new ResponseEntity<String>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //get image by product and image id
    @RequestMapping(path = "/v1/product/{productId}/image/{image_id}", method = RequestMethod.GET)
    public ResponseEntity<?> fetchImageByID(@PathVariable int productId, @PathVariable int image_id, HttpServletRequest request) {
        statsDClient.incrementCounter("v1.product.productId.image.image_id.get");
        try{
            if(productId == 0 || image_id == 0) {
                throw new InvalidInputException("Enter Valid data");
            }
            productService.isAuthorisedForPut(productId,request.getHeader("Authorization").split(" ")[1], null);
            LOGGER.info("Authentication successful for productId: "+productId+" in ImageController:fetchImageByID");
            return new ResponseEntity<>(imageService.fetchImageByID(productId, image_id), HttpStatus.OK);
        }catch (UserAuthrizationExeception e) {
            LOGGER.warn("UserAuthrizationExeception for productId: "+productId+" in ImageController:fetchImageByID. Exception: "+e);
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        }catch (DataNotFoundExeception e) {
            LOGGER.warn("No Image/product found with given details for productId: "+productId+" in ImageController:fetchImageByID. Exception: "+e);
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (InvalidInputException e) {
            LOGGER.warn("Invalid Input Exception for productId: "+productId+" in ImageController:fetchImageByID. Exception: "+e);
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
            if (e instanceof DataIntegrityViolationException) {
                LOGGER.warn("Enter all required valid data for productId: "+productId+" in ImageController:fetchImageByID. Exception: "+e);
                return new ResponseEntity<String>( "Enter Valid data. Make sure SKU is unique",HttpStatus.BAD_REQUEST);
            }
            LOGGER.error("Internal server error in ImageController:fetchImageByID. Exception = {}",e);
            return new ResponseEntity<String>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //delete
    @RequestMapping(path = "/v1/product/{productId}/image/{image_id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteImageByProductImageID(@PathVariable int productId, @PathVariable int image_id, HttpServletRequest request) {
        statsDClient.incrementCounter("v1.product.productId.image.image_id.delete");
        try{
            if(productId == 0) {
                throw new InvalidInputException("Enter Valid Product Id");
            }
            productService.isAuthorisedForPut(productId,request.getHeader("Authorization").split(" ")[1], null);
            LOGGER.info("Authentication successful for productId: "+productId+" in ImageController:deleteImageByProductImageID");
            return new ResponseEntity<>(imageService.deleteFile(image_id), HttpStatus.NO_CONTENT);
        }catch (UserAuthrizationExeception e) {
            LOGGER.warn("UserAuthrizationExeception for productId: "+productId+" in ImageController:deleteImageByProductImageID. Exception: "+e);
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.FORBIDDEN);
        }catch (DataNotFoundExeception e) {
            LOGGER.warn("No Image/product found with given details for productId: "+productId+" in ImageController:deleteImageByProductImageID. Exception: "+e);
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (InvalidInputException e) {
            LOGGER.warn("Invalid Input Exception for productId: "+productId+" in ImageController:deleteImageByProductImageID. Exception: "+e);
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
            if (e instanceof DataIntegrityViolationException) {
                LOGGER.warn("Enter all required valid data for productId: "+productId+" in ImageController:deleteImageByProductImageID. Exception: "+e);
                return new ResponseEntity<String>( "Enter Valid data. Make sure SKU is unique",HttpStatus.BAD_REQUEST);
            }
            LOGGER.error("Internal server error in ImageController:deleteImageByProductImageID. Exception = {}",e);
            return new ResponseEntity<String>(UserConstants.InternalErr,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
