package com.zephyr.edge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zephyr.edge.model.Order;
import com.zephyr.edge.model.Purchase;
import com.zephyr.edge.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

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


    private Purchase purchase1 = new Purchase("T-P-001", 2);
    private Purchase purchase2 = new Purchase("T-P-002", 4);
    private Purchase purchase3 = new Purchase("T-P-003", 6);
    private Purchase purchase4 = new Purchase("T-P-004");

    ArrayList<Purchase> purchaseList1 = new ArrayList<Purchase>() {{
        add(purchase2);
        add(purchase3);
    }};

    private Order order1 = new Order("T-O-001", purchase1);
    private Order order2 = new Order("T-O-002", purchaseList1);
    private Order order3 = new Order("T-O-002", purchase4);
    private Order order4 = new Order("T-O-002");

    private User user1 = new User("Cindy1", "CindyTest", "KnapenTest", "nepemailadres56@thomasmore.be", "test1234567", "HerenthoutseSteenweg", "28", "2270","Herenthout", "0487263554", null );
    private User user2 = new User("Cindy2", "CindyTest2", "KnapenTest2", "nepemailadres562@thomasmore.be", "test1234567", "HerenthoutseSteenweg", "28", "2270","Herenthout", "0487263554", null );
    private User user3 = new  User("Enzo2", "Enzo", "Mazzaro", "nepemailadres2@thomasmore.be", "test12345", null, null, null, null, null, null);
    private User userVoorDelete = new  User("Enzo56", "Enzo", "MazzaroTest2", "nepemailadres256@thomasmore.be", "test12345", "GeelseSteenweg", "40", "2200", "Geel", null, User.Role.admin);

    @BeforeEach
    public void initializeMockserver() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }
}
