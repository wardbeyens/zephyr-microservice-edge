package com.zephyr.edge.controller;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.zephyr.edge.model.Clothes;
import com.zephyr.edge.model.Order;
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


    @GetMapping("/user/search")
    public User getUserByUserNameOrByUUID(@RequestParam(required = false) String userName, @RequestParam(required = false) String uuid) {

        if (userName != null) {
            return restTemplate.getForObject(userServiceBaseUrl + "/users/{userName}",
                    User.class, userName);
        } else {
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

    @GetMapping("/clothes")
    public List<Clothes> getClothes() {

        ResponseEntity<List<Clothes>> responseEntity =
                restTemplate.exchange(clothesServiceBaseUrl + "/clothes",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Clothes>>() {
                        });

        return responseEntity.getBody();
    }

    @GetMapping("/clothes/search")
    public Clothes getClothesByNameOrByUUID(@RequestParam(required = false) String name, @RequestParam(required = false) String uuid) {

        if (name != null) {
            return restTemplate.getForObject(clothesServiceBaseUrl + "/clothes/name/" + name,
                    Clothes.class, name);
        } else {
            return restTemplate.getForObject(clothesServiceBaseUrl + "/clothes/uuid/" + uuid,
                    Clothes.class, uuid);
        }
    }


    @PostMapping("/clothes")
    public Clothes addClothes(@RequestBody Clothes clothes) {

        return restTemplate.postForObject(clothesServiceBaseUrl + "/clothes",
                clothes, Clothes.class);
    }

    @PutMapping("/clothes/{uuid}")
    public Clothes updateUser(@PathVariable String uuid, @RequestBody Clothes clothes) {

        ResponseEntity<Clothes> responseEntity =
                restTemplate.exchange(clothesServiceBaseUrl + "/clothes/" + uuid,
                        HttpMethod.PUT, new HttpEntity<>(clothes), Clothes.class);

        return responseEntity.getBody();
    }

    @DeleteMapping("/clothes/{uuid}")
    public ResponseEntity<String> deleteClothesByUUID(@PathVariable String uuid) {

        try {
            restTemplate.delete(clothesServiceBaseUrl + "/clothes/" + uuid);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/orders")
    public List<Order> getOrders() {

        ResponseEntity<List<Order>> responseEntity =
                restTemplate.exchange(orderServiceBaseUrl + "/orders",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Order>>() {
                        });

        return responseEntity.getBody();
    }


    @GetMapping("/orders/search")
    public List<Order> getOrdersByUUIDorBUserUUID(@RequestParam(required = false) String uuid, @RequestParam(required = false) String userID) {

        if (userID != null) {
            ResponseEntity<List<Order>> responseEntity = restTemplate.exchange(orderServiceBaseUrl + "/orders/user/" + userID, HttpMethod.GET
                    , null, new ParameterizedTypeReference<List<Order>>() {
                    }, userID);

            return responseEntity.getBody();
        } else {
            List<Order> listOrders = new ArrayList<>();
            Order getOrder = restTemplate.getForObject(orderServiceBaseUrl + "/order/" + uuid,
                    Order.class, uuid);

            listOrders.add(getOrder);
            return listOrders;
        }


    }

    @PostMapping("/order")
    public Order addOrder(@RequestBody Order order) {

        return restTemplate.postForObject(orderServiceBaseUrl + "/order",
                order, Order.class);
    }

    @PutMapping("/order/{uuid}")
    public Order updateUser(@PathVariable String uuid, @RequestBody Order updatedOrder) {

        ResponseEntity<Order> responseEntity =
                restTemplate.exchange(orderServiceBaseUrl + "/order/" + uuid,
                        HttpMethod.PUT, new HttpEntity<>(updatedOrder), Order.class);

        return responseEntity.getBody();
    }

    @PostMapping("/order/{uuid}/paid")
    public Order updateOrderPaid(@PathVariable String uuid) {

        return restTemplate.postForObject(orderServiceBaseUrl + "/order/" + uuid + "/paid",
                null, Order.class);
    }

    @DeleteMapping("/order/{uuid}")
    public ResponseEntity<String> deleteOrderByUUID(@PathVariable String uuid) {

        try {
            restTemplate.delete(orderServiceBaseUrl + "/order/" + uuid);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
