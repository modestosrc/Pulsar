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

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            return userRepository.loadUserByUsername(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

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

    public boolean doesUserExist(String username) {
        try {
            User _ = userRepository.loadUserByUsername(username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void createDefaultUser() {
        try {
            if (!doesUserExist("test_user")) {
                User user = new User("test_user", passwordEncoder.encode("1504"), List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
                userRepository.save(user);
            }
        } catch (Exception e) {
            System.out.println("Error creating default user");
            e.printStackTrace();
        }
    }
}
