package mateus.pulsar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import mateus.pulsar.model.dto.AuthRequest;
import mateus.pulsar.security.JwtService;

/**
 * Controller for handling authentication requests.
 * <p>
 * This controller provides endpoints for user login and token generation.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * The JwtService instance used for token generation.
     */
    @Autowired
    private JwtService jwtService;

    /**
     * Handles user login and token generation.
     *
     * @param credentials The authentication request containing username and
     *                    password.
     * @return A ResponseEntity containing the generated JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest credentials) {

        String token = jwtService.generateToken(credentials);
        return ResponseEntity.ok("{token: " + token + "}" + "{username: " + credentials.getUsername() + "}");
    }
}
