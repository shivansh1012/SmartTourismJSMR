package com.smarttoursim.backend.Location;

import java.util.ArrayList;

import com.smarttoursim.backend.Review.Review;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "location")
public class Location {

    @Id
    public String id;

    public String name;
    public String imageURL;
    public String description;
    
    @DBRef
    public ArrayList<Review> reviews;
    
    public String address;
    public String state;
    public String country;

    public Location() {
    }

    public Location(String name, String imageURL, String description, String address, String state, String country) {
        this.name = name;
        this.imageURL = imageURL;
        this.description = description;
        this.reviews = new ArrayList<Review>();
        this.address = address;
        this.state = state;
        this.country = country;
    }
}
