package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
@Entity
@Table
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    //@JdbcTypeCode(SqlTypes.CHAR)
    private int id;

    private int owner_user_id;
    private String product_name;
    private String description;
    @Column(unique = true)
    private String sku;
    private String manufacturer;

    @NotBlank(message = "enter valid as product_qty")
    @Min(value = 0, message = "Product quality should be more than 0")
    @Max(value = 100, message = "Product Quantity is too big") // MAX_INT
    @Positive(message = "Enter valid product quantity")
    @Digits(message="Number should contain 10 digits.",fraction=0, integer=10)
    private int product_qty; // if product_qty not in range then 500 Internal Server Error

    @CreationTimestamp
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ReadOnlyProperty
    private LocalDateTime date_added;
    @UpdateTimestamp
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ReadOnlyProperty
    private LocalDateTime date_last_updated;

    public Products(int id, int owner_user_id, String product_name, String description, String sku, String manufacturer, int product_qty, LocalDateTime date_added, LocalDateTime date_last_updated) {
        this.id = id;
        this.owner_user_id = owner_user_id;
        this.product_name = product_name;
        this.description = description;
        this.sku = sku;
        this.manufacturer = manufacturer;
        this.product_qty = product_qty;
        this.date_added = date_added;
        this.date_last_updated = date_last_updated;
    }

    public Products() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner_user_id() {
        return owner_user_id;
    }

    public void setOwner_user_id(int owner_user_id) {
        this.owner_user_id = owner_user_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(int product_qty) {
        this.product_qty = product_qty;
    }

    public LocalDateTime getDate_added() {
        return date_added;
    }

    public void setDate_added(LocalDateTime date_added) {
        this.date_added = date_added;
    }

    public LocalDateTime getDate_last_updated() {
        return date_last_updated;
    }

    public void setDate_last_updated(LocalDateTime date_last_updated) {
        this.date_last_updated = date_last_updated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", owner_user_id=" + owner_user_id +
                ", product_name='" + product_name + '\'' +
                ", desc='" + description + '\'' +
                ", sku='" + sku + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", product_qty='" + product_qty + '\'' +
                ", date_added=" + date_added +
                ", date_last_updated=" + date_last_updated +
                '}';
    }
}
