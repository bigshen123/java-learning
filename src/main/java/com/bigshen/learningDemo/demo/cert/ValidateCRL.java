package com.bigshen.learningDemo.demo.cert;


import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;

/**
 * @author byj
 * @date 2023/12/4
 * @Description 验证CRL，检查证书是否被吊销
 */
public class ValidateCRL {

    public static void main(String[] args) throws Exception {
        String crlPath = "D:\\IdeaProjects\\learnning-demo\\src\\main\\resources\\asset\\my_ca.crl";

//		创建CRL对象
        X509CRL crl = loadX509CRL(crlPath);

//		创建包含可用和被吊销的两个证书对象的数组
        String availCertPath = "D:\\IdeaProjects\\learnning-demo\\src\\main\\resources\\asset\\server127.crt";
        X509Certificate[] certArray = new X509Certificate[1];
        certArray[0] = getCertificate(availCertPath);

//		验证证书是否被吊销
        for (int i = 0; i < certArray.length; i++) {
            System.out.println("证书序列号=" + getSerialNumber(certArray[i]));
            System.out.println("证书DN=" + certArray[i].getSubjectDN());
            if (crl.isRevoked(certArray[i])) {
                System.out.println("证书被吊销\n");
            } else {
                System.out.println("证书可用\n");
            }
        }
    }

    /**
     * 加载CRL证书吊销列表文件
     *
     * @param crlFilePath
     * @return
     * @throws Exception
     */
    public static X509CRL loadX509CRL(String crlFilePath) throws Exception {
        FileInputStream in = new FileInputStream(crlFilePath);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509CRL crl = (X509CRL) cf.generateCRL(in);
        crl.getSignature();
        String name = crl.getIssuerX500Principal().getName();
        String name1 = crl.getIssuerDN().getName();
        in.close();
        return crl;
    }

    /**
     * 加载证书文件
     *
     * @param certFilePath
     * @return
     * @throws Exception
     */
    public static X509Certificate getCertificate(String certFilePath) throws Exception {
        FileInputStream in = new FileInputStream(certFilePath);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(in);
        return cert;
    }

    /**
     * 读取证书序列号
     *
     * @param cert
     * @return
     */
    public static String getSerialNumber(X509Certificate cert) {
        if (null == cert) {
            return null;
        }
        byte[] serial = cert.getSerialNumber().toByteArray();
        if (serial.length > 0) {
            StringBuilder serialNumberString = new StringBuilder();
            for (int i = 0; i < serial.length; i++) {
                String s = Integer.toHexString(Byte.valueOf(serial[i]).intValue());
                if (s.length() == 8) {
                    s = s.substring(6);
                } else if (1 == s.length()) {
                    s = "0" + s;
                }
                serialNumberString.append(s).append(" ");
            }
            return serialNumberString.toString();
        }
        return null;
    }

}
