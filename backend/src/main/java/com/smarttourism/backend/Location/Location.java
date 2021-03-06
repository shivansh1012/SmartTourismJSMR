package com.smarttourism.backend.Location;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.smarttourism.backend.Review.Review;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "location")
public class Location {

    @Id
    private String id;

    private String name;
    private String imageURL;
    private String description;

    private Integer views;
    
    @DBRef
    private ArrayList<Review> review;
    
    private String address;
    private String state;
    private String country;

    public Location() {
    }

    public Location(String name, String imageURL, String description, String address, String state, String country) {
        this.name = name;
        this.imageURL = imageURL;
        this.description = description;
        this.review = new ArrayList<Review>();
        this.views = 0;
        this.address = address;
        this.state = state;
        this.country = country;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonManagedReference
    public ArrayList<Review> getReview() {
        return review;
    }

    public void setReview(ArrayList<Review> review) {
        this.review = review;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Location [address=" + address + ", country=" + country + ", description=" + description + ", id=" + id
                + ", imageURL=" + imageURL + ", name=" + name + ", review=" + review + ", state=" + state + "]";
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }
    
}
