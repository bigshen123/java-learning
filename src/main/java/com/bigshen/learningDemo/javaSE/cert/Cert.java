package com.bigshen.learningDemo.javaSE.cert;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author BYJ
 * @Date 2023/11/25 11:20
 * @Describe 参考 <a href="https://blog.csdn.net/yyb1369584682/article/details/132064639">...</a>
 *
 * {
 *   "code": 200,
 *   "data": {
 *     "effDate": "2023-07-03 17:44:47",
 *     "serialNumber": "00a4cbbc64a19f000003",
 *     "issuerDN": "CN=1111, C=CN",
 *     "type": "EC",
 *     "expDate": "2024-07-02 17:44:47",
 *     "subjectDN": "CN=52000000121320000000_5200000012, O=123, O=00, L=123, L=123, C=CN",
 *     "sigAlgName": "SM3的SM2签名"
 *   },
 *   "msg": "成功"
 * }
 */
public class Cert {

    private static final String CERT_PATH = "D:\\NSAG\\cert_20231125112941\\c51b7b33a57795954481f7d29d2bc701601adb43\\KoalCa_RSA.cer";
    private static final String CERT_TYPE = "X.509";

    public static void main(String[] args) throws IOException, CertificateException {
       // Object cert = getCert();
       // System.out.println(cert);
        getCertInfo();
    }

    public static void getCertInfo() throws IOException, CertificateException {
        File file = new File(CERT_PATH);
        InputStream inputStream = Files.newInputStream(file.toPath());

        CertificateFactory cf = CertificateFactory.getInstance(CERT_TYPE);
        X509Certificate oCert = (X509Certificate)cf.generateCertificate(inputStream);
        System.out.println("【输出证书信息】:\n"+oCert.toString());
        //主体部分
        System.out.println("【版本号】:"+oCert.getVersion());
        System.out.println("【序列号】:"+ oCert.getSerialNumber().toString(16));
        System.out.println("【签名算法】："+oCert.getSigAlgName() + "(" + oCert.getSigAlgOID() + ")");
        System.out.println("【主体名】："+oCert.getSubjectX500Principal());
        System.out.println("【主体唯一标识符】："+oCert.getSubjectUniqueID());
        System.out.println("【颁发者】：" + oCert.getIssuerX500Principal());
        System.out.println("【颁发者唯一标识符】：" + oCert.getIssuerUniqueID());
        System.out.println("【有效期起始】："+oCert.getNotBefore());
        System.out.println("【有效期结束】："+oCert.getNotAfter());
        System.out.println("【公钥】：\n  "+ oCert.getPublicKey());
        System.out.println("【公钥算法】："+ oCert.getPublicKey().getAlgorithm());
        System.out.println("【公钥格式】："+oCert.getPublicKey().getFormat());

        PublicKey publicKey = oCert.getPublicKey();
        System.out.println("-----------------公钥--------------------");
        System.out.println(publicKey.toString());
        System.out.println("-----------------公钥--------------------");
    }
    /**
     * 获取证书对象
     *
     * @return 证书详情
     */
    public static Object getCert() {
        String filePath = "D:\\NSAG\\cert_20231125112941\\c51b7b33a57795954481f7d29d2bc701601adb43";
        String fileName = "KoalCa_RSA.cer";
        filePath = filePath + File.separator + fileName;
        JSONObject jsonObject = new JSONObject();
        CertificateFactory cf;
        X509Certificate cert;
        try {
            cf = CertificateFactory.getInstance("X.509");
            FileInputStream in = new FileInputStream(filePath);
            cert = (X509Certificate) cf.generateCertificate(in);
        } catch (CertificateException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //签发者
        String subjectDN = cert.getSubjectDN().toString();
        //使用者
        String issuerDN = cert.getIssuerDN().toString();
        //序列号（十进制转十六进制，左补零）
        String serialNumber = String.format("%" + 20 + "s", cert.getSerialNumber().toString(16)).replace(' ', '0');
        //生效时间
        Date effDate = cert.getNotBefore();
        //过期时间
        Date expDate = cert.getNotAfter();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String effStr = null;
        String expStr = null;
        try {
            effStr = sdf.format(effDate);
            expStr = sdf.format(expDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //秘钥类型
        String type = cert.getPublicKey().getAlgorithm();
        //签名哈希算法
        String sigAlgName = cert.getSigAlgName();

        jsonObject.put("subjectDN", subjectDN);
        jsonObject.put("issuerDN", issuerDN);
        jsonObject.put("serialNumber", serialNumber);
        jsonObject.put("effDate", effStr);
        jsonObject.put("expDate", expStr);
        jsonObject.put("type", type);
        jsonObject.put("sigAlgName", CertType.getNameByCode(sigAlgName));
        return jsonObject;
    }
}
