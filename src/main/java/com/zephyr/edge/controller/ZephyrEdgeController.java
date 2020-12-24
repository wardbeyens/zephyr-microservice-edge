package com.zephyr.edge.controller;

import com.zephyr.edge.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ZephyrEdgeController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${protocol}://${userservice.host}:${userservice.port}")
    private String userServiceBaseUrl;

    @Value("${protocol}://${clothesservice.host}:${clothesservice.port}")
    private String clothesServiceBaseUrl;

    @Value("${protocol}://${orderservice.host}:${orderservice.port}")
    private String orderServiceBaseUrl;


    @GetMapping("/users")
    public List<User> getUsers() {

        ResponseEntity<List<User>> responseEntity =
                restTemplate.exchange(userServiceBaseUrl + "/users",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
                        });

        return responseEntity.getBody();
    }


    @GetMapping("/user")
    public User getUserByUserNameOrByUUID(@RequestParam(required = false) String userName, @RequestParam(required = false) String uuid) {

        if(userName != null){
            return restTemplate.getForObject(userServiceBaseUrl + "/users/{userName}",
                    User.class, userName);
        }else{
            return restTemplate.getForObject(userServiceBaseUrl + "/users/uuid/{uuid}",
                    User.class, uuid);
        }
    }


    @PostMapping("/user")
    public User addUser(@RequestBody User user) {

        return restTemplate.postForObject(userServiceBaseUrl + "/users/create",
                user, User.class);
    }

    @PutMapping("/user/{userName}")
    public User updateUser(@PathVariable String userName, @RequestBody User user) {

        ResponseEntity<User> responseEntity =
                restTemplate.exchange(userServiceBaseUrl + "/users/" + userName,
                        HttpMethod.PUT, new HttpEntity<>(user), User.class);

        return responseEntity.getBody();
    }

    @DeleteMapping("/user/{userName}")
    public ResponseEntity<String> deleteUserByUserName(@PathVariable String userName) {

        try {
            restTemplate.delete(userServiceBaseUrl + "/users/delete/" + userName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }


}
