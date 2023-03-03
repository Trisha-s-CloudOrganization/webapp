package com.example.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.exception.DataNotFoundExeception;
import com.example.demo.model.ProductImages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private AmazonS3 s3client;
    @Autowired
    private ImageRepository imageRepository;
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    //get all images under productid
    public List<ProductImages> fetchAllImageByProductId(int productId) {
        List<ProductImages> productImagesByProductId = imageRepository.findProductImagesByProduct_id(productId);
        return productImagesByProductId;
    }

    //post image
    public ProductImages addFile(int productId, MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        s3client.getUrl(bucketName,fileName);
        ProductImages p = new ProductImages();
        p.setProduct_id(productId);
        p.setFile_name(fileName);
        p.setS3_bucket_path(s3client.getUrl(bucketName,fileName).toString());
        ProductImages save = imageRepository.save(p);
        fileObj.delete();
        return save;
    }

    //get image by prod and image id
    public List<ProductImages> fetchImageByID(int productId, int image_id ) {
        List<ProductImages> l = new ArrayList<>();
        List<ProductImages> productImagesByProductId = imageRepository.findProductImagesByProduct_id(productId);
        for(ProductImages p:productImagesByProductId) {
            if (p.getImage_id() == image_id) {
                l.add(p);
            }
        }
        return l;
    }

    //delete
    public String deleteFile(int image_id) throws DataNotFoundExeception {
        try {
            String fileName = imageRepository.findById(image_id).get().getFile_name();
            s3client.deleteObject(bucketName, fileName);
            imageRepository.deleteById(image_id);
            return fileName + " removed ...";
        }catch (Exception e){
            System.out.println(e);
            throw new DataNotFoundExeception("File Not Found");
        }
    }

    public String deleteImagesbyProdId(int productId) {
        try {
            List<ProductImages> iList = fetchAllImageByProductId(productId);
            for(ProductImages i : iList) {
                String fileName = imageRepository.findById(i.getImage_id()).get().getFile_name();
                s3client.deleteObject(bucketName, fileName);
                imageRepository.deleteById(i.getImage_id());
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            System.out.println("ImageService i/o exception in convertMultiPartFileToFile");
        }
        return convertedFile;
    }
}
