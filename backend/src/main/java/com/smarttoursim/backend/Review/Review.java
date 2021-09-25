package com.smarttoursim.backend.Review;

import com.smarttoursim.backend.Location.Location;
import com.smarttoursim.backend.User.User;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "review")
public class Review {
    @Id
    public String id;

    @DBRef
    public User user;
    
    @DBRef
    public Location location;
    
    public String review;
    public String rating;

    public Review() {
    }

    public Review(User user, Location location, String review, String rating) {
        this.user = user;
        this.location = location;
        this.review = review;
        this.rating = rating;
    }

    
}
