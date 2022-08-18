package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTest
{
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController(null, null, null);

        TestHelp.addObjects(userController, "bCryptPasswordEncoder", passwordEncoder);
        TestHelp.addObjects(userController, "userRepository", userRepository);
        TestHelp.addObjects(userController, "cartRepository", cartRepository);


        User user = new User();
        user.setId(0);
        user.setUsername("test");
        user.setPassword("password");
        when(userRepository.findByUsername("test")).thenReturn(user);
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.findByUsername("someone")).thenReturn(null);

    }


    @Test
    public void create_user() {
        when(passwordEncoder.encode("password")).thenReturn("Hashed");
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("test");
        user.setPassword("password");
        user.setConfirmPassword("password");
        ResponseEntity<User> response = userController.createUser(user);
        User u = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("Hashed", u.getPassword());

    }

    @Test
    public void create_user_short_password(){
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("test");
        user.setPassword("pass");
        user.setConfirmPassword("pass");
        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(400, response.getStatusCodeValue());

    }

    @Test
    public void create_user_wrong_password(){
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("test");
        user.setPassword("password");
        user.setConfirmPassword("pAssWORd");
        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(400, response.getStatusCodeValue());

    }

    @Test
    public void find_user_by_id_correct() {
        long id = 0;
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("test");
        user.setPassword("password");
        user.setConfirmPassword("password");
        ResponseEntity<User> response = userController.createUser(user);
        User test = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(id, test.getId());
    }

    @Test
    public void find_user_by_id_wrong() {
        long id = 8730;
        ResponseEntity<User> response = userController.findById(id);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void find_user_by_name_correct() {
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("test");
        user.setPassword("password");
        user.setConfirmPassword("password");
        ResponseEntity<User> response = userController.createUser(user);
        User test = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test", test.getUsername());
    }

    @Test
    public void find_user_by_name_wrong() {
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("test");
        user.setPassword("password");
        user.setConfirmPassword("password");
        userController.createUser(user);
        ResponseEntity<User> response = userController.findByUserName("someone");
        assertEquals(404, response.getStatusCodeValue());
    }

}
