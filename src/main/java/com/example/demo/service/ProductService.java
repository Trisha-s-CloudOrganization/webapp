package com.example.demo.service;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.exception.DataNotFoundExeception;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.UserAuthrizationExeception;
import com.example.demo.exception.UserNameNotFoundException;
import com.example.demo.model.Products;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserService userService;

    public Products saveProduct(Products products){
        return productRepository.save(products);
    }

    public int updateProduct(Products products, int productId){
        return productRepository.update(products.getProduct_name(), products.getDescription(), products.getSku(), products.getManufacturer(), products.getProduct_qty(), productId);
    }

    public void deleteProduct(int productId){
        productRepository.delete(getProductById(productId));
    }

    public Products getProductById(int id){
        return productRepository.getReferenceById(id);
    }

    public boolean isAuthorisedForGet(int productId, String tokenEnc) throws DataNotFoundExeception, UserAuthrizationExeception {

        int userId = getUserDetailsAuth(productId).getOwner_user_id();
        Optional<User> user = userService.fetchUserbyId(userId);
        byte[] token = Base64.getDecoder().decode(tokenEnc);
        String decodedStr = new String(token, StandardCharsets.UTF_8);
        String userName = decodedStr.split(":")[0];
        String passWord = decodedStr.split(":")[1];
        System.out.println("Value of Token" + " "+ decodedStr);
        if(!(user.get().getUsername().equals(userName)) || !(PassEncoder().matches(passWord,user.get().getPassword()))){
            throw new UserAuthrizationExeception("Forbidden to access");
        }
        return true;
    }
    public boolean isAuthorisedForPut(int productId,String tokenEnc, Products productsRequest) throws DataNotFoundExeception, UserAuthrizationExeception, InvalidInputException {
        int userId = getUserDetailsAuth(productId).getOwner_user_id();
        Optional<User> user = userService.fetchUserbyId(userId);
        byte[] token = Base64.getDecoder().decode(tokenEnc);
        String decodedStr = new String(token, StandardCharsets.UTF_8);
        String userName = decodedStr.split(":")[0];
        String passWord = decodedStr.split(":")[1];
        System.out.println("Value of Token" + " "+ decodedStr);
        if(!(user.get().getUsername().equals(userName)) || !(PassEncoder().matches(passWord,user.get().getPassword()))){
            throw new UserAuthrizationExeception("Forbidden to access");
        }
        return true;
    }
    public Products getUserDetailsAuth(int productId) throws DataNotFoundExeception{
        Optional<Products> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return product.get();
        }
        throw new DataNotFoundExeception("Product Not Found");
    }

    public User authCredential(String tokenEnc)  throws UsernameNotFoundException, UserAuthrizationExeception {
        byte[] token = Base64.getDecoder().decode(tokenEnc);
        String decodedStr = new String(token, StandardCharsets.UTF_8);
        String userName = decodedStr.split(":")[0];
        String passWord = decodedStr.split(":")[1];
        User user = userService.getUserByUsername(userName);
        if(!(PassEncoder().matches(passWord,user.getPassword()))){
            throw new UserAuthrizationExeception("Enter valid password");
        }
        return user;
    }

    public BCryptPasswordEncoder PassEncoder() {
        return new BCryptPasswordEncoder();
    }
}
