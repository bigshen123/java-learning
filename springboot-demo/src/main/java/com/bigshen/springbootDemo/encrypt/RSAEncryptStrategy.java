package com.bigshen.springbootDemo.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * 对接JD项目时引入
 */
@Component
public class RSAEncryptStrategy implements EncryptStrategy {

    @Override
    public String getNameKey() {
        // TODO 从配置中获取
        return "JD RSA";
    }

    @Override
    public String getPwdKey() {
        // TODO 从配置中获取
        return "JD RSA KEY";
    }

    /**
     * RSA公钥加密
     *
     * @param plainText 明文，待加密字符串
     * @param publicKey 公钥
     * @return 密文
     */
    @Override
    public String encrypt(String plainText, String publicKey) {
        try {
            //base64编码的公钥
            byte[] decoded = Base64.decodeBase64(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return Base64.encodeBase64String(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    @Override
    public Boolean support(int type) {
        return type == 1;
    }
}