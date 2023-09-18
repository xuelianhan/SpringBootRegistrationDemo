package com.github.register;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author sniper
 * @date 18 Sep 2023
 * @see <a href="https://stackoverflow.com/questions/56181357/expiration-of-jwt-not-working-when-using-expiration-date-in-utc"></a>
 */
public class JWTTokenExpireTest {

    @Test
    public void shouldMatchIssuedAtAndExpiration() {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiration = issuedAt.plus(3, ChronoUnit.MINUTES);

        System.out.println("Issued at: " + issuedAt);
        System.out.println("Expires at: " + expiration);

        String jws = Jwts.builder()
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiration))
                .signWith(key)
                .compact();

        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jws)
                .getBody();

        assertThat(claims.getIssuedAt().toInstant(), is(issuedAt));
        assertThat(claims.getExpiration().toInstant(), is(expiration));
    }


}
