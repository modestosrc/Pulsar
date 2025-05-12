package mateus.pulsar.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mateus.pulsar.repository.FileUserRepository;
import mateus.pulsar.model.dto.UserDto;

/**
 * Service for managing user details and authentication.
 * <p>
 * This service provides methods for loading user details, validating users,
 * creating default users, and managing user accounts.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private FileUserRepository userRepository;

    public UserDetailsServiceImpl(FileUserRepository userRepository) {
        this.userRepository = userRepository;
        createDefaultUser();
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    /**
     * Loads user details by username.
     *
     * @param username The username of the user to be loaded.
     * @return UserDetails object representing the user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            return userRepository.loadUserByUsername(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    /**
     * Validates the user's credentials.
     *
     * @param username The username of the user to be validated.
     * @param password The password of the user to be validated.
     * @return true if the user is valid, false otherwise.
     */
    public boolean isUserValid(String username, String password) {
        try {
            User user = userRepository.loadUserByUsername(username);
            if (user.getUsername().equals(username) && passwordEncoder.matches(password, user.getPassword())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error loading user: " + username);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a user exists in the system.
     *
     * @param username The username of the user to be checked.
     * @return true if the user exists, false otherwise.
     */
    public boolean doesUserExist(String username) {
        try {
            User _ = userRepository.loadUserByUsername(username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Creates a default user if it does not already exist.
     */
    public void createDefaultUser() {
        try {
            if (!doesUserExist("test_user")) {
                User user = new User("test_user", passwordEncoder.encode("1504"),
                        List.of(new SimpleGrantedAuthority("ROLE_USER")));
                userRepository.save(user);
            }
        } catch (Exception e) {
            System.out.println("Error creating default user");
            e.printStackTrace();
        }
    }

    /**
     * Creates a new user in the system.
     *
     * @param user The UserDetails object representing the new user.
     * @return The newly created UserDetails object.
     */
    public UserDetails createUser(UserDto user) {
        try {
            if (doesUserExist(user.getUsername())) {
                throw new Exception("User already exists");
            }
            User newUser = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()),
                    List.of(new SimpleGrantedAuthority("ROLE_USER")));
            userRepository.save(newUser);
            return newUser;
        } catch (Exception e) {
            System.out.println("Error creating user: " + user.getUsername());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates an existing user in the system.
     *
     * @param username The username of the user to be updated.
     * @param user     The UserDetails object containing the updated user
     *                 information.
     * @return The updated UserDetails object.
     */
    public UserDetails setUser(String username, UserDto user) {
        try {
            if (!doesUserExist(username)) {
                throw new Exception("User does not exist");
            }
            User updatedUser = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()),
                    List.of(new SimpleGrantedAuthority("ROLE_USER")));
            userRepository.save(updatedUser);
            return updatedUser;
        } catch (Exception e) {
            System.out.println("Error updating user: " + user.getUsername());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes a user from the system.
     *
     * @param username The username of the user to be deleted.
     */
    public void deleteUser(String username) {
        try {
            if (!doesUserExist(username)) {
                throw new Exception("User does not exist");
            }
            userRepository.deleteUser(username);
        } catch (Exception e) {
            System.out.println("Error deleting user: " + username);
            e.printStackTrace();
        }
    }
}
