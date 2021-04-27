package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private CartRepository cartRepository = mock(CartRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "orderRepository", orderRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCart() {
        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        User user = createUser();
        Item item = createItem();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void addToCartUserNotFound() {
        ResponseEntity<Cart> response = cartController.addTocart(createModifyCartRequest());

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addToCartItemNotPresent() {
        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        modifyCartRequest.setItemId(0);
        User user = createUser();
        Item item = createItem();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCart() {
        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        User user = createUser();
        Item item = createItem();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartUserNotFound() {
        ResponseEntity<Cart> response = cartController.removeFromcart(createModifyCartRequest());

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartItemNotPresent() {
        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        modifyCartRequest.setItemId(0);
        User user = createUser();
        Item item = createItem();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }


    public ModifyCartRequest createModifyCartRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(createItem().getId());
        modifyCartRequest.setUsername(createUser().getUsername());
        modifyCartRequest.setQuantity(1);

        return modifyCartRequest;
    }

    public User createUser() {
        User user = new User();
        user.setUsername("testName");
        user.setPassword("testPassword");
        user.setCart(createCart(user));

        return user;
    }

    public Item createItem() {
        Item item = new Item();
        item.setName("Item test");
        item.setPrice(BigDecimal.valueOf(22));
        item.setDescription("description");
        item.setId(1L);

        return item;
    }

    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setTotal(BigDecimal.valueOf(22));
        cart.setUser(user);
        cart.setId(1L);

        List<Item> items = new ArrayList<Item>();
        items.add(createItem());
        cart.setItems(items);

        return cart;
    }
}
