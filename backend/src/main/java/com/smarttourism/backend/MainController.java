package com.smarttoursim.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
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

    // body.forEach((k,v) -> System.out.println("Key = "+ k + ", Value = " + v));
    // ***************Location Requests**********************

    @RequestMapping(method = RequestMethod.GET, path = "/location", produces = {"application/JSON"})
    public @ResponseBody
    ResponseEntity<?> getLocation(@RequestParam(defaultValue = "all") String id,
                                  @CookieValue(value = "userId", defaultValue = "null") String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();

        if (id.equals("all"))
            hashMap.put("locationList", locationRepository.findAll());
        else {
            if (userId.equals("null")) {
                hashMap.put("isBookmarked", false);
            } else {
                ArrayList<Location> bookmarkList = userRepository.findById(userId).get().getBookmarkedLocation();
                for (Location location : bookmarkList) {
                    if (location.getId().equals(id)) {
                        hashMap.put("isBookmarked", true);
                        break;
                    }
                }
                hashMap.putIfAbsent("isBookmarked", false);
            }
            hashMap.put("locationList", locationRepository.findById(id));
        }
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/location")
    public @ResponseBody
    ResponseEntity<?> newLocation(@RequestBody HashMap<String, String> body) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            locationRepository.save(new Location(body.get("name"), body.get("imageURL"), body.get("description"),
                    body.get("address"), body.get("state"), body.get("country")));
            hashMap.put("message", "Place Addition Success");
            return new ResponseEntity<>(hashMap, HttpStatus.CREATED);
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ***************User Login**********************

    @RequestMapping(method = RequestMethod.POST, path = "/user")
    public @ResponseBody
    ResponseEntity<?> userLogin(@RequestBody HashMap<String, String> body,
                                HttpServletResponse response) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            User existingUser = userRepository.findByemail(body.get("email"));
            if (existingUser == null || !existingUser.getPassword().equals(body.get("password"))) {
                hashMap.put("message", "Incorrect Email or Password");
                return new ResponseEntity<>(hashMap, HttpStatus.UNAUTHORIZED);
            }
            Cookie cookie = new Cookie("userId", existingUser.getId());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            hashMap.put("message", "Login Success");
            return new ResponseEntity<>(hashMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ***************User Register**********************

    @RequestMapping(method = RequestMethod.POST, path = "/user/register")
    public @ResponseBody
    ResponseEntity<?> userRegister(@RequestBody HashMap<String, String> body) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            userRepository.save(new User(body.get("name"), body.get("email"), body.get("password")));
            hashMap.put("message", "User Creation Success");
            return new ResponseEntity<>(hashMap, HttpStatus.CREATED);
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ***************User Auth**********************

    @RequestMapping(method = RequestMethod.GET, path = "/user/getloggedin")
    public @ResponseBody
    ResponseEntity<?> getLoggedIn(HttpServletRequest request,
                                  @CookieValue(value = "userId", defaultValue = "null") String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            if (userId.equals("null")) {
                hashMap.put("userAuth", false);
                return new ResponseEntity<>(hashMap, HttpStatus.OK);
            }
            User existingUser = userRepository.findById(userId).get();
            hashMap.put("userAuth", true);
            hashMap.put("name", existingUser.name);
            hashMap.put("email", existingUser.email);
            return new ResponseEntity<>(hashMap, HttpStatus.OK);
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ***************User Logout**********************

    @RequestMapping(method = RequestMethod.GET, path = "/user/logout")
    public @ResponseBody
    ResponseEntity<?> userLogout(HttpServletResponse response) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            Cookie cookie = new Cookie("userId", null);
            cookie.setMaxAge(0);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return new ResponseEntity<>(hashMap, HttpStatus.OK);
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ***************All User Bookmarks**********************

    @RequestMapping(method = RequestMethod.GET, path = "/user")
    public @ResponseBody
    ResponseEntity<?> getUserBookmarkList(
            @CookieValue(value = "userId", defaultValue = "null") String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            User existingUser = userRepository.findById(userId).get();

            hashMap.put("bookmarkList", existingUser.getBookmarkedLocation());
            return new ResponseEntity<>(hashMap, HttpStatus.OK);
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ***************Add User Bookmark**********************

    @RequestMapping(method = RequestMethod.POST, path = "/user/bookmark")
    public @ResponseBody
    ResponseEntity<?> addBookmarkLocation(@RequestBody HashMap<String, String> body,
                                          @CookieValue(value = "userId", defaultValue = "null") String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            if (userId.equals("null")) {
                hashMap.put("message", "Please Login First");
                return new ResponseEntity<>(hashMap, HttpStatus.UNAUTHORIZED);
            }
            User existingUser = userRepository.findById(userId).get();
            Location existingLocation = locationRepository.findById(body.get("locationId")).get();
            Query userQuery = new Query();
            Criteria userCriteria = Criteria.where("id").is(existingUser.getId());
            userQuery.addCriteria(userCriteria);
            Update userUpdate = new Update();
            userUpdate.push("bookmarkedLocation", existingLocation);

            User userCounter = mongoOps.findAndModify(userQuery, userUpdate, User.class);
            // System.out.println(!Objects.isNull(userCounter) ? userCounter.getId() : 1);

            hashMap.put("message", "Bookmark Add Success");
            return new ResponseEntity<>(hashMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ***************Remove User Bookmark**********************

    @RequestMapping(method = RequestMethod.POST, path = "/user/bookmark/remove")
    public @ResponseBody
    ResponseEntity<?> removeBookmarkLocation(@RequestBody HashMap<String, String> body,
                                             @CookieValue(value = "userId", defaultValue = "null") String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            if (userId.equals("null")) {
                hashMap.put("message", "Please Login First");
                return new ResponseEntity<>(hashMap, HttpStatus.UNAUTHORIZED);
            }

            Query userQuery = new Query();
            Criteria userCriteria = Criteria.where("id").is(userId);
            userQuery.addCriteria(userCriteria);
            Update userUpdate = new Update();
            userUpdate.pull("bookmarkedLocation", body.get(("locationId")));

            User userCounter = mongoOps.findAndModify(userQuery, userUpdate, User.class);
//            System.out.println(!Objects.isNull(userCounter) ? userCounter.getId() : 1);

            hashMap.put("message", "Bookmark Removed");
            return new ResponseEntity<>(hashMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println(e);
            hashMap.put("message", "Internal Server Error");
            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ***************Review Requests**********************

    @RequestMapping(method = RequestMethod.GET, path = "/review")
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

    @RequestMapping(method = RequestMethod.POST, path = "/review")
    public @ResponseBody
    ResponseEntity<?> newReview(@RequestBody HashMap<String, String> body,
                                @CookieValue(value = "userId", defaultValue = "null") String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            if (userId.equals("null")) {
                hashMap.put("message", "Please Login First");
                return new ResponseEntity<>(hashMap, HttpStatus.UNAUTHORIZED);
            }

            Optional<User> optionalUser = userRepository.findById(userId);
            Optional<Location> optionalLocation = locationRepository.findById(body.get("locationid"));

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
                    .save(new Review(user, location, body.get("reviewtext"), body.get("rating")));

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
            // System.out.println(!Objects.isNull(locationCounter) ? locationCounter.getId(): 1);

            hashMap.put("message", "Review Addition Success");
            return new ResponseEntity<>(hashMap, HttpStatus.CREATED);
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
