package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Date;
//import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Entity
@Table
public class User {
    @Id
    @GeneratedValue()
    @Column(nullable = false)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    //@JdbcTypeCode(SqlTypes.CHAR)
    @ReadOnlyProperty
    private int id;
    @NotBlank(message = "enter valid as first_name")
    private String first_name;
    @NotBlank(message = "enter valid as last_name")
    private String last_name;
    @NotBlank(message = "enter valid as username")
    @Email
    private String username;

//    @JsonIgnore
//    @JsonProperty("password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "enter password")
    private String password;
    @CreationTimestamp
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ReadOnlyProperty
    private LocalDateTime account_created;
    @UpdateTimestamp
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ReadOnlyProperty
    private LocalDateTime account_updated;

    public User() {
    }

    public User(int id, String fname, String lname, String username, String password, LocalDateTime creation_time, LocalDateTime update_time) {
        //super();
        this.id = id;
        this.first_name = fname;
        this.last_name = lname;
        this.username = username;
        this.password = password;
        this.account_created = creation_time;
        this.account_updated = update_time;
    }

    public User(String fname, String lname, String username, String password) {
        //super();
        this.first_name = fname;
        this.last_name = lname;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String fname) {
        this.first_name = fname;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String lname) {
        this.last_name = lname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String email) {
        this.username = email;
    }

//    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getAccount_created() {
        return account_created;
    }

    public void setAccount_created(LocalDateTime creation_time) {
        this.account_created = creation_time;
    }

    public LocalDateTime getAccount_updated() {
        return account_updated;
    }

    public void setAccount_updated(LocalDateTime update_time) {
        this.account_updated = update_time;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", account_created=" + account_created +
                ", account_updated=" + account_updated +
                '}';
    }
//        @Override
//    public String toString() {
//        return "User{" +
//
//                ", first_name='" + first_name + '\'' +
//                ", last_name='" + last_name + '\'' +
//                ", username='" + username + '\'' +
//                ", password='" + password + '\'' +
//                '}';
//    }

}
