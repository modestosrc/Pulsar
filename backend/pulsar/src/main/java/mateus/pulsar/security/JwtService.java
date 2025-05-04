package mateus.pulsar.security;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyPair;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.KeyAlgorithm;
import io.jsonwebtoken.security.AeadAlgorithm;

import mateus.pulsar.model.dto.AuthRequest;

@Service
public class JwtService {

    KeyPair pair = Jwts.SIG.RS512.keyPair().build();
    KeyAlgorithm<PublicKey, PrivateKey> alg = Jwts.KEY.RSA_OAEP_256;
    AeadAlgorithm enc = Jwts.ENC.A256GCM;

    public String generateToken(AuthRequest credentials) {
        return Jwts.builder()
                .subject(credentials.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 1)) // 1 hora.
                .encryptWith(pair.getPublic(), alg, enc)
                .compact();
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
        // TODO:
        // - Implementar banco de usuários.
        return extractUsername(token) == "test_user";
    }

    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .decryptWith(pair.getPrivate())
                .build()
                .parseEncryptedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    }
}
