package ir.maktabsharif.finalproject.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import ir.maktabsharif.finalproject.util.KeyUtils;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private static final long EXPIRATION_TIME = 60 * 60 * 1000;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtService() throws Exception {
        this.privateKey = KeyUtils.loadPrivateKey("src/main/resources/keys/private.pem");
        this.publicKey = KeyUtils.loadPublicKey("src/main/resources/keys/public.pem");
    }

    public String generateToken(String username, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .subject(username)
                .claims(extraClaims)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusMillis(EXPIRATION_TIME)))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();

    }

    public String generateToken(String username) {
        return generateToken(username, Map.of());
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public Date extractExpiration(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getExpiration();
    }
}
