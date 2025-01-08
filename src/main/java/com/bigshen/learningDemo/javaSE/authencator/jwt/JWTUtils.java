package com.bigshen.learningDemo.javaSE.authencator.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author byj
 * @date 2025/1/6
 * @Description
 */
public class JWTUtils {
    public static String SIGN = "ASDFse@#w";

    /**
     * 生成Token
     * @param userId
     * @param userName
     * @return
     */
    public static String createToken(String userId, String userName) {
        Map<String, Object> tokenHeaderMap = new HashMap<>();
        Calendar cr = Calendar.getInstance();
        cr.add(Calendar.SECOND, 100);
        String token = JWT.create().withHeader(tokenHeaderMap)
                .withClaim("userId", userId)
                .withClaim("userName", userName)
                .withExpiresAt(cr.getTime())
                // 加签（配置私钥，防止字符串被篡改）
                .sign(Algorithm.HMAC256(SIGN));
        System.out.println("生成的token：" + token);
        return token;
    }

    /**
     * token 验签
     * token 有效负荷读取
     *
     * @param token
     */
    public static DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);//创建验证对象
    }
}
