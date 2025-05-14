package mateus.pulsar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import mateus.pulsar.model.dto.UserDto;
import mateus.pulsar.service.UserDetailsServiceImpl;

/**
 * Controller for managing user-related operations.
 * <p>
 * This controller handles HTTP requests related to user management, including
 * retrieving, creating, updating, and deleting users.
 */
@CrossOrigin(origins = "*")
@RestController
public class UserController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Retrieves the username of the user.
     *
     * @param user The username of the user.
     * @return The UserDetails object representing the user.
     */
    @GetMapping("/{user}/user")
    UserDetails getUser(@PathVariable String user) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(user);
            return userDetails;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Creates a new user.
     *
     * @param user The UserDetails object representing the new user.
     * @return The newly created UserDetails object.
     */
    @PostMapping("/user")
    UserDetails createUser(@RequestBody UserDto user) {
        return userDetailsService.createUser(user);
    }

    /**
     * Updates an existing user.
     *
     * @param user        The username of the user to be updated.
     * @param userDetails The UserDetails object containing the updated user information.
     * @return The updated UserDetails object.
     */
    @PutMapping("/{user}/user")
    UserDetails updateUser(@PathVariable String user, @RequestBody UserDto userDetails) {
        return userDetailsService.setUser(user, userDetails);
    }

    /**
     * Deletes a user.
     *
     * @param user The username of the user to be deleted.
     */
    @DeleteMapping("/{user}/user")
    void deleteUser(@PathVariable String user) {
        userDetailsService.deleteUser(user);
    }
}
