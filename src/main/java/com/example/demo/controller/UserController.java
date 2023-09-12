package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢( POST )🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢
    @PostMapping("create-user")
    public ResponseEntity<User> createUser(@RequestBody String email) {
        User createdUser = userService.createUser(email);
        logger.info("User with email {} successfully created.", email);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    //🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵( GET )🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵

    @GetMapping("get-user-by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("get-user-by-id/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable UUID userId) {
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }



    //🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡( Put )🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡
    @PutMapping("update-user/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable UUID userId, @RequestBody User updatedUser) {
        try {
            User user = userService.updateUser(userId, updatedUser);
            logger.info("User with ID: {} successfully updated.", userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IllegalArgumentException iae) {
            logger.error("Illegal argument exception while updating user with ID: {}", userId, iae);
            return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error occurred while updating user with ID: {}", userId, e);
            return new ResponseEntity<>("An error occurred while updating the user.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
