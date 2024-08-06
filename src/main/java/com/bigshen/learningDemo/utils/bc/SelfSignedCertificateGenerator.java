package com.bigshen.learningDemo.utils.bc;

import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import sun.security.x509.X500Name;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * @author byj
 * @date 2024/7/31
 * @Description
 */
public class SelfSignedCertificateGenerator {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

//    public static X509Certificate generateRSACertificate() throws Exception {
//        // 生成密钥对
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
//        keyPairGenerator.initialize(2048);
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        PrivateKey privateKey = keyPair.getPrivate();
//        PublicKey aPublic = keyPair.getPublic();
//        // 证书信息
//        X500Name issuer = new X500Name("CN=Self-Signed CA, O=MyOrg, L=MyCity, ST=MyState, C=MyCountry");
//        X500Name subject = new X500Name("CN=MySite, O=MyOrg, L=MyCity, ST=MyState, C=MyCountry");
//        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
//        Date startDate = new Date();
//        Date endDate = new Date(startDate.getTime() + (365 * 24 * 60 * 60 * 1000L));
//
//        // 构建证书
//        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
//                issuer, serialNumber, startDate, endDate, subject, aPublic);
//
//
//        // 添加扩展
//        certBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
//        certBuilder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.digitalSignature));
//
//        // 签名
//        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA").build(privateKey);
//        X509Certificate certificate = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBuilder.build(contentSigner));
//
//        return certificate;
//    }

//    public static void main(String[] args) throws Exception {
//        X509Certificate certificate = generateRSACertificate();
//        System.out.println("Generated RSA Certificate:");
//        System.out.println(certificate);
//    }
}
