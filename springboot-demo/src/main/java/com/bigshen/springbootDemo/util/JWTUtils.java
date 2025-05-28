package com.bigshen.springbootDemo.util;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author byj
 * @date 2025/1/6
 * @Description
 */
public class JWTUtils {
    /**
     * 根据有效荷载创建JWT验证字符串
     * 使用的签名算法为HS256,对称算法，生成JWT字符串与解析JWT
     *
     * @param playload JWT有效荷载
     * @param key      签名的key,
     * @return
     */
    public String createJWTStrHS256(Map<String, Object> playload, String key) {
        //头部JSON数据，固定写法
        Map<String, Object> headerMap = new HashMap<>();
        //算法HS256
        headerMap.put("alg", SignatureAlgorithm.HS256.getValue());
        headerMap.put("typ", "JWT");

        //使用JJWT提供的API创建JWT
        String s = Jwts.builder()
                //设置头部数据
                .setHeader(headerMap)
                //设置有效荷载数据
                .setClaims(playload)
                //使用HS256签名算法
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        return s;
    }

    /**
     * 从JWT字符串中获得有效荷载对象
     *
     * @param JwtStr
     * @param key    自定义key，与创建JWT使用的key一致
     * @return
     */
    public Object decodeJWTrHS256(String JwtStr, String key) {
        //头部JSON数据，固定写法
        Map<String, Object> headerMap = new HashMap<>();
        //算法HS256
        headerMap.put("alg", SignatureAlgorithm.HS256.getValue());
        headerMap.put("typ", "JWT");

        Jwt jwt = Jwts.parser()
                .setSigningKey(key)
                .parse(JwtStr);

        return jwt.getBody();
    }

    /**
     * 生成RS256签名算法中需要的公钥私钥文件
     *
     * @param password 随机密码
     */
    public void generateRS256Key(String password) throws NoSuchAlgorithmException {
        //钥匙对生成Generator，使用的是RSA算法
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom(password.getBytes());
        keyPairGenerator.initialize(1024, secureRandom);
        //获得钥匙对
        KeyPair keyPair = keyPairGenerator.genKeyPair();

        //公钥字节码
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        //私钥字节码
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();

        //公私钥匙对信息写入文件
        try {
            FileOutputStream fos = new FileOutputStream("..\\pub.key");
            fos.write(publicKeyBytes);
            fos.close();

            FileOutputStream fos_1 = new FileOutputStream("..\\pri.key");
            fos_1.write(privateKeyBytes);
            fos_1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据私钥物理文件来创建私钥对象，用于JWT使用RSA256签名算法
     *
     * @return
     */
    public PrivateKey generatePrivateKey() {
        try {
            //根据类路径下的pri.key私钥物理文件获得输入流
            InputStream resourceAsStream =
                    this.getClass().getClassLoader().getResourceAsStream("pri.key");
            DataInputStream dis = new DataInputStream(resourceAsStream);
            byte[] keyBytes = new byte[resourceAsStream.available()];
            dis.readFully(keyBytes);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据公钥物理文件来创建公钥对象，用于JWT使用RSA256签名算法
     *
     * @return
     */
    public PublicKey generatePublicKey() {
        try {
            //根据类路径下的pri.key私钥物理文件获得输入流
            InputStream resourceAsStream =
                    this.getClass().getClassLoader().getResourceAsStream("pri.key");
            DataInputStream dis = new DataInputStream(resourceAsStream);
            byte[] keyBytes = new byte[resourceAsStream.available()];
            dis.readFully(keyBytes);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据有效荷载创建JWT验证字符串
     * 使用的签名算法为RS256,非对称算法，创建JWT字符串需要使用私钥
     *
     * @param playload JWT有效荷载
     * @return
     */
    public String createJWTStrRS256(Map<String, Object> playload) {
        //头部JSON数据，固定写法
        Map<String, Object> headerMap = new HashMap<>();
        //算法HS256
        headerMap.put("alg", SignatureAlgorithm.RS256.getValue());
        headerMap.put("typ", "JWT");

        PrivateKey key = generatePrivateKey();

        //使用JJWT提供的API创建JWT
        String s = Jwts.builder()
                //设置头部数据
                .setHeader(headerMap)
                //设置有效荷载数据
                .setClaims(playload)
                //使用HS256签名算法
                .signWith(SignatureAlgorithm.RS256, key)
                .compact();
        return s;
    }

    /**
     * 从JWT字符串中获得有效荷载对象
     *
     * @param JwtStr 需要从公钥中解析JWT
     * @return
     */
    public Object decodeJWTrRS256(String JwtStr) {
        //头部JSON数据，固定写法
        Map<String, Object> headerMap = new HashMap<>();
        //算法HS256
        headerMap.put("alg", SignatureAlgorithm.RS256.getValue());
        headerMap.put("typ", "JWT");

        PrivateKey key = generatePrivateKey();

        Jwt jwt = Jwts.parser()
                .setSigningKey(key)
                .parse(JwtStr);

        return jwt.getBody();
    }

}
