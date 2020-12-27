package com.zephyr.edge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zephyr.edge.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class EdgeContollerUnitTests {

    @Value("${protocol}://${userservice.host}:${userservice.port}")
    private String userServiceBaseUrl;

    @Value("${protocol}://${clothesservice.host}:${clothesservice.port}")
    private String clothesServiceBaseUrl;

    @Value("${protocol}://${orderservice.host}:${orderservice.port}")
    private String orderServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    private String userUuid1 = "user0001-1234-1234-1234-ijklmnopqrst";
    private String userUuid2 = "user0002-1234-1234-1234-ijklmnopqrst";
    private String userUuid3 = "user0003-1234-1234-1234-ijklmnopqrst";

    private User user1 = new User(userUuid1, "Cindy1", "CindyTest", "KnapenTest", "nepemailadres56@thomasmore.be", "test1234567", "HerenthoutseSteenweg", "28", "2270", "Herenthout", "0487263554",null);
    private User user2 = new User(userUuid2, "Cindy2", "CindyTest2", "KnapenTest2", "nepemailadres562@thomasmore.be", "test1234567", "HerenthoutseSteenweg", "28", "2270", "Herenthout", "0487263554", User.Role.admin);
    private User user3 = new User(userUuid3, "Enzo2", "Enzo", "Mazzaro", "nepemailadres2@thomasmore.be", "test12345", null, null, null, null, null, null);
    private User user3b = new User("Enzo2", "EnzoTest", "Mazzaro", "nepemailadres2@thomasmore.be", "test12345", "HerenthoutseSteenweg", "28", "2270", "Herenthout", "0487263554", null);
    private User user4 = new User("Enzo56", "Enzo", "MazzaroTest2", "nepemailadres256@thomasmore.be", "test12345", "GeelseSteenweg", "40", "2200", "Geel", null, User.Role.admin);

    private List<User> allUsers = Arrays.asList(user1, user2, user3, user4);

    private String clothesUuid1 = "clothes1-1234-1234-1234-ijklmnopqrst";
    private String clothesUuid2 = "clothes2-1234-1234-1234-ijklmnopqrst";
    private String clothesUuid3 = "clothes3-1234-1234-1234-ijklmnopqrst";
    private String clothesUuid4 = "clothes4-1234-1234-1234-ijklmnopqrst";

    private Clothes clothes1 = new Clothes(clothesUuid1, "Pijamama", "Fuchsia", "XXL", "Unisex", "RLX", 99.99, "Avondkledij");
    private Clothes clothes2 = new Clothes(clothesUuid2, "Shirtie", "White", "XL", "M", "Bateau", 29.99, "Shirt");
    private Clothes clothes3 = new Clothes(clothesUuid3, "RegenJas", "Black", "L", "F", "RJS", 39.99, "Jacket");
    private Clothes clothes3b = new Clothes(clothesUuid3, "RegenJas", "Black", "L", "F", "RJS", 59.99, "Jacket");
    private Clothes clothes4 = new Clothes(clothesUuid4, "Frakske", "Orange", "M", "F", "LMFAO", 29.00, "Jacket");
    private Clothes clothes5 = new Clothes("Broek", "Blue", "S", "F", "off-white", 129.99, "Jeans");

    private List<Clothes> allClothes = Arrays.asList(clothes1, clothes2, clothes3, clothes4);

    private Purchase purchase1 = new Purchase(clothesUuid1);
    private Purchase purchase2 = new Purchase(clothesUuid2, 2);
    private Purchase purchase3 = new Purchase(clothesUuid3, 3);
    private Purchase purchase4 = new Purchase(clothesUuid4, 44);

    private ArrayList<Purchase> purchaseList0 = new ArrayList<Purchase>() {{
        add(purchase1);
        add(purchase2);
    }};


    private ArrayList<Purchase> purchaseList1 = new ArrayList<Purchase>() {{
        add(purchase2);
        add(purchase3);
    }};

    private String orderUuid1 = "order001-1234-1234-1234-ijklmnopqrst";
    private String orderUuid2 = "order002-1234-1234-1234-ijklmnopqrst";
    private String orderUuid3 = "order003-1234-1234-1234-ijklmnopqrst";
    private String orderUuid4 = "order004-1234-1234-1234-ijklmnopqrst";

    private Order order1 = new Order(userUuid1, purchase1);
    private Order order1b = new Order(userUuid1, purchaseList0);
    private Order order2 = new Order(userUuid2, purchaseList1);
    private Order order3 = new Order(userUuid2, purchase4);
    private Order order4 = new Order(userUuid3);

    private List<Order> orderList = Arrays.asList(order1, order2, order3, order4);

    private List<Order> orderListSmall = Collections.singletonList(order1);

    @BeforeEach
    public void initializeMockserver() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    //users
    @Test
    public void whenGetAllUsers_thenReturnJsonUsers() throws Exception {
        // GET all users
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(userServiceBaseUrl + "/users")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allUsers))
                );

        mockMvc.perform(get("/users"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].userName", is("Cindy1")))
                .andExpect(jsonPath("$[0].firstName", is("CindyTest")))
                .andExpect(jsonPath("$[0].lastName", is("KnapenTest")))
                .andExpect(jsonPath("$[0].email", is("nepemailadres56@thomasmore.be")))
                .andExpect(jsonPath("$[0].password", is("test1234567")))
                .andExpect(jsonPath("$[0].streetName", is("HerenthoutseSteenweg")))
                .andExpect(jsonPath("$[0].number", is("28")))
                .andExpect(jsonPath("$[0].postalCode", is("2270")))
                .andExpect(jsonPath("$[0].city", is("Herenthout")))
                .andExpect(jsonPath("$[0].phoneNumber", is("0487263554")))
                .andExpect(jsonPath("$[0].role", is("normalUser")));
    }

    @Test
    public void givenUser_whenGetUserByUserName_thenReturnJsonReview() throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(userServiceBaseUrl + "/users/" + user1.getUserName())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(user1))
                );

        mockMvc.perform(get("/user/search").param("userName", user1.getUserName()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", is("Cindy1")))
                .andExpect(jsonPath("$.firstName", is("CindyTest")))
                .andExpect(jsonPath("$.lastName", is("KnapenTest")))
                .andExpect(jsonPath("$.email", is("nepemailadres56@thomasmore.be")))
                .andExpect(jsonPath("$.password", is("test1234567")))
                .andExpect(jsonPath("$.streetName", is("HerenthoutseSteenweg")))
                .andExpect(jsonPath("$.number", is("28")))
                .andExpect(jsonPath("$.postalCode", is("2270")))
                .andExpect(jsonPath("$.city", is("Herenthout")))
                .andExpect(jsonPath("$.phoneNumber", is("0487263554")))
                .andExpect(jsonPath("$.role", is("normalUser")));
    }

    @Test
    public void givenUser_whenGetUserByUUID_thenReturnJsonReview() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(userServiceBaseUrl + "/users/uuid/" + userUuid1)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(user1))
                );

        mockMvc.perform(get("/user/search").param("uuid", userUuid1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", is("Cindy1")))
                .andExpect(jsonPath("$.firstName", is("CindyTest")))
                .andExpect(jsonPath("$.lastName", is("KnapenTest")))
                .andExpect(jsonPath("$.email", is("nepemailadres56@thomasmore.be")))
                .andExpect(jsonPath("$.password", is("test1234567")))
                .andExpect(jsonPath("$.streetName", is("HerenthoutseSteenweg")))
                .andExpect(jsonPath("$.number", is("28")))
                .andExpect(jsonPath("$.postalCode", is("2270")))
                .andExpect(jsonPath("$.city", is("Herenthout")))
                .andExpect(jsonPath("$.phoneNumber", is("0487263554")))
                .andExpect(jsonPath("$.role", is("normalUser")));
    }

    @Test
    public void givenUser_whenPostUser_thenReturnJsonReview() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(userServiceBaseUrl + "/users/create")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(user2))
                );

        mockMvc.perform(post("/user")
                .content(mapper.writeValueAsString(user2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", is("Cindy2")))
                .andExpect(jsonPath("$.firstName", is("CindyTest2")))
                .andExpect(jsonPath("$.lastName", is("KnapenTest2")))
                .andExpect(jsonPath("$.email", is("nepemailadres562@thomasmore.be")))
                .andExpect(jsonPath("$.password", is("test1234567")))
                .andExpect(jsonPath("$.streetName", is("HerenthoutseSteenweg")))
                .andExpect(jsonPath("$.number", is("28")))
                .andExpect(jsonPath("$.postalCode", is("2270")))
                .andExpect(jsonPath("$.city", is("Herenthout")))
                .andExpect(jsonPath("$.phoneNumber", is("0487263554")))
                .andExpect(jsonPath("$.role", is("admin")));
    }

    @Test
    public void givenUser_whenPutUser_thenReturnJsonReview() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(userServiceBaseUrl + "/users/" + user3.getUserName())))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(user3b))
                );

        mockMvc.perform(put("/user/{userName}", user3.getUserName())
                .content(mapper.writeValueAsString(user3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", is("Enzo2")))
                .andExpect(jsonPath("$.firstName", is("EnzoTest")))
                .andExpect(jsonPath("$.lastName", is("Mazzaro")))
                .andExpect(jsonPath("$.email", is("nepemailadres2@thomasmore.be")))
                .andExpect(jsonPath("$.password", is("test12345")))
                .andExpect(jsonPath("$.streetName", is("HerenthoutseSteenweg")))
                .andExpect(jsonPath("$.number", is("28")))
                .andExpect(jsonPath("$.postalCode", is("2270")))
                .andExpect(jsonPath("$.city", is("Herenthout")))
                .andExpect(jsonPath("$.phoneNumber", is("0487263554")))
                .andExpect(jsonPath("$.role", is("normalUser")));
    }

    @Test
    public void givenUser_whenDeleteUser_thenStatusOk() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(userServiceBaseUrl + "/users/delete/" + user4.getUserName())))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                );

        mockMvc.perform(delete("/user/{userName}", user4.getUserName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void givenNoUser_whenDeleteUser_thenStatusNotFound() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(userServiceBaseUrl + "/users/delete/" + "niet-vindbaar")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST)
                );

        mockMvc.perform(delete("/user/{userName}", "niet-vindbaar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    //clothes
    @Test
    public void givenClothes_whenGetAllClothes_thenReturnJsonClothes() throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(clothesServiceBaseUrl + "/clothes")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allClothes))
                );

        mockMvc.perform(get("/clothes"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[1].name", is("Shirtie")))
                .andExpect(jsonPath("$[1].color", is("White")))
                .andExpect(jsonPath("$[1].size", is("XL")))
                .andExpect(jsonPath("$[1].gender", is("M")))
                .andExpect(jsonPath("$[1].brand", is("Bateau")))
                .andExpect(jsonPath("$[1].price", is(29.99)))
                .andExpect(jsonPath("$[1].type", is("Shirt")));
    }

    @Test
    public void givenClothes_whenGetClothesByName_thenReturnJsonReviews() throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(clothesServiceBaseUrl + "/clothes/name/" + clothes2.getName())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(clothes2))
                );

        mockMvc.perform(get("/clothes/search").param("name", clothes2.getName()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Shirtie")))
                .andExpect(jsonPath("$.color", is("White")))
                .andExpect(jsonPath("$.size", is("XL")))
                .andExpect(jsonPath("$.gender", is("M")))
                .andExpect(jsonPath("$.brand", is("Bateau")))
                .andExpect(jsonPath("$.price", is(29.99)))
                .andExpect(jsonPath("$.type", is("Shirt")));
    }

    @Test
    public void givenClothes_whenGetClothesByUUID_thenReturnJsonReview() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(clothesServiceBaseUrl + "/clothes/uuid/" + clothes2.getUuid())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(clothes2))
                );

        mockMvc.perform(get("/clothes/search").param("uuid", clothes2.getUuid()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Shirtie")))
                .andExpect(jsonPath("$.color", is("White")))
                .andExpect(jsonPath("$.size", is("XL")))
                .andExpect(jsonPath("$.gender", is("M")))
                .andExpect(jsonPath("$.brand", is("Bateau")))
                .andExpect(jsonPath("$.price", is(29.99)))
                .andExpect(jsonPath("$.type", is("Shirt")));
    }

    @Test
    public void whenPostClothes_thenReturnJsonReview() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(clothesServiceBaseUrl + "/clothes")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(clothes5))
                );

        mockMvc.perform(post("/clothes")
                .content(mapper.writeValueAsString(clothes5))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Broek")))
                .andExpect(jsonPath("$.color", is("Blue")))
                .andExpect(jsonPath("$.size", is("S")))
                .andExpect(jsonPath("$.gender", is("F")))
                .andExpect(jsonPath("$.brand", is("off-white")))
                .andExpect(jsonPath("$.price", is(129.99)))
                .andExpect(jsonPath("$.type", is("Jeans")));
    }

    @Test
    public void givenReview_whenPutReview_thenReturnJsonReview() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(clothesServiceBaseUrl + "/clothes/" + clothes3.getUuid())))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(clothes3b))
                );

        mockMvc.perform(put("/clothes/{uuid}", clothes3.getUuid())
                .content(mapper.writeValueAsString(clothes3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("RegenJas")))
                .andExpect(jsonPath("$.color", is("Black")))
                .andExpect(jsonPath("$.size", is("L")))
                .andExpect(jsonPath("$.gender", is("F")))
                .andExpect(jsonPath("$.brand", is("RJS")))
                .andExpect(jsonPath("$.price", is(59.99)))
                .andExpect(jsonPath("$.type", is("Jacket")));

    }


    @Test
    public void givenClothes_whenDeleteClothes_thenStatusOK() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(clothesServiceBaseUrl + "/clothes/" + clothes1.getUuid())))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                );

        mockMvc.perform(delete("/clothes/{uuid}", clothes1.getUuid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoClothes_whenDeleteClothes_thenStatusNotFound() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(clothesServiceBaseUrl + "/clothes/" + "niet-vindbaar")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST)
                );

        mockMvc.perform(delete("/clothes/{uuid}", "niet-vindbaar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void givenAllOrders_thenReturnJsonOrders() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(orderServiceBaseUrl + "/orders")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(orderListSmall))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(userServiceBaseUrl + "/users/uuid/" + orderListSmall.get(0).getUserID())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(user1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(clothesServiceBaseUrl + "/clothes/uuid/" + orderListSmall.get(0).getPurchaseList().get(0).getClothes())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(clothes1))
                );


        mockMvc.perform(get("/orders"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user.userName", is("Cindy1")))
                .andExpect(jsonPath("$[0].user.firstName", is("CindyTest")))
                .andExpect(jsonPath("$[0].user.lastName", is("KnapenTest")))
                .andExpect(jsonPath("$[0].user.email", is("nepemailadres56@thomasmore.be")))
                .andExpect(jsonPath("$[0].user.streetName", is("HerenthoutseSteenweg")))
                .andExpect(jsonPath("$[0].user.number", is("28")))
                .andExpect(jsonPath("$[0].user.postalCode", is("2270")))
                .andExpect(jsonPath("$[0].user.city", is("Herenthout")))
                .andExpect(jsonPath("$[0].user.phoneNumber", is("0487263554")))
                .andExpect(jsonPath("$[0].user.role", is("normalUser")))
                .andExpect(jsonPath("$[0].paid", is(false)))
                .andExpect(jsonPath("$[0].purchaseList[0].clothes.name", is("Pijamama")))
                .andExpect(jsonPath("$[0].purchaseList[0].clothes.color", is("Fuchsia")))
                .andExpect(jsonPath("$[0].purchaseList[0].amount", is(1)));
    }

    @Test
    public void givenOrder_whenGetOrderByUUID_thenReturnJsonOrder() throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(orderServiceBaseUrl + "/order/" + orderUuid1)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(order1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(userServiceBaseUrl + "/users/uuid/" + order1.getUserID())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(user1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(clothesServiceBaseUrl + "/clothes/uuid/" + order1.getPurchaseList().get(0).getClothes())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(clothes1))
                );


        mockMvc.perform(get("/orders/search")
                .param("uuid", orderUuid1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user.userName", is("Cindy1")))
                .andExpect(jsonPath("$[0].user.firstName", is("CindyTest")))
                .andExpect(jsonPath("$[0].user.lastName", is("KnapenTest")))
                .andExpect(jsonPath("$[0].user.email", is("nepemailadres56@thomasmore.be")))
                .andExpect(jsonPath("$[0].user.streetName", is("HerenthoutseSteenweg")))
                .andExpect(jsonPath("$[0].user.number", is("28")))
                .andExpect(jsonPath("$[0].user.postalCode", is("2270")))
                .andExpect(jsonPath("$[0].user.city", is("Herenthout")))
                .andExpect(jsonPath("$[0].user.phoneNumber", is("0487263554")))
                .andExpect(jsonPath("$[0].user.role", is("normalUser")))
                .andExpect(jsonPath("$[0].paid", is(false)))
                .andExpect(jsonPath("$[0].purchaseList[0].clothes.name", is("Pijamama")))
                .andExpect(jsonPath("$[0].purchaseList[0].clothes.color", is("Fuchsia")))
                .andExpect(jsonPath("$[0].purchaseList[0].amount", is(1)));
    }


    @Test
    public void givenOrders_whenGetOrdersByUserID_thenReturnJsonOrders() throws Exception {

        // GET order by uuid
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(orderServiceBaseUrl + "/orders/user/" + userUuid1)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(orderListSmall))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(userServiceBaseUrl + "/users/uuid/" + orderListSmall.get(0).getUserID())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(user1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(clothesServiceBaseUrl + "/clothes/uuid/" + orderListSmall.get(0).getPurchaseList().get(0).getClothes())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(clothes1))
                );


        mockMvc.perform(get("/orders/search")
                .param("userID", userUuid1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user.userName", is("Cindy1")))
                .andExpect(jsonPath("$[0].user.firstName", is("CindyTest")))
                .andExpect(jsonPath("$[0].user.lastName", is("KnapenTest")))
                .andExpect(jsonPath("$[0].user.email", is("nepemailadres56@thomasmore.be")))
                .andExpect(jsonPath("$[0].user.streetName", is("HerenthoutseSteenweg")))
                .andExpect(jsonPath("$[0].user.number", is("28")))
                .andExpect(jsonPath("$[0].user.postalCode", is("2270")))
                .andExpect(jsonPath("$[0].user.city", is("Herenthout")))
                .andExpect(jsonPath("$[0].user.phoneNumber", is("0487263554")))
                .andExpect(jsonPath("$[0].user.role", is("normalUser")))
                .andExpect(jsonPath("$[0].paid", is(false)))
                .andExpect(jsonPath("$[0].purchaseList[0].clothes.name", is("Pijamama")))
                .andExpect(jsonPath("$[0].purchaseList[0].clothes.color", is("Fuchsia")))
                .andExpect(jsonPath("$[0].purchaseList[0].amount", is(1)));
    }

    @Test
    public void whenPostOrder_thenReturnJsonOrder() throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(orderServiceBaseUrl + "/order")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(order1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(userServiceBaseUrl + "/users/uuid/" + order1.getUserID())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(user1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(clothesServiceBaseUrl + "/clothes/uuid/" + order1.getPurchaseList().get(0).getClothes())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(clothes1))
                );

        mockMvc.perform(post("/order")
                .content(mapper.writeValueAsString(order1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.userName", is("Cindy1")))
                .andExpect(jsonPath("$.user.firstName", is("CindyTest")))
                .andExpect(jsonPath("$.user.lastName", is("KnapenTest")))
                .andExpect(jsonPath("$.user.email", is("nepemailadres56@thomasmore.be")))
                .andExpect(jsonPath("$.user.streetName", is("HerenthoutseSteenweg")))
                .andExpect(jsonPath("$.user.number", is("28")))
                .andExpect(jsonPath("$.user.postalCode", is("2270")))
                .andExpect(jsonPath("$.user.city", is("Herenthout")))
                .andExpect(jsonPath("$.user.phoneNumber", is("0487263554")))
                .andExpect(jsonPath("$.user.role", is("normalUser")))
                .andExpect(jsonPath("$.paid", is(false)))
                .andExpect(jsonPath("$.purchaseList[0].clothes.name", is("Pijamama")))
                .andExpect(jsonPath("$.purchaseList[0].clothes.color", is("Fuchsia")))
                .andExpect(jsonPath("$.purchaseList[0].amount", is(1)));
    }

    @Test
    public void givenOrder_whenPutOrder_thenReturnJsonOrder() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(orderServiceBaseUrl + "/order/" + orderUuid1)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(order1b))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(userServiceBaseUrl + "/users/uuid/" + order1b.getUserID())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(user1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(clothesServiceBaseUrl + "/clothes/uuid/" + order1b.getPurchaseList().get(0).getClothes())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(clothes1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(clothesServiceBaseUrl + "/clothes/uuid/" + order1b.getPurchaseList().get(1).getClothes())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(clothes2))
                );


        mockMvc.perform(put("/order/{uuid}", orderUuid1)
                .content(mapper.writeValueAsString(order1b))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.userName", is("Cindy1")))
                .andExpect(jsonPath("$.user.firstName", is("CindyTest")))
                .andExpect(jsonPath("$.user.lastName", is("KnapenTest")))
                .andExpect(jsonPath("$.user.email", is("nepemailadres56@thomasmore.be")))
                .andExpect(jsonPath("$.user.streetName", is("HerenthoutseSteenweg")))
                .andExpect(jsonPath("$.user.number", is("28")))
                .andExpect(jsonPath("$.user.postalCode", is("2270")))
                .andExpect(jsonPath("$.user.city", is("Herenthout")))
                .andExpect(jsonPath("$.user.phoneNumber", is("0487263554")))
                .andExpect(jsonPath("$.user.role", is("normalUser")))
                .andExpect(jsonPath("$.paid", is(false)))
                .andExpect(jsonPath("$.purchaseList[0].clothes.name", is("Pijamama")))
                .andExpect(jsonPath("$.purchaseList[0].clothes.color", is("Fuchsia")))
                .andExpect(jsonPath("$.purchaseList[0].amount", is(1)))
                .andExpect(jsonPath("$.purchaseList[1].clothes.name", is("Shirtie")))
                .andExpect(jsonPath("$.purchaseList[1].clothes.color", is("White")))
                .andExpect(jsonPath("$.purchaseList[1].amount", is(2)));
    }

    @Test
    public void givenOrder_whenPostOrderPaid_thenReturnJsonOrder() throws Exception {
        Order order1paid = new Order(userUuid1, purchase1);
        order1paid.setPaid(true);

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(orderServiceBaseUrl + "/order/" + orderUuid1 + "/paid")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(order1paid))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(userServiceBaseUrl + "/users/uuid/" + order1paid.getUserID())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(user1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(clothesServiceBaseUrl + "/clothes/uuid/" + order1paid.getPurchaseList().get(0).getClothes())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(clothes1))
                );

        mockMvc.perform(post("/order/{uuid}/paid", orderUuid1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.userName", is("Cindy1")))
                .andExpect(jsonPath("$.user.firstName", is("CindyTest")))
                .andExpect(jsonPath("$.user.lastName", is("KnapenTest")))
                .andExpect(jsonPath("$.user.email", is("nepemailadres56@thomasmore.be")))
                .andExpect(jsonPath("$.user.streetName", is("HerenthoutseSteenweg")))
                .andExpect(jsonPath("$.user.number", is("28")))
                .andExpect(jsonPath("$.user.postalCode", is("2270")))
                .andExpect(jsonPath("$.user.city", is("Herenthout")))
                .andExpect(jsonPath("$.user.phoneNumber", is("0487263554")))
                .andExpect(jsonPath("$.user.role", is("normalUser")))
                .andExpect(jsonPath("$.paid", is(true)))
                .andExpect(jsonPath("$.purchaseList[0].clothes.name", is("Pijamama")))
                .andExpect(jsonPath("$.purchaseList[0].clothes.color", is("Fuchsia")))
                .andExpect(jsonPath("$.purchaseList[0].amount", is(1)));
    }

    @Test
    public void givenOrder_whenDeleteOrder_thenStatusOK() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(orderServiceBaseUrl + "/order/" + orderUuid3)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                );

        mockMvc.perform(delete("/order/{uuid}", orderUuid3)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void givenNoOrder_whenDeleteOrder_thenStatusNotFound() throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(orderServiceBaseUrl + "/order/" + "bestaat-niet")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST)
                );

        mockMvc.perform(delete("/order/{uuid}", "bestaat-niet")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }


    @Test
    public void deleteAllCurrentOrdersAndCreateOrdersWithRealUsersAndClothes_thenStatusOK() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(userServiceBaseUrl + "/users")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allUsers))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(clothesServiceBaseUrl + "/clothes")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allClothes))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(orderServiceBaseUrl + "/orders/delete/all")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(orderServiceBaseUrl + "/order")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(order1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(orderServiceBaseUrl + "/order")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(order2))
                );


        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(orderServiceBaseUrl + "/order")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(order3))
                );

        mockMvc.perform(delete("/deleteAllCurrentOrdersAndCreateOrdersWithRealUsersAndClothes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}