package com.bigshen.learningDemo.utils;

import org.apache.commons.codec.binary.Base64;

import javax.websocket.EncodeException;

/**
 * @author byj
 * @date 2024/7/8
 * @Description
 */
public class CheckEnvelopedKeyBlob {
//    public static void checkEnvelopedKeyBlob() throws EncodeException {
//        // 网关只支持导入数据结构等同于0016-2012规范里SKF_ENVELOPEDKEYBLOB的密钥保护文件
//        // 0016-2012一般为AQAAAA格式
//        String envelopStr="WgAwIBAgIIPfUAPwA3NOgwDAYIKoEcz1UBg3UFADBpMQswCQYDVQQGEwJDTjE8MDoGA1UECgwzTmF0aW9uYWwgRS1Hb3Zlcm5tZW50IE5ldHdvcmsgQWRtaW5pc3RyYXRpb24gQ2VudGVyMRwwGgYDVQQDDBNDRUdOIFNNMiBDbGFzcyAyIENBMB4XDTI0MDcwNTA4MzU1M1oXDTI5MDcwNDA4MzU1M1owgZcxCzAJBgNVBAYTAkNOMRIwEAYDVQQIDAnkupHljZfnnIExEjAQBgNVBAcMCeWkp";
//        byte[] envelopedKeyBlob = checkValidEnvelopedKeyBlob(envelopStr.getBytes());
//        if (is0016EnvelopedKey(envelopedKeyBlob)){
//            System.out.println("1");
//        }
//        if (Base64.isBase64(envelopedKeyBlob)) {
//            envelopedKeyBlob = Base64.decodeBase64(envelopedKeyBlob);
//        }
//
//        OsccaSM2EnvelopedBlob osccaSM2EnvelopedBlob;
//        try {
//            osccaSM2EnvelopedBlob = OsccaSM2EnvelopedBlob.valueOf(envelopedKeyBlob, false);
//        } catch (Exception e) {
//            throw new ApiException(400, "解析ECC加密密钥对保护结构异常", e);
//        }
//        //对称加密后的加密证书私钥
//        byte[] encryptedPrvKeyBytes = osccaSM2EnvelopedBlob.getEncryptedPrvKey();
//        //加密证书公钥
//        byte[] encPubKeyBytes = osccaSM2EnvelopedBlob.getPubKeyBytes();
//        //签名证书公钥加密后的对称密钥
//        byte[] encryptedSymmKey = osccaSM2EnvelopedBlob.getEncryptedSymmKey();
//
//        //签名私钥解密对称密钥
//        SM2CipherStruct sm2CipherStruct;
//        try {
//            byte[] bytes = Decoder.parseEncryptedData2SM2CipherData(encryptedSymmKey);
//            sm2CipherStruct = new SM2CipherStruct();
//            sm2CipherStruct.decode(bytes);
//        } catch (Exception e) {
//            throw new ApiException(400, "解析SM2加密块结构异常", e);
//        }
//    }
//
//    public static void main(String[] args) throws EncodeException {
//        checkEnvelopedKeyBlob();
//    }
}
