package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller for managing User as Customer.
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final UserService userService;

    @Autowired
    public CustomerController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new customer.
     *
     * @param user The User object with customer details.
     * @return The created User object.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerCustomer(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    /**
     * Retrieves all users.
     *
     * @return List of all User objects.
     */
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Retrieves a customer's details by user ID.
     *
     * @param userId The ID of the user.
     * @return User object.
     */
    @GetMapping("/details/{userId}")
    public ResponseEntity<User> getUserDetails(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Updates a customer's profile.
     *
     * @param userId      The ID of the user.
     * @param userDetails The User object with updated profile details.
     * @return The updated User object.
     */
    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUserDetails(@PathVariable Long userId, @RequestBody User userDetails) {
        User updatedUser = userService.updateProfile(userId, userDetails);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Updates the customer's profile picture.
     *
     * @param userId The ID of the user.
     * @param file   The profile picture file.
     * @return The updated User object.
     */
    @PutMapping("/updateProfilePic/{userId}")
    public ResponseEntity<User> updateProfilePic(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        User updatedUser = userService.updateProfilePic(userId, file);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Deletes the user's account.
     *
     * @param userId The ID of the user.
     * @return Response message indicating successful deletion.
     */
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deactivateUser(userId);
        return new ResponseEntity<>("User account deleted successfully.", HttpStatus.OK);
    }
}
