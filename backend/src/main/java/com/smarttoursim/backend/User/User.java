package com.smarttoursim.backend.User;

import java.util.ArrayList;

import com.smarttoursim.backend.Location.Location;
import com.smarttoursim.backend.Review.Review;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document( collection = "user")
public class User {
    @Id
    public String id;

    public String name;
    public String email;
    public String password;

    @DBRef
    public ArrayList<Review> review;
    
    @DBRef
    public ArrayList<Location> bookmarkedLocation;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.review = new ArrayList<>();
        this.bookmarkedLocation = new ArrayList<>();
    }

    
}
