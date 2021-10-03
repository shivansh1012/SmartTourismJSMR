package com.smarttourism.backend.Review;

import com.smarttourism.backend.Location.Location;
import com.smarttourism.backend.Location.LocationRepository;
import com.smarttourism.backend.User.User;
import com.smarttourism.backend.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/review")
public class ReviewController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoOperations mongoOps;

    // ***************Review Requests**********************

    @RequestMapping(method = RequestMethod.GET, path = "")
    public @ResponseBody
    ResponseEntity<?> getReview(@RequestParam String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            hashMap.put("reviewDetail", reviewRepository.findById(id));
            return new ResponseEntity<>(hashMap, HttpStatus.OK);
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "")
    public @ResponseBody ResponseEntity<?> newReview(@RequestBody HashMap<String, String> body,
                                                     @CookieValue(value = "userId", defaultValue = "null") String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            if (userId.equals("null")) {
                hashMap.put("message", "Please Login First");
                return new ResponseEntity<>(hashMap, HttpStatus.UNAUTHORIZED);
            }

            Optional<User> optionalUser = userRepository.findById(userId);
            Optional<Location> optionalLocation = locationRepository.findById(body.get("locationId"));

            if (optionalUser.isEmpty()) {
                hashMap.put("message", "Invalid User");
                return new ResponseEntity<>(hashMap, HttpStatus.CONFLICT);
            }
            if (optionalLocation.isEmpty()) {
                hashMap.put("message", "Invalid Location");
                return new ResponseEntity<>(hashMap, HttpStatus.CONFLICT);
            }

            User user = optionalUser.get();
            Location location = optionalLocation.get();

            Review savedReview = reviewRepository
                    .save(new Review(user, location, body.get("reviewText"), body.get("rating")));

            Query userQuery = new Query();
            Criteria userCriteria = Criteria.where("id").is(user.getId());
            userQuery.addCriteria(userCriteria);
            Update userUpdate = new Update();
            userUpdate.push("review", savedReview);

            Query locationQuery = new Query();
            Criteria locationCriteria = Criteria.where("id").is(location.getId());
            locationQuery.addCriteria(locationCriteria);
            Update locationUpdate = new Update();
            locationUpdate.push("review", savedReview);

            User userCounter = mongoOps.findAndModify(userQuery, userUpdate, User.class);
            Location locationCounter = mongoOps.findAndModify(locationQuery, locationUpdate, Location.class);

            // System.out.println(!Objects.isNull(userCounter) ? userCounter.getId() : 1);
            // System.out.println(!Objects.isNull(locationCounter) ?
            // locationCounter.getId(): 1);

            hashMap.put("message", "Review Addition Success");
            return new ResponseEntity<>(hashMap, HttpStatus.CREATED);
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
