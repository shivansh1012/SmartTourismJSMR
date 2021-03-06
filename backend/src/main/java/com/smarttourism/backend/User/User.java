package com.smarttourism.backend.User;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.smarttourism.backend.Location.Location;
import com.smarttourism.backend.Review.Review;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document( collection = "user")
public class User {
    @Id
    private String id;

    private String name;
    private String email;
    private String password;

    @DBRef
    private ArrayList<Review> review;
    
    @DBRef
    private ArrayList<Location> bookmarkedLocation;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.review = new ArrayList<>();
        this.bookmarkedLocation = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @JsonBackReference
    public ArrayList<Review> getReview() {
        return review;
    }

    public void setReview(ArrayList<Review> review) {
        this.review = review;
    }

    @JsonBackReference
    public ArrayList<Location> getBookmarkedLocation() {
        return bookmarkedLocation;
    }

    public void setBookmarkedLocation(ArrayList<Location> bookmarkedLocation) {
        this.bookmarkedLocation = bookmarkedLocation;
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", id=" + id + ", name=" + name + ", password=" + password + "]";
    }
    
}
