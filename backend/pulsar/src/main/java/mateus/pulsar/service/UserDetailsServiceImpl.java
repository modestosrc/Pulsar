package mateus.pulsar.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import mateus.pulsar.repository.FileUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private FileUserRepository userRepository;

    public UserDetailsServiceImpl(FileUserRepository userRepository) {
        this.userRepository = userRepository;
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
}
