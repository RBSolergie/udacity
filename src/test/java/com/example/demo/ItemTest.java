package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemTest {
    private CartController cartController;
    private ItemController itemController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController(null);
        TestHelp.addObjects(itemController, "itemRepository", itemRepository);

        Item item = new Item();
        item.setId(1L);
        item.setName("ball");
        item.setDescription("ball");
        BigDecimal price = BigDecimal.valueOf(1.00);
        item.setPrice(price);
        when(itemRepository.findByName("ball")).thenReturn(Collections.singletonList(item));
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));

    }

    @Test
    public void get_all_items() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertEquals(1, items.size());
    }

    @Test
    public void get_items_by_name_correct() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("ball");
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertEquals("ball", items.get(0).getName());
    }

    @Test
    public void get_items_by_name_wrong() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("BaLl");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_item_by_id_correct() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertEquals(200, response.getStatusCodeValue());
        Item item = response.getBody();
        assertEquals("ball", item.getName());
    }

    @Test
    public void get_item_by_id_wrong() {
        ResponseEntity<Item> response = itemController.getItemById(2L);
        assertEquals(404, response.getStatusCodeValue());
        //assertEquals(null, item.getName());
    }


}
