package com.generation.veziocastelmanager.security;

import com.generation.veziocastelmanager.model.entities.User;
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
public class JwtService
{
    // chiave usata per firmare il token
    // in un progetto reale non si mette qui nel codice ma nelle variabili d'ambiente
    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    // genera il token a partire dai dati dell'utente
    // mette nel payload le info che ci servono lato frontend
    public String generateToken(User user)
    {
        // claims = dati che voglio mettere dentro il token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",        user.getId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName",  user.getLastName());
        claims.put("username",  user.getUsername());
        claims.put("email",     user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())      // subject = chi è l'utente
                .setIssuedAt(new Date(System.currentTimeMillis()))  // quando è stato creato
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // scade dopo 10 ore
                .signWith(getSignKey(), SignatureAlgorithm.HS256)    // firmo con la chiave segreta
                .compact();     // genera la stringa finale del token
    }

    // getSubject restituisce il campo "sub" del token, che abbiamo impostato con setSubject(username)
    //  estrae lo username dal token
    public String   extractUsername(String token)
    {
        // Claims::getSubject versione compatta di: claims -> claims.getSubject()
        return extractClaim(token, Claims::getSubject);
    }

    // prende tutti i claims del token e ci applica la funzione che gli passo come parametro
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        return claimsResolver.apply(extractAllClaims(token));
    }

    /*
     *
     *       public boolean      isTokenValid(String token, UserDetails userDetails)
     *       {
     *           return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
     *       }
     */
    // controlla due cose insieme:lo username nel token coincide con quello dell'utente che fa la richiesta; il token non è scaduto
    // se una delle due è falsa, ritorna false
    public boolean      isTokenValid(String token, UserDetails userDetails)
    {
        boolean     usernameOk = extractUsername(token).equals(userDetails.getUsername());
        boolean     nonScaduto = !isTokenExpired(token);
        return      (usernameOk && nonScaduto);
    }

    //getExpiration estrae la data di scadenza del token
    // .before(new Date()) controlla se quella data è già passata rispetto ad adesso
    private boolean         isTokenExpired(String token)
    {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // se la firma non torna lancia eccezione da solo
    private Claims          extractAllClaims(String token)
    {
        return Jwts.parserBuilder()  // creo il parser
                .setSigningKey(getSignKey()) // gli dico con quale chiave verificare la firma
                .build() // costruisco il parser
                .parseClaimsJws(token) // decodifico il token e verifico la firma
                .getBody(); // prendo solo il body, cioè i dati dentro il token
    }

    /*
    // la stringa SECRET è in Base64, la decodifico
    // e la converto in un oggetto Key usabile dall'algoritmo
    private Key         getSignKey()
    {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    }*/

    private Key         getSignKey()
    {
        byte[]  keyBytes = Decoders.BASE64.decode(SECRET);  // decodifico la stringa Base64 in bytes
        // converto i bytes in una Key usabile da HMAC-SHA256
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
