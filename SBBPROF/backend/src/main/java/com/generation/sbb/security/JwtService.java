package com.generation.sbb.security;

import com.generation.sbb.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Chiave segreta (in un'app reale va nelle variabili d'ambiente, non nel codice!)
    // Deve essere lunga almeno 256 bit per HS256
    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    // il token per come lo genero può essere RAGIONEVOLMENTE stato generato SOLO da questo segreto e solo con quei dati

    // il Token deve contenere dei dati sulla sua generazione (frega niente), vengono messi in automatico
    // un PAYLOAD che deve contenere i dati dell'utente e i suoi CLAIMS. Le sue informazioni
    // i suoi DIRITTI
    // e alla fine contiene una firma
    // te ne freghi della firma, te ne freghi dell'intestazione

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("username", user.getUsername()); 
        claims.put("role", user.getRole());
        claims.put("quote","Amo il profumo del napalm al mattino");
        claims.put("email", user.getEmail());
        if (user.getHotel() != null) {
            claims.put("hotelId", user.getHotel().getId());
            claims.put("hotelName", user.getHotel().getName());
            claims.put("hotelAddress", user.getHotel().getAddress());
            claims.put("hotelCity", user.getHotel().getCity());
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Scade tra 10 ore
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

        // risultato
        //eyJhbGciOiJIUzI1NiJ9.eyJmaXJzdE5hbWUiOiJMYXVyYSIsImxhc3ROYW1lIjoiTGVvbmUiLCJob3RlbENpdHkiOiJNb256YSIsInJvbGUiOiJSRUNFUFRJT05JU1QiLCJob3RlbEFkZHJlc3MiOiJWaWEgQ2VzYXJlIEJhdHRpc3RpIiwiaWQiOjEsImhvdGVsSWQiOjEsImhvdGVsTmFtZSI6IkImQiBTdXBlcmlvciBNb256YSIsImVtYWlsIjoibGF1cmEubGVvbmVAZmFrZW1haWwuaXQiLCJ1c2VybmFtZSI6ImxsYXVyYSIsInN1YiI6ImxsYXVyYSIsImlhdCI6MTc3MjQ0MDY5NSwiZXhwIjoxNzcyNDc2Njk1fQ.7U3vux8QxnB3CuG23q7psYMJQ31r0yszWtnjKBCMGus

    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}