package com.bigshen.learningDemo.demo.comparator;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author byj
 * @date 2023/11/27
 * @Description
 */
public class CalculateMD5 {

    private static final ConcurrentHashMap<String, Long> TENANT_MEM_POINTER_MAP = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        // ClassPathResource classPathResource = new ClassPathResource("asset/SUBCA01.crl");
        File file = new File("D:\\NSAG\\crl\\SUBCA01.crl");
//
//        // DigestUtils 加载整个文件计算md5
        long start = System.currentTimeMillis();
        String md51 = calculateMD5(file);
        long end = System.currentTimeMillis();
        System.err.println("cost:" + (end - start));
//        System.out.println("计算md5：" + md51);
//        // 处理大文件时 流式计算md5 减少内存占用
//        String md52 = streamCalculateMD5(file.toPath());
//        System.out.println("流式计算md5：" + md52);

//        InputStream inputStream = new FileInputStream("D:\\IdeaProjects\\learnning-demo\\src\\main\\resources\\asset\\ca.crt");
//        CertificateFactory  certificateFactory  = CertificateFactory.getInstance("X.509");
//        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
//        String name = certificate.getIssuerDN().getName();
//        System.out.println(name);
//        byte[] encoded = certificate.getSubjectX500Principal().getEncoded();
//
//        System.out.println(encoded);
//        BigInteger serialNumber = certificate.getSerialNumber();
//        System.out.println(serialNumber);


        // test();
        // test2();
    }

    private static void test2() {

    }

    private static void test() {
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            try (InputStream in = Files.newInputStream(Paths.get(""))) {
                X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(in);
                String name = certificate.getIssuerDN().getName();
                System.out.println(name);
                byte[] issuer = certificate.getIssuerX500Principal().getEncoded();
                byte[] sn = certificate.getSerialNumber().toString().getBytes();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String calculateMD5(File file) {
        try (InputStream tempCrlIns = Files.newInputStream(file.toPath())) {
            return DigestUtils.md5Hex(tempCrlIns);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    /**
//     * 当处理大文件时，将整个文件读入内存以计算其 MD5 值可能会造成内存压力
//     * 采用流式处理，逐块读取文件内容并计算其 MD5 值，这样可以减少内存占用
//     *
//     * @param filePath 文件路径
//     * @return 十六进制 md5值
//     */
//    private static String streamCalculateMD5(Path filePath) {
//        MessageDigest md;
//        try {
//            md = MessageDigest.getInstance("MD5");
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//        try (InputStream is = Files.newInputStream(filePath);
//             DigestInputStream digestInputStream = new DigestInputStream(is, md)) {
//            Files.copy(digestInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//            byte[] md5Bytes = md.digest();
//            return bytesToHex(md5Bytes);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    /**
//     * 将字节数组转换为十六进制字符串
//     *
//     * @param bytes md5
//     * @return 十六进制
//     */
//    private static String bytesToHex(byte[] bytes) {
//        StringBuilder result = new StringBuilder();
//        for (byte aByte : bytes) {
//            result.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
//        }
//        return result.toString();
//    }
}
