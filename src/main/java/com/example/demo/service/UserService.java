package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢( POST )🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢
    public User createUser(String email) {
        // Check if email already exists
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            // Return the existing user without overwriting
            return existingUser.get();
        }

        // If the user is new, set the default values
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(email);  // Assuming you want to use the email as the username by default
        newUser.setPicture("https://res.cloudinary.com/yilin1234/image/upload/v1685746074/Generic-Profile-Image_vlk1kx.png");
        newUser.setAbout("Hello, I'm new here and excited to be part of this community!");

        return userRepository.save(newUser);
    }


    //🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡( Put )🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡
    public User updateUser(UUID id, User updatedUser) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with ID " + id + " not found.");
        }

        // Check if another user with the same username exists.
        Optional<User> userWithSameUsername = userRepository.findByUsername(updatedUser.getUsername());

        if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(id)) {
            throw new IllegalArgumentException("Username is already taken by another user.");
        }

        updatedUser.setId(id);
        return userRepository.save(updatedUser);
    }

    //🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵( GET )🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found."));
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found."));
    }
}
