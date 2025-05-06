package mateus.pulsar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mateus.pulsar.model.dto.AuthRequest;
import mateus.pulsar.security.JwtService;

@RestController

@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest credentials) {

        String token = jwtService.generateToken(credentials);
        return ResponseEntity.ok(token);
    }
}
