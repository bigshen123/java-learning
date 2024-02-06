package com.bigshen.learningDemo.demo.cert;

import java.io.ByteArrayInputStream;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * @author byj
 * @date 2023/12/26
 * @Description
 */
public class CertParseUtil {
    /**
     * 解析base64证书
     * 如果是国密证书，调用该方法前请引入bc库
     * Security.addProvider(new BouncyCastleProvider());
     *
     * @param base64Cert
     * @return
     * @throws CertificateException
     * @throws NoSuchProviderException
     */
    public static X509Certificate parseBase64Cert(String base64Cert) throws CertificateException, NoSuchProviderException {
        byte[] certificateData = Base64.getDecoder().decode(base64Cert);
        // 从字节数组中加载证书
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        try {
            return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certificateData));
        } catch (CertificateException e) {
            // 国密证书无法使用JDK自带的java.security解析证书
            cf = CertificateFactory.getInstance("X.509", "BC");
            return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certificateData));
        }
    }
}
