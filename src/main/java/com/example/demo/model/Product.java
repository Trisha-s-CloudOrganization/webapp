package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class Product {

    @Id
    @GeneratedValue()
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JdbcTypeCode(SqlTypes.CHAR)
    private int id;
    @NotBlank(message = "Invalid User Id")
    private int userId;

    @NotBlank(message = "enter valid as product_name")
    private String product_name;

    @NotBlank(message = "enter valid as product_qty")
    private String product_qty;

    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime product_created;
    @UpdateTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime product_updated;

    public Product(int id, int userId, String product_name, String product_qty, LocalDateTime product_created, LocalDateTime product_updated) {
        this.id = id;
        this.userId = userId;
        this.product_name = product_name;
        this.product_qty = product_qty;
        this.product_created = product_created;
        this.product_updated = product_updated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public LocalDateTime getProduct_created() {
        return product_created;
    }

    public void setProduct_created(LocalDateTime product_created) {
        this.product_created = product_created;
    }

    public LocalDateTime getProduct_updated() {
        return product_updated;
    }

    public void setProduct_updated(LocalDateTime product_updated) {
        this.product_updated = product_updated;
    }
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", userId=" + userId +
                ", product_name='" + product_name + '\'' +
                ", product_qty='" + product_qty + '\'' +
                ", product_created=" + product_created +
                ", product_updated=" + product_updated +
                '}';
    }
}
