package com.smarttourism.backend.Location;

import com.smarttourism.backend.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping(path = "api/location")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoOperations mongoOps;

    // ***************Location Requests**********************

    @RequestMapping(method = RequestMethod.GET, path = "", produces = { "application/JSON" })
    public @ResponseBody
    ResponseEntity<?> getLocation(@RequestParam(defaultValue = "all") String id,
                                  @CookieValue(value = "userId", defaultValue = "null") String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (id.equals("all"))
            hashMap.put("locationList", locationRepository.findAll());
        else {
            hashMap.put("locationList", locationRepository.findById(id));

            Query locationQuery = new Query();
            Criteria locationCriteria = Criteria.where("id").is(id); // Update location WHERE id=inputId set views=views+1
            locationQuery.addCriteria(locationCriteria);
            Update locationUpdate = new Update();
            locationUpdate.inc("views", 1);

            mongoOps.findAndModify(locationQuery, locationUpdate, Location.class);

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
        }
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "")
    public @ResponseBody ResponseEntity<?> newLocation(@RequestBody HashMap<String, String> body) {
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
}
