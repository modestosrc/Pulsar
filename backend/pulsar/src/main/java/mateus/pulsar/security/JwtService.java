package mateus.pulsar.security;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyPair;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.KeyAlgorithm;
import io.jsonwebtoken.security.AeadAlgorithm;

import mateus.pulsar.model.dto.AuthRequest;
import mateus.pulsar.service.UserDetailsServiceImpl;

@Service
public class JwtService {

    KeyPair pair = Jwts.SIG.RS512.keyPair().build();
    KeyAlgorithm<PublicKey, PrivateKey> alg = Jwts.KEY.RSA_OAEP_256;
    AeadAlgorithm enc = Jwts.ENC.A256GCM;

    private final UserDetailsServiceImpl userDetailsService  = new UserDetailsServiceImpl();

    public String generateToken(AuthRequest credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();
        if (!userDetailsService.isUserValid(username, password))
            throw new RuntimeException("Usuário ou senha inválidos");
        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 1)) // 1 hora.
                .encryptWith(pair.getPublic(), alg, enc)
                .compact();
        return token;
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .decryptWith(pair.getPrivate())
                .build()
                .parseEncryptedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            String username = extractUsername(token);
            return userDetailsService.doesUserExist(username) && !isTokenExpired(token);
        } catch (Exception e) {
            System.out.println("Token inválido: " + e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .decryptWith(pair.getPrivate())
                .build()
                .parseEncryptedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    }
}
