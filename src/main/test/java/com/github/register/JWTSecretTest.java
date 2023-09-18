package com.github.register;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.HashMap;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
public class JWTSecretTest {

    public static void main(String[] args) {
        String u = "LM22cp1O75xadIIrSee10adc3949ba59abbe56e057f20f883e7c121ff53bf6245f24d36e0a10481f56kmweLEmEu6PY";
        System.out.println("length of this string: "+u.getBytes().length);
        byte[] bytes = Decoders.BASE64.decode(u);
        System.out.println("length after decoded by BASE64: "+bytes.length);
        Key secretKey = Keys.hmacShaKeyFor(bytes);
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("name","csl");
        String jwt = Jwts.builder()
                .setClaims(payload)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        System.out.println(jwt);
    }
}
