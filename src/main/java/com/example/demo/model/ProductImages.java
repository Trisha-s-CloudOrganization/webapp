package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.time.LocalDateTime;

@Entity
@Table
public class ProductImages {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int image_id;
    //@ReadOnlyProperty
    private int product_id;

    //@ReadOnlyProperty
    private String file_name;

    @ReadOnlyProperty
    @CreationTimestamp
    private LocalDateTime date_created;

    //@ReadOnlyProperty
    private String s3_bucket_path;

    public ProductImages(int image_id, int product_id, String file_name, LocalDateTime date_created, String s3_bucket_path) {
        this.image_id = image_id;
        this.product_id = product_id;
        this.file_name = file_name;
        this.date_created = date_created;
        this.s3_bucket_path = s3_bucket_path;
    }

    public ProductImages() {
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public LocalDateTime getDate_created() {
        return date_created;
    }

    public void setDate_created(LocalDateTime date_created) {
        this.date_created = date_created;
    }

    public String getS3_bucket_path() {
        return s3_bucket_path;
    }

    public void setS3_bucket_path(String s3_bucket_path) {
        this.s3_bucket_path = s3_bucket_path;
    }

    @Override
    public String toString() {
        return "productImages{" +
                "image_id=" + image_id +
                ", product_id=" + product_id +
                ", file_name='" + file_name + '\'' +
                ", date_created='" + date_created + '\'' +
                ", s3_bucket_path='" + s3_bucket_path + '\'' +
                '}';
    }
}
