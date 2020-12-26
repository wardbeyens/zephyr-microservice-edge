package com.zephyr.edge.controller;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.zephyr.edge.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @PostConstruct
    public void createUsableOrders() {
        List<User> userList = getUsers();
        User initUser1 = userList.get(0);
        User initUser2 = userList.get(1);

//        System.out.println("First User: " + initUser1.getUserName());
//        System.out.println("Second User: " + initUser2.getUserName());

        List<Clothes> clothesList = getClothes();
        Clothes initClothes1 = clothesList.get(0);
        Clothes initClothes2 = clothesList.get(1);

//        System.out.println("First Clothes: " + initClothes1.getName());
//        System.out.println("Second Clothes: " + initClothes2.getName());

        Purchase initPurchase1 = new Purchase(initClothes1.getUuid());
        Purchase initPurchase2 = new Purchase(initClothes2.getUuid(), 2);
        Purchase initPurchase3 = new Purchase(initClothes1.getUuid(), 3);

        ArrayList<Purchase> initPurchaseList1 = new ArrayList<>();
        initPurchaseList1.add(initPurchase2);
        initPurchaseList1.add(initPurchase3);

        restTemplate.delete(orderServiceBaseUrl + "/orders/delete/all");

        ResponseEntity<List<Order>> responseEntityListOrder =
                restTemplate.exchange(orderServiceBaseUrl + "/orders",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Order>>() {
                        });

//        System.out.println("Deleted all Orders");

        Order initCreatedOrder1 = restTemplate.postForObject(orderServiceBaseUrl + "/order",
                new Order(initUser1.getUuid(), initPurchase1), Order.class);

        Order initCreatedOrder2 = restTemplate.postForObject(orderServiceBaseUrl + "/order",
                new Order(initUser2.getUuid(), initPurchaseList1), Order.class);

        Order initCreatedOrder3 = restTemplate.postForObject(orderServiceBaseUrl + "/order",
                new Order(initUser1.getUuid()), Order.class);

//        System.out.println("Created Orders with real Users and Clothes");
    }

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
    public List<FilledOrder> getOrders() {
        ResponseEntity<List<Order>> responseEntity =
                restTemplate.exchange(orderServiceBaseUrl + "/orders",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Order>>() {
                        });
        List<FilledOrder> filledOrderList = new ArrayList<>();
        if (responseEntity.getBody() != null) {
            filledOrderList = getFilledOrderListInternalByOrderList(responseEntity.getBody());
        }
        return filledOrderList;
    }

    @GetMapping("/orders/search")
    public List<FilledOrder> getOrdersByUUIDorBUserUUID(@RequestParam(required = false) String uuid, @RequestParam(required = false) String userID) {
        List<Order> listOrders = new ArrayList<>();
        if (userID != null) {
            ResponseEntity<List<Order>> responseEntity = restTemplate.exchange(orderServiceBaseUrl + "/orders/user/" + userID, HttpMethod.GET
                    , null, new ParameterizedTypeReference<List<Order>>() {
                    }, userID);

            listOrders = responseEntity.getBody();
        } else {
            Order getOrder = restTemplate.getForObject(orderServiceBaseUrl + "/order/" + uuid,
                    Order.class, uuid);

            listOrders.add(getOrder);
        }

        List<FilledOrder> filledOrderList = new ArrayList<>();
        if (listOrders != null) {
            filledOrderList = getFilledOrderListInternalByOrderList(listOrders);
        }
        return filledOrderList;
    }

    @PostMapping("/order")
    public FilledOrder addOrder(@RequestBody Order order) {


        Order notFilledOrder = restTemplate.postForObject(orderServiceBaseUrl + "/order",
                order, Order.class);

        FilledOrder filledOrder = new FilledOrder();
        if (notFilledOrder != null) {
            filledOrder = getFilledOrderInternalByOrder(notFilledOrder);
        }
        return filledOrder;
    }

    @PutMapping("/order/{uuid}")
    public FilledOrder updateUser(@PathVariable String uuid, @RequestBody Order updatedOrder) {

        ResponseEntity<Order> responseEntity =
                restTemplate.exchange(orderServiceBaseUrl + "/order/" + uuid,
                        HttpMethod.PUT, new HttpEntity<>(updatedOrder), Order.class);

        Order notFilledOrder = responseEntity.getBody();

        FilledOrder filledOrder = new FilledOrder();
        if (notFilledOrder != null) {
            filledOrder = getFilledOrderInternalByOrder(notFilledOrder);
        }
        return filledOrder;
    }

    @PostMapping("/order/{uuid}/paid")
    public FilledOrder updateOrderPaid(@PathVariable String uuid) {

        Order notFilledOrder = restTemplate.postForObject(orderServiceBaseUrl + "/order/" + uuid + "/paid",
                null, Order.class);

        FilledOrder filledOrder = new FilledOrder();
        if (notFilledOrder != null) {
            filledOrder = getFilledOrderInternalByOrder(notFilledOrder);
        }
        return filledOrder;
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

    public User getUserInternalByUserUUId(String uuid) {
        User user = restTemplate.getForObject(userServiceBaseUrl + "/users/uuid/{uuid}",
                User.class, uuid);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    public Clothes getClothesInternalByClothesUUId(String uuid) {
        return restTemplate.getForObject(clothesServiceBaseUrl + "/clothes/uuid/" + uuid,
                Clothes.class, uuid);
    }

    public ArrayList<FilledPurchase> getFilledPurchaseListInternalByPurchaseList(List<Purchase> purchaseList) {
        ArrayList<FilledPurchase> filledPurchaseList = new ArrayList<>();
        for (Purchase p : purchaseList
        ) {
            String uuid = p.getClothes();
            Clothes clothes = getClothesInternalByClothesUUId(uuid);
            FilledPurchase filledPurchase = new FilledPurchase(clothes, p.getAmount());
            filledPurchaseList.add(filledPurchase);
        }
        return filledPurchaseList;
    }

    public FilledOrder getFilledOrderInternalByOrder(Order order) {
        User user = getUserInternalByUserUUId(order.getUserID());
        ArrayList<FilledPurchase> purchaseList = getFilledPurchaseListInternalByPurchaseList(order.getPurchaseList());
        return new FilledOrder(order.getUuid(), user, purchaseList, order.isPaid());
    }

    public List<FilledOrder> getFilledOrderListInternalByOrderList(List<Order> orderList) {

        List<FilledOrder> filledOrderList = new ArrayList<>();

        for (Order o : orderList
        ) {
            FilledOrder filledOrder = getFilledOrderInternalByOrder(o);
            filledOrderList.add(filledOrder);
        }
        return filledOrderList;
    }


}
