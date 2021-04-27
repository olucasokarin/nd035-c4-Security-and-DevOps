package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void submitOrder() {
        User user = createUserAndCart();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());

        Assert.assertNotNull(response);
        UserOrder userOrder = response.getBody();
        Assert.assertEquals(1, userOrder.getItems().size());
        Assert.assertEquals(user.getUsername(), userOrder.getUser().getUsername());
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void submitOrderUsernameNotFound() {
        ResponseEntity<UserOrder> response = orderController.submit("teste");

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void historyOrder() {
        User user = createUserAndCart();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());

        Assert.assertNotNull(response);
        List<UserOrder> userOrder = response.getBody();
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void historyOrderUsernameNotFound() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("teste");

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }

    public User createUserAndCart() {
        User user = createUser();
        user.setCart(createCart(user));
        return user;
    }

    public User createUser() {
        User user = new User();
        user.setUsername("testName");
        user.setPassword("testPassword");

        return user;
    }

    public List<Item> createItem() {
        Item item = new Item();
        item.setName("Item test");
        item.setPrice(BigDecimal.valueOf(22));
        item.setDescription("description");
        item.setId(1L);

        List<Item> items = new ArrayList<Item>();
        items.add(item);
        return items;
    }

    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setItems(createItem());
        cart.setTotal(BigDecimal.valueOf(22));
        cart.setUser(user);
        cart.setId(1L);

        return cart;
    }

}
