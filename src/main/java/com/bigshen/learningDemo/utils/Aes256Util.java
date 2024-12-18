package com.bigshen.learningDemo.utils;

import kl.nbase.security.jce.provider.BouncyCastleProvider;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * @author byj
 * @date 2024/12/12
 * @Description
 */
public class Aes256Util {
    private static final String ENCODING = "UTF-8";
    /**
     * IV length must be 8 bytes long
     */
    private static final byte[] SALT_BYTES = "SALT_BYTES".getBytes();

    private static final String AES = "AES";
    private static final String BC = "BC";
    private static final String AES_256_ALGORITHM_NAME = "PBKDF2WithHmacSHA256";
    private static final String AES_256_MODE_PADDING = "AES/ECB/PKCS7Padding";


    private static Cipher getCipherByAes(int mode, String secretKey) throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException, NoSuchProviderException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(AES_256_ALGORITHM_NAME);
        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), SALT_BYTES, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), AES);
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(AES_256_MODE_PADDING, BC);
        cipher.init(mode, secretKeySpec);
        return cipher;
    }

    /**
     * 方法描述：AES256加密
     *
     * @param plainText 明文
     * @param secretKey 密钥
     * @return String    密文
     * @throws Exception 加密失败
     */
    public static String encode(String plainText, String secretKey)
            throws Exception {
        Cipher cipher = getCipherByAes(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(ENCODING));
        return new String(Base64Utils.encode(encryptData), ENCODING).trim();
    }

    /**
     * 方法描述： AES256解密
     *
     * @param encryptText 密文
     * @param secretKey   密钥
     * @return String     明文
     * @throws Exception 解密失败
     */
    public static String decode( String encryptText,  String secretKey)
            throws Exception {
        Cipher cipher = getCipherByAes(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptData = cipher.doFinal(Base64Utils.decode(encryptText.getBytes(ENCODING)));
        return new String(decryptData, ENCODING).trim();
    }

    public static void main(String[] args) {
        try {
            String key = "autoLoginKey";
            //加密
            String encryptStr = Aes256Util.encode("AES,高级加密标准（英语：Advanced Encryption Standard，缩写：AES），在密码学中又称Rijndael加密法，是美国联邦政府采用的一种区块加密标准。这个标准用来替代原先的DES，已经被多方分析且广为全世界所使用。" +
                    "严格地说，AES和Rijndael加密法并不完全一样（虽然在实际应用中二者可以互换），因为Rijndael加密法可以支持更大范围的区块和密钥长度：AES的区块长度固定为128 比特，" +
                    "密钥长度则可以是128，192或256比特；而Rijndael使用的密钥和区块长度可以是32位的整数倍，以128位为下限，256比特为上限。" +
                    "包括AES-ECB,AES-CBC,AES-CTR,AES-OFB,AES-CFB ", key);
            System.out.println(encryptStr);
            //解密
            String decryptStr = Aes256Util.decode(encryptStr, key);
            System.out.println(decryptStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
