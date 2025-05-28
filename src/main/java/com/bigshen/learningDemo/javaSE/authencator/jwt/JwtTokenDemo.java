package com.bigshen.learningDemo.javaSE.authencator.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Clock;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * @author byj
 * @date 2025/1/6
 * @Description
 */
public class JwtTokenDemo {

    public static void main(String[] args) {
        buildToken();
        verifyToken();

    }

    private static void buildToken() {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            String token = JWT.create()
                    .withIssuer("auth0")
                    .sign(algorithm);
            System.out.println("token: " + token);
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
        }
    }

    private static void verifyToken() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXUyJ9.eyJpc3MiOiJhdXRoMCJ9.AbIJTDMFc7yUa5MhvcP03nJPyCPzZtQcGEp-zWfOkEE";
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);

            JWTVerifier verifier2 = JWT.require(algorithm)
                    .acceptLeeway(1) // 1 sec for nbf, iat and exp
                    .build();

            JWTVerifier.BaseVerification verification = (JWTVerifier.BaseVerification) JWT.require(algorithm)
                    .acceptLeeway(1)
                    .acceptExpiresAt(5);
            Clock clock = new CustomClock(); //Must implement Clock interface
            JWTVerifier verifier3 = verification.build(clock);
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
        }


    }

    private static class CustomClock implements Clock {
        @Override
        public Date getToday() {
            return null;
        }
    }
}
