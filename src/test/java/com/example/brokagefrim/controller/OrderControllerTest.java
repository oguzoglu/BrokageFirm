package com.example.brokagefrim.controller;

import com.example.brokagefrim.AbstractTestContainerConfig;
import com.example.brokagefrim.BrokageFirmApplication;
import com.example.brokagefrim.dto.mapper.OrderMapper;
import com.example.brokagefrim.dto.request.OrderRequestDto;
import com.example.brokagefrim.dto.response.OrderResponseDto;
import com.example.brokagefrim.model.*;
import com.example.brokagefrim.repository.AssetRepository;
import com.example.brokagefrim.repository.CustomerRepository;
import com.example.brokagefrim.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@SpringBootTest(classes = BrokageFirmApplication.class)
@AutoConfigureMockMvc
public class OrderControllerTest extends AbstractTestContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private OrderMapper orderMapper;

    private Asset asset1;

    private Order order1;
    private Order order2;

    private static String token;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @BeforeAll // Use BeforeAll for one-time setup
    static void setUpOnce(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception { // Inject MockMvc and ObjectMapper
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "admin");
        loginRequest.put("password", "admin123");

        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = loginResult.getResponse().getContentAsString();
        var responseMap = objectMapper.readValue(responseContent, Map.class);
        token = (String) responseMap.get("token");
    }
    /// check DataInitializer class for user and customer seeded data
    @BeforeEach
    void setUp() throws Exception {
        orderRepository.deleteAll();
        assetRepository.deleteAll();

        // Create some assets
        asset1 = new Asset();
        asset1.setAssetName("A");
        asset1.setCurrency(Currency.TRY);
        asset1.setSize(1000L);
        asset1.setUsableSize(1000L);
        asset1.setCustomerId(1L);
        assetRepository.save(asset1);

        Asset asset2 = new Asset();
        asset2.setAssetName("B");
        asset2.setCurrency(Currency.TRY);
        asset2.setSize(500L);
        asset2.setUsableSize(500L);
        asset2.setCustomerId(2L);
        assetRepository.save(asset2);

        Asset asset3 = new Asset();
        asset3.setAssetName("B");
        asset3.setCurrency(Currency.USD);
        asset3.setSize(600L);
        asset3.setUsableSize(600L);
        asset3.setCustomerId(3L);
        assetRepository.save(asset3);


        OrderRequestDto orderRequestDto1 = new OrderRequestDto();
        orderRequestDto1.setCustomerId(1L);
        orderRequestDto1.setOrderSide(OrderSide.BUY);
        orderRequestDto1.setAssetName("A");
        orderRequestDto1.setSize(10L);
        orderRequestDto1.setPrice(BigDecimal.valueOf(150));

        String order1Str = mockMvc.perform(MockMvcRequestBuilders.post("/api/orders/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequestDto1))).andReturn().getResponse().getContentAsString();

        order1 = objectMapper.readValue(order1Str, Order.class);


        OrderRequestDto orderRequestDto2 = new OrderRequestDto();
        orderRequestDto2.setCustomerId(2L);
        orderRequestDto2.setOrderSide(OrderSide.BUY);
        orderRequestDto2.setAssetName("A");
        orderRequestDto2.setSize(5L);
        orderRequestDto2.setPrice(BigDecimal.valueOf(2500.00));

        String order2Str = mockMvc.perform(MockMvcRequestBuilders.post("/api/orders/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequestDto2))).andReturn().getResponse().getContentAsString();

        order2 = objectMapper.readValue(order2Str, Order.class);
    }

    @Test
    void createOrder_validInput_returnsCreatedOrder() throws Exception {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setCustomerId(3L);
        orderRequestDto.setOrderSide(OrderSide.BUY);
        orderRequestDto.setAssetName("XYZ");
        orderRequestDto.setSize(20L);
        orderRequestDto.setPrice(BigDecimal.valueOf(300.00));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.customerId").value(3))
                .andExpect(jsonPath("$.orderSide").value("BUY"))
                .andExpect(jsonPath("$.assetName").value("XYZ"))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.price").value(300.00))
                .andExpect(jsonPath("$.status").value("PENDING")); // Default status should be PENDING
    }

    @Test
    void getOrders_byCustomerId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")
                        .param("customerId", "1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1));
    }

    @Test
    void getOrders_noParams() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void listAll_noParams() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/listAll")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[1].customerId").value(2));
    }

    @Test
    void listAll_byDateRange() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String startDate = now.minusDays(1).format(DATE_TIME_FORMATTER);
        String endDate = now.plusDays(1).format(DATE_TIME_FORMATTER);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/listAll")
                        .param("startDate", startDate)
                        .param("endDate", endDate)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[1].customerId").value(2));
    }

    @Test
    void listAll_byStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/listAll")
                        .param("status", "PENDING")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[1].customerId").value(2));
    }

    @Test
    void cancelOrder_SELL_returnsCanceledOrder() throws Exception {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setCustomerId(1L);
        orderRequestDto.setOrderSide(OrderSide.SELL);
        orderRequestDto.setAssetName("A");
        orderRequestDto.setSize(8L);
        orderRequestDto.setPrice(BigDecimal.valueOf(150));

        String orderStr = mockMvc.perform(MockMvcRequestBuilders.post("/api/orders/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequestDto))).andReturn().getResponse().getContentAsString();
        OrderResponseDto orderResponse = objectMapper.readValue(orderStr, OrderResponseDto.class);

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/cancel/"+orderResponse.getCustomerId()+"/"+orderResponse.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString();

        var assetResult = objectMapper.readValue(result, OrderResponseDto.class);
        Assertions.assertEquals(assetResult.getStatus(), OrderStatus.CANCELED);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/assets/"+ orderResponse.getCustomerId()+"/"+orderResponse.getAssetName())
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.usableSize").value(asset1.getUsableSize()))
                .andExpect(jsonPath("$.size").value(asset1.getSize()));
    }

    @Test
    void cancelOrder_BUY_returnsCanceledOrder() throws Exception {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setCustomerId(1L);
        orderRequestDto.setOrderSide(OrderSide.BUY);
        orderRequestDto.setAssetName("A");
        orderRequestDto.setSize(8L);
        orderRequestDto.setPrice(BigDecimal.valueOf(150));

        String orderStr = mockMvc.perform(MockMvcRequestBuilders.post("/api/orders/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequestDto))).andReturn().getResponse().getContentAsString();
        OrderResponseDto orderResponse = objectMapper.readValue(orderStr, OrderResponseDto.class);

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/cancel/"+orderResponse.getCustomerId()+"/"+orderResponse.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        var assetResult = objectMapper.readValue(result, OrderResponseDto.class);
        Assertions.assertEquals(assetResult.getStatus(), OrderStatus.CANCELED);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/assets/"+ orderResponse.getCustomerId()+"/"+orderResponse.getAssetName())
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.usableSize").value(asset1.getUsableSize()))
                .andExpect(jsonPath("$.size").value(asset1.getSize()));
    }


    @Test
    void cancelOrder_orderNotFound_returnsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/cancel/1/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void matchOrder_buy_validInput_returnsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/match/"+ order1.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.MATCHED.toString()))
                .andExpect(jsonPath("$.customerId").value(1));

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/api/assets/"+ order1.getCustomerId()+"/"+order1.getAssetName())
                        .header("Authorization", "Bearer " + token)).andReturn().getResponse().getContentAsString();

        var assetResult = objectMapper.readValue(result, Asset.class);
        Long expectedSize = order1.getSize() + asset1.getSize();
        var actualSize = assetResult.getSize();
        var actualUsableSize = assetResult.getUsableSize();
        Assertions.assertEquals(expectedSize, actualSize);
        Assertions.assertEquals(expectedSize, actualUsableSize);
    }

    @Test
    void matchOrder_sell_validInput_returnsOk() throws Exception {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setCustomerId(1L);
        orderRequestDto.setOrderSide(OrderSide.SELL);
        orderRequestDto.setAssetName("A");
        orderRequestDto.setSize(8L);
        orderRequestDto.setPrice(BigDecimal.valueOf(150));

        String orderStr = mockMvc.perform(MockMvcRequestBuilders.post("/api/orders/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequestDto))).andReturn().getResponse().getContentAsString();
        OrderResponseDto orderResponse = objectMapper.readValue(orderStr, OrderResponseDto.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/match/"+ orderResponse.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.customerId").value(1));

        String orderAfterMatchStr =  mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/"+orderResponse.getCustomerId()+"/"+orderResponse.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();


        OrderResponseDto orderResponseAfterMatch = objectMapper.readValue(orderAfterMatchStr, OrderResponseDto.class);
        Assertions.assertEquals(orderResponseAfterMatch.getStatus(), OrderStatus.MATCHED);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/api/assets/"+ orderResponse.getCustomerId()+"/"+orderResponse.getAssetName())
                .header("Authorization", "Bearer " + token)).andReturn().getResponse().getContentAsString();

        var assetResult = objectMapper.readValue(result, Asset.class);
        Long expectedSize =  asset1.getSize() - orderResponse.getSize();
        var actualSize = assetResult.getSize();
        var actualUsableSize = assetResult.getUsableSize();
        Assertions.assertEquals(expectedSize, actualSize);
        Assertions.assertEquals(expectedSize, actualUsableSize);
    }

    @Test
    void listAll_nonAdminUser_returnsForbidden() throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "Omer");
        loginRequest.put("password", "user123");

        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = loginResult.getResponse().getContentAsString();
        var responseMap = objectMapper.readValue(responseContent, Map.class);
        String myToken = (String) responseMap.get("token");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/listAll")
                        .param("status", "PENDING")
                        .header("Authorization", "Bearer " + myToken))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    void matchOrder_validInput_returnsForbidden() throws Exception {
        // Omer is the Customer with id 1
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "Omer");
        loginRequest.put("password", "user123");

        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = loginResult.getResponse().getContentAsString();
        var responseMap = objectMapper.readValue(responseContent, Map.class);
        String myToken = (String) responseMap.get("token");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/match/"+ order1.getId())
                        .header("Authorization", "Bearer " + myToken))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void getOrders_withCustomerId_returnsForbidden() throws Exception {
        // Omer is the Customer with id 1
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "Omer");
        loginRequest.put("password", "user123");

        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = loginResult.getResponse().getContentAsString();
        var responseMap = objectMapper.readValue(responseContent, Map.class);
        String myToken = (String) responseMap.get("token");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")
                        .param("customerId", "2") // not Omer
                        .header("Authorization", "Bearer " + myToken))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}
