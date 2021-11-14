package com.smarttourism.backend.User;

import com.smarttourism.backend.Location.Location;
import com.smarttourism.backend.Location.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/user")
public class UserController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoOperations mongoOps;

    // ***************User Login**********************

    @RequestMapping(method = RequestMethod.POST, path = "/login")
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

    @RequestMapping(method = RequestMethod.POST, path = "/register")
    public @ResponseBody ResponseEntity<?> userRegister(@RequestBody HashMap<String, String> body) {
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

    @RequestMapping(method = RequestMethod.GET, path = "/verify")
    public @ResponseBody ResponseEntity<?> getLoggedIn(HttpServletResponse response,
            @CookieValue(value = "userId", defaultValue = "null") String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            if (userId.equals("null")) {
                hashMap.put("userAuth", false);
                return new ResponseEntity<>(hashMap, HttpStatus.OK);
            }

            Optional<User> optionalUser = userRepository.findById(userId);
            if(! optionalUser.isPresent()){
                Cookie cookie = new Cookie("userId", null);
                cookie.setMaxAge(0);
                cookie.setSecure(true);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                response.addCookie(cookie);
                hashMap.put("userAuth", false);
                return new ResponseEntity<>(hashMap, HttpStatus.OK);
            }
            User existingUser = optionalUser.get();
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

    @RequestMapping(method = RequestMethod.GET, path = "/logout")
    public @ResponseBody ResponseEntity<?> userLogout(HttpServletResponse response) {
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

    @RequestMapping(method = RequestMethod.GET, path = "/bookmark")
    public @ResponseBody ResponseEntity<?> getUserBookmarkList(
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

    @RequestMapping(method = RequestMethod.POST, path = "/bookmark")
    public @ResponseBody ResponseEntity<?> addBookmarkLocation(@RequestBody HashMap<String, String> body,
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

            mongoOps.findAndModify(userQuery, userUpdate, User.class);
            // System.out.println(!Objects.isNull(userCounter) ? userCounter.getId() : 1);

            hashMap.put("message", "Bookmark Add Success");
            return new ResponseEntity<>(hashMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            hashMap.put("message", "Internal Server Error");
            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ***************Remove User Bookmark**********************

    @RequestMapping(method = RequestMethod.POST, path = "/bookmark/remove")
    public @ResponseBody ResponseEntity<?> removeBookmarkLocation(@RequestBody HashMap<String, String> body,
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

            mongoOps.findAndModify(userQuery, userUpdate, User.class);
            // System.out.println(!Objects.isNull(userCounter) ? userCounter.getId() : 1);

            hashMap.put("message", "Bookmark Removed");
            return new ResponseEntity<>(hashMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println(e);
            hashMap.put("message", "Internal Server Error");
            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
