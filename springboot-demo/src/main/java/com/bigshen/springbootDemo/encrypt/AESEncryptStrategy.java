package com.bigshen.springbootDemo.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;


/**
 * 对接idaas时引入
 */
@Component
public class AESEncryptStrategy implements EncryptStrategy {

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_STR = "AES/ECB/PKCS5Padding";
    /**
     * 字符编码
     */
    private static final String CHARSET = "UTF-8";

    @Override
    public String getNameKey() {
        // TODO 从配置中获取
        return "AES";
    }

    @Override
    public String getPwdKey() {
        // TODO 从配置中获取
        return "AES KEY";
    }

    /**
     * AES加密
     *
     * @param plainText 明文，待加密字符串
     * @param key       密钥
     * @return 密文
     */
    @Override
    public String encrypt(String plainText, String key) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return Base64.encodeBase64String(cipher.doFinal(plainText.getBytes(CHARSET)));
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    @Override
    public Boolean support(int type) {
        return type == 2;
    }
}