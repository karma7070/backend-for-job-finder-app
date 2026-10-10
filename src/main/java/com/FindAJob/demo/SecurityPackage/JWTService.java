package com.FindAJob.demo.SecurityPackage;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

@Service
public class JWTService {
    @Value("${jwt.token}")
    private String secretKey;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());

    }

    //creates JWT from email after log in

    public String generateToken(String email,
                                String name,
                                UserRoles role){
        return Jwts.builder()//starts the creation of the jwt
                .subject(email) //uses email to create token for user to identify user
                .claim("name", name)
                .claim("role", role)
                .issuedAt(new Date()) //time of creation
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) //time of expiration
                .signWith(this.getKey())//signs token so it can't be tampered with
                .compact(); //converts all of this into JWT
    }

    //used whenever a request arrives with a JWT
    //Reads the token and returns the email that was stored inside it.

    public String extractEmail(String token){

        return Jwts.parser()
                .verifyWith(getKey()) //verify the token was signed with secret key
                .build()
                .parseSignedClaims(token)//parses JWT i.e converts it back
                .getPayload()// gets the data in the JWT i.e payload
                .getSubject(); //returns email
    }

    public String extractUsername(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("name", String.class);
    }

    public String extractRole(String token){
       return Jwts.parser()
               .verifyWith(getKey())
               .build()
               .parseSignedClaims(token)
               .getPayload()
               .get("role", String.class);
    }


    public boolean isTokenValid(String token){
        try {
            Jwts.parser()
                    .verifyWith(getKey())//verifies signature authenticity
                    .build()
                    .parseSignedClaims(token); // checks if it has expired  by converting it back

            return true;
        } catch (Exception e){
            return false;
        }
    }

}
