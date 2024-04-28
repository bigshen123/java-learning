package com.bigshen.learningDemo.javaSE.cert;

/**
 * @Author BYJ
 * @Date 2023/11/25 11:19
 * @Describe
 */
/**
 *
 * 枚举类，实现数字证书对象标识Oid与名称的转换
 */
public enum CertType {

    rsaEncryption("1.2.840.113549.1.1.1", "RSA"),
    sha1withRSAEncryption("1.2.840.113549.1.1.5", "SHA1"),
    ECC("1.2.840.10045.2.1", "ECC"),
    SM2("1.2.156.10197.1.301", "SM2"),
    SM3WithSM2("1.2.156.10197.1.501", "SM3的SM2签名"),
    sha1withSM2("1.2.156.10197.1.502", "SHA1的SM2签名"),
    sha256withSM2("1.2.156.10197.1.503", "SHA256的SM2签名"),
    sm3withRSAEncryption("1.2.156.10197.1.504", "SM3的RSA签名"),
    commonName("2.5.4.3", "主体名"),
    emailAddress("1.2.840.113549.1.9.1", "邮箱"),
    cRLDistributionPoints("2.5.29.31", "CRL分发点"),
    extKeyUsage("2.5.29.37", "扩展密钥用法"),
    subjectAltName("2.5.29.17", "使用者备用名称"),
    CP("2.5.29.32", "证书策略"),
    clientAuth("1.3.6.1.5.5.7.3.2", "客户端认证");


    CertType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String code;

    private String name;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getNameByCode(String code){
        CertType[] values = CertType.values();
        for(CertType plateColorEnum:values){
            if(code.equals(plateColorEnum.getCode())){
                return plateColorEnum.getName();
            }
        }
        return null;
    }

    public static String getCodeByName(String name){
        CertType[] values = CertType.values();
        for(CertType plateColorEnum:values){
            if(name.equals(plateColorEnum.getName())){
                return String.valueOf(plateColorEnum.getCode());
            }
        }
        return null;
    }

}
