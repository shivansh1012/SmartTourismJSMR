package com.smarttoursim.backend;

import java.util.HashMap;
import java.util.Optional;

import com.mongodb.client.result.UpdateResult;
import com.smarttoursim.backend.Location.Location;
import com.smarttoursim.backend.Location.LocationRepository;
import com.smarttoursim.backend.Review.ReviewRepository;
import com.smarttoursim.backend.User.User;
import com.smarttoursim.backend.User.UserRepository;
import com.smarttoursim.backend.Review.Review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api")
public class MainController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoOperations mongoOps;

    @RequestMapping(method = RequestMethod.GET, path = "/location")
    public @ResponseBody HashMap<String, Object> getLocation(@RequestParam(defaultValue = "") String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (id.equals(""))
            hashMap.put("locationList", locationRepository.findAll());
        else
            hashMap.put("locationList", locationRepository.findById(id));
        return hashMap;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/location")
    public @ResponseBody HashMap<String, Object> newLocation(@RequestBody HashMap<String, String> body) {
        // System.out.println("TEST");
        // body.forEach((k,v) -> System.out.println("Key = "+ k + ", Value = " + v));
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            locationRepository.save(new Location(body.get("name"), body.get("imageURL"), body.get("description"),
                    body.get("address"), body.get("state"), body.get("country")));
            hashMap.put("message", "Place Addition Success");
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
        }
        return hashMap;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/user")
    public @ResponseBody HashMap<String, Object> userLogin(@RequestBody HashMap<String, String> body) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            User existingUser = userRepository.findByemail(body.get("email"));
            if (existingUser == null || !existingUser.password.equals(body.get("password"))) {
                hashMap.put("message", "Incorrect Email or Password");
                return hashMap;
            }
            hashMap.put("message", "Login Success");
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
        }
        return hashMap;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/user/register")
    public @ResponseBody HashMap<String, Object> userRegister(@RequestBody HashMap<String, String> body) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            userRepository.save(new User(body.get("name"), body.get("email"), body.get("password")));
            hashMap.put("message", "User Creation Success");
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
        }
        return hashMap;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/review")
    public @ResponseBody HashMap<String, Object> getReview(@RequestParam String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            hashMap.put("reviewDetail", reviewRepository.findById(id));
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
        }
        return hashMap;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/review")
    public @ResponseBody HashMap<String, Object> newReview(@RequestBody HashMap<String, String> body) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            Optional<User> optionalUser = userRepository.findById(body.get("userid"));
            Optional<Location> optionalLocation = locationRepository.findById(body.get("locationid"));
            
            if (!optionalUser.isPresent()) {
                hashMap.put("message", "Invalid User");
                return hashMap;
            }
            if (!optionalLocation.isPresent()) {
                hashMap.put("message", "Invalid Location");
                return hashMap;
            }
            User user = optionalUser.get();
            Location location = optionalLocation.get();
            
            //add the same in (location and user) review list

            // Query sQuery = new Query();
            // Criteria sCriteria = Criteria.where("id").is(user);
            // sQuery.addCriteria(sCriteria);

            // Update sUpdate = new Update();
            // sUpdate.set("id", user);

            // UpdateResult sUpdateRequest = mongoOps.updateFirst(sQuery, sUpdate, User.class);
            // System.out.println(sUpdateRequest);

            reviewRepository.save(new Review(user, location, body.get("reviewtext"), body.get("rating")));
            hashMap.put("message", "Review Addition Success");
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
            hashMap.put("error", e);
        }
        return hashMap;
    }

}
