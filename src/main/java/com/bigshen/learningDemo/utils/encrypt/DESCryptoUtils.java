package com.bigshen.learningDemo.utils.encrypt;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName  DESCrypto
 * @Description: xml加密解密，DES加密
 * @Author: byj
 * @Date: 2020/1/14
 */
public class DESCryptoUtils {
    public static void main(String[] args) {
        //需要加密的xml文件
        String filename = "E:\\IdeaProjects\\ds-encryption-service\\src\\main\\resources\\ds-portal-publish-configuration.xml";
        //加密后的输出路径
        String inputFile="E:\\AA\\ds-portal-publish-configuration01.xml";
        //解密后的输出路径
        String outputFile="E:\\AA\\ds-portal-publish-configuration02.xml";
        //加密解密方法
        Map<String,byte[]> map=new HashMap<String, byte[]>();
        byte[] bytes = sCrypto(filename, inputFile,map);
        for (Map.Entry<String, byte[]> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(Arrays.toString(entry.getValue()));
        }
        dESCrypto(inputFile,outputFile,bytes);

    }

    /**
     * DES进行加密解密操作
     * @param filename 需要加密解密的xml文件
     * @param inputFile 加密后输出路径
     */
    private static byte[] sCrypto(String filename,String inputFile, Map<String,byte[]> map){
        KeyGenerator kg;
        byte[] result=null;
        String name = filename.substring(filename.lastIndexOf("\\") + 1);
        try {
            kg = KeyGenerator.getInstance("DES");
            //生成密钥的位数
            kg.init(56);
            // 产生密钥
            Key key = kg.generateKey();
            System.out.println("Key format: " + key.getFormat());//RAW
            System.out.println("Key algorithm: " + key.getAlgorithm());//DES
            // 加密用Cipher来实现
            Cipher cipher = Cipher.getInstance("DES");
            System.out.println("Cipher provider: " + cipher.getProvider());//SunJCE version 1.6
            System.out.println("Cipher algorithm: " + cipher.getAlgorithm());//DES

            byte[] data = "Hello World!".getBytes();
            System.out.println("Original data : [" + data.length + "]"
                    + new String(data));//[12]Hello World!

            // 设置加密模式
            cipher.init(Cipher.ENCRYPT_MODE, key);
            result = cipher.doFinal(data);
            map.put(name,result);
            System.out.println("Encrypted data: [" + result.length + "]"
                    + new String(result));//[16]C澵K墙憇`禃??


            // 读入并加密文件
            try {
                // 输入流
                cipher.init(Cipher.ENCRYPT_MODE, key);
                BufferedInputStream in = new BufferedInputStream(
                        new FileInputStream(filename));
                // 输出流
                CipherOutputStream out = new CipherOutputStream(
                        new BufferedOutputStream(new FileOutputStream(inputFile)), cipher);
                int i;
                do {
                    i = in.read();
                    if (i != -1)
                        out.write(i);
                } while (i != -1);

                in.close();
                out.close();
                System.out.println("加密文件完成！");
            } catch (Exception ey5) {
                System.out.println("Error when encrypt the file");
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param inputFile   需要解密的文件路径
     * @param outputFile 解密后输出路径
     * @param result    加密文件字节数组
     */
    private static void dESCrypto(String inputFile,String outputFile,byte[] result){
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance("DES");
            //生成密钥的位数
            kg.init(56);
            // 产生密钥
            Key key = kg.generateKey();
            System.out.println("Key format: " + key.getFormat());//RAW
            System.out.println("Key algorithm: " + key.getAlgorithm());//DES
            Cipher cipher = Cipher.getInstance("DES");
            // 设置解密模式
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] original = cipher.doFinal(result);
            System.out.println("Decrypted data: [" + original.length + "]"
                    + new String(original));//[12]Hello World!
            try {
                cipher.init(Cipher.DECRYPT_MODE, key);
                // 输出流
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(outputFile));
                // 输入流
                CipherInputStream in = new CipherInputStream(
                        new BufferedInputStream(new FileInputStream(
                                inputFile)), cipher);
                int i;
                do {
                    i = in.read();
                    if (i != -1)
                        out.write(i);
                } while (i != -1);

                in.close();
                out.close();
                System.out.println("解密文件完成！");
            } catch (Exception ey5) {
                System.out.println("Error when encrypt the file");
                System.exit(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

