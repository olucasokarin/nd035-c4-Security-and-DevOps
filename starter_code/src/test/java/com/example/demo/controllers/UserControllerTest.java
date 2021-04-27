package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.persistence.User;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserHappyPath() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUser = createUser();
        ResponseEntity<User> response = userController.createUser(createUser);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        Assert.assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void createUserPasswordLess() throws Exception {
        CreateUserRequest createUser = createUserFailurePassword();
        ResponseEntity<User> response = userController.createUser(createUser);

        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void UserAlreadyExists() throws Exception {
        CreateUserRequest createUser = createUser();
        ResponseEntity<User> response = userController.createUser(createUser);

        when(userRepository.findByUsername(response.getBody().getUsername())).thenReturn(response.getBody());

        ResponseEntity<User> responseExists = userController.createUser(createUser);

        Assert.assertNotNull(responseExists);
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseExists.getStatusCode());
    }

    @Test
    public void getUserByName() throws Exception {
        CreateUserRequest createUser = createUser();
        ResponseEntity<User> response = userController.createUser(createUser);
        Assert.assertNotNull(response);

        when(userRepository.findByUsername(response.getBody().getUsername())).thenReturn(response.getBody());

        User user = response.getBody();

        ResponseEntity<User> findUserResponse = userController.findByUserName(user.getUsername());
        User findUser = findUserResponse.getBody();

        Assert.assertNotNull(findUser);
        Assert.assertEquals(user.getUsername(), findUser.getUsername());
    }

    @Test
    public void getUserById() throws Exception {
        CreateUserRequest createUser = createUser();
        ResponseEntity<User> response = userController.createUser(createUser);
        Assert.assertNotNull(response);

        when(userRepository.findById(response.getBody().getId())).thenReturn(Optional.of(response.getBody()));

        User user = response.getBody();
        ResponseEntity<User> findUserResponse = userController.findById(user.getId());
        User findUser = findUserResponse.getBody();

        Assert.assertNotNull(findUser);
        Assert.assertEquals(user.getUsername(), findUser.getUsername());
        Assert.assertEquals(user.getId(), findUser.getId());
    }

    public CreateUserRequest createUser() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testName");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        return createUserRequest;
    }

    public CreateUserRequest createUserFailurePassword() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testName");
        createUserRequest.setPassword("testPa");
        createUserRequest.setConfirmPassword("testPa");

        return createUserRequest;
    }
}

