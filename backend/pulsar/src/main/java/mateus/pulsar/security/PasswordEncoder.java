package mateus.pulsar.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder{
    private String rawPassword;
    private String encodedPassword;

    public PasswordEncoder(String rawPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.rawPassword = rawPassword;
        this.encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public boolean isPasswordValid(String rawPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
