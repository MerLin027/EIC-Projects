package com.eic.product_catalogue_api.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService
{
    private final SecretKey secretKey;
    private final Long expirationMs;
    private final Long now = System.currentTimeMillis();
    private static final String ISSUER = "PRODUCT_CATALOGUE_API";
    //
    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration-ms}") Long expirationMs)
    {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        // returns a cryptographic key which will be used to generate the JWT
        this.expirationMs = expirationMs;
    }
    //
    public String generateToken(String username)
    {
        return Jwts.builder()
                .subject(username)
                .issuer(ISSUER)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(secretKey)
                // text = encodedHEADER.encodedPAYLOAD (BASE64)
                // signature = HMAC-SHA256(text, SECRET_KEY)
                .compact(); // Assembles the final string: encdedHeader.encodedPayload.encodedSignature
    }
    //
    public String validateTokenAndGetUsername(String token)
    {
        return extractAllClaims(token).getSubject();
        // We extract claims from the token and then extract the Subject, which is the username
    }
    //
    private Claims extractAllClaims(String token) throws JwtException
    {
        // This method asks JJWT to parse the token, verify its signature, check its expiration and THEN extract its
        // claims. Only then, the username is extracted from the subject claim

        return Jwts.parser()
                // Component that reads the incoming JWT
                .verifyWith(secretKey) // Configures the verification key
                .requireIssuer(ISSUER) // Configures the ISSUER
                .build()
                // Builds the configured parser
                .parseSignedClaims(token)
                // Parses the JWT - verifies its signature and validates it
                // Verifies the signature - reads header and payload, reconstructs original data, uses secret key to
                // calculate expected key, compares the expected signature to the incoming signature in the token
                // If the signatures do not match OR if the token is malformed, parsing fails with JwtException
                // If the token is null or empty, parsing fails with IllegalArgumentException
                // Validates claims - checks registered claims such as expiration
                // An expired token causes an ExpiredJwtException which is a type of JwtException
                // DOES NOT AUTHENTICATE AGAINST THE DB
                .getPayload(); // Once parsing succeeds, it retrieves JWT's payload as a Claims object
    }
}
