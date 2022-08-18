package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderTest {

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController(userRepository, orderRepository);
        TestHelp.addObjects(orderController, "orderRepository", orderRepository);

        Item item = new Item();
        item.setId(1L);
        item.setName("ball");
        item.setDescription("ball");
        BigDecimal price = BigDecimal.valueOf(1.00);
        item.setPrice(price);


        List<Item> items = new ArrayList<Item>();
        items.add(item);

        User user = new User();
        Cart cart = new Cart();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("password");
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(items);
        BigDecimal total = BigDecimal.valueOf(1.00);
        cart.setTotal(total);
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);
        when(userRepository.findByUsername("tEsT")).thenReturn(null);

    }
    @Test
    public void submit_order_correct() {
        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertEquals(200, response.getStatusCodeValue());
        UserOrder order = response.getBody();
        assertEquals("ball", order.getItems().get(0).getName());
    }

    @Test
    public void submit_order_user_not_found() {
        ResponseEntity<UserOrder> response = orderController.submit("tEST");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_orders_by_user_correct() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("test");
        assertEquals(200, ordersForUser.getStatusCodeValue());
        List<UserOrder> orders = ordersForUser.getBody();
        assertNotNull(orders);

    }

    @Test
    public void get_orders_for_user_not_found() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("tEST");
        assertEquals(404, ordersForUser.getStatusCodeValue());

    }
}
