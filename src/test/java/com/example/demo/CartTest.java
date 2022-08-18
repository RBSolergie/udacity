package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartTest {

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController(null, null, null);
        TestHelp.addObjects(cartController, "userRepository", userRepository);
        TestHelp.addObjects(cartController, "cartRepository", cartRepository);
        TestHelp.addObjects(cartController, "itemRepository", itemRepository);

        User user = new User();
        Cart cart = new Cart();

        user.setId(1);
        user.setUsername("test");
        user.setPassword("password");
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("ball");
        item.setDescription("ball");
        BigDecimal price = BigDecimal.valueOf(1.00);
        item.setPrice(price);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));

    }

    @Test
    public void add_to_cart_correct() {
        ModifyCartRequest Cart = new ModifyCartRequest();
        double total = 1.00;
        Cart.setItemId(1L);
        Cart.setQuantity(1);
        Cart.setUsername("test");
        ResponseEntity<Cart> response = cartController.addTocart(Cart);

        assertEquals(200, response.getStatusCodeValue());
        Cart c = response.getBody();
        assertEquals(BigDecimal.valueOf(total), c.getTotal());

    }

    @Test
    public void add_to_cart_wrong_user() {
        ModifyCartRequest cart = new ModifyCartRequest();
        cart.setItemId(1L);
        cart.setQuantity(1);
        cart.setUsername("tEST");
        ResponseEntity<Cart> response = cartController.addTocart(cart);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_correct() {
        ModifyCartRequest cart = new ModifyCartRequest();
        cart.setItemId(1L);
        cart.setQuantity(5);
        cart.setUsername("test");
        cartController.addTocart(cart);

        double total = 1.0+1.0;
        cart = new ModifyCartRequest();
        cart.setItemId(1L);
        cart.setQuantity(3);
        cart.setUsername("test");
        ResponseEntity<Cart> response = cartController.removeFromcart(cart);
        Cart c = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(total), c.getTotal());

    }

    @Test
    public void remove_from_cart_wrong_item() {
        ModifyCartRequest cart = new ModifyCartRequest();
        int id = 8730;
        cart.setItemId(8730);
        cart.setQuantity(1);
        cart.setUsername("test");
        ResponseEntity<Cart> response = cartController.removeFromcart(cart);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_too_much() {
        ModifyCartRequest cart = new ModifyCartRequest();
        cart.setItemId(1L);
        cart.setQuantity(5);
        cart.setUsername("test");
        cartController.addTocart(cart);

        cart = new ModifyCartRequest();
        cart.setItemId(1L);
        cart.setQuantity(8);
        cart.setUsername("test");
        ResponseEntity<Cart> response = cartController.removeFromcart(cart);
        Cart c = response.getBody();
        double total = -3.0;

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(total), c.getTotal());
    }






}
