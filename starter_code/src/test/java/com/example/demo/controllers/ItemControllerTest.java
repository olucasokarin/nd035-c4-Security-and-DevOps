package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItems() {
        Item item = createItem();
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        ResponseEntity<List<Item>> response = itemController.getItems();
        Assert.assertNotNull(response);

        List<Item> items = response.getBody();
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals(1, items.size());
    }

    @Test
    public void getItemById() {
        Item item = createItem();
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(item.getId());
        Assert.assertNotNull(response);

        Item responseItem = response.getBody();
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals(item.getName(), responseItem.getName());
    }

    @Test
    public void getItemsByName() {
        Item item = createItem();
        when(itemRepository.findByName(item.getName())).thenReturn(Collections.singletonList(item));
        ResponseEntity<List<Item>> response = itemController.getItemsByName(item.getName());
        Assert.assertNotNull(response);

        List<Item> items = response.getBody();
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals(1, items.size());
    }

    @Test
    public void getItemsByNameNotFound() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("test");
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }

    public Item createItem() {
        Item item = new Item();
        item.setName("Item test");
        item.setPrice(BigDecimal.valueOf(22));
        item.setDescription("description");
        item.setId(1L);

        return item;
    }
}
