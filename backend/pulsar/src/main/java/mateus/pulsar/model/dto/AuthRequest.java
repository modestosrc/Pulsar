package mateus.pulsar.model.dto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthRequest {
    private String username;
    private String password;

    public AuthRequest(String username, String password) {
        this.username = username;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

}
