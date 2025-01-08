package com.bigshen.learningDemo.jvm.commandLine;

/**
 * @Author BYJ
 * @Date 2024/4/24 20:44
 * @Describe
 */
public class ArgsMain {
    public static void main(String[] args) {
        if (args.length == 1 && ("--help".equals(args[0]) || "-h".equals(args[0]))) {
            System.out.println("Usage: java -cp packageName class args...");
            // java -cp  ucs-sdk-cmd-1.0.0-SNAPSHOT-jar-with-dependencies.jar test.TestPkcs1Sign -host 10.2.5.241 -port  51001 -ak 54121fa55f2c4075836bcd0d79e33b6b -sk d7cbf4c7905b43a58dfc629f93c71868 -trace 123 -signKeyValue gaodq_svs_sm2 -origin koal1234
            System.out.println("       eg: java -cp ucs-sdk-1.0.0-SNAPSHOT.jar test.TestPkcs1Sign -host 10.2.5.241 -port  51001 ...");
            System.out.println();
            System.out.println("where class include:");
            System.out.println("test.TestPkcs1Sign                     --p1签名");
            System.out.println("test.TestPkcs1Verify                   --p1验签");
            System.out.println("test.TestPkcs7Sign                     --p7签名");
            System.out.println("test.TestPkcs7Verify                   --p7验签");
            System.out.println("test.TestRawEncrypt                    --裸加密");
            System.out.println("test.TestRawDecrypt                    --裸解密");
            System.out.println("test.TestEncrypt                       --数字信封加密");
            System.out.println("test.TestDecrypt                       --数字信封解密");
            System.out.println();
            System.out.println("where args include:");
            System.out.println("-host                                  --服务ip");
            System.out.println("-port                                  --服务端口");
            System.out.println("-ak                                    --ak");
            System.out.println("-sk                                    --sk");
            System.out.println("-authMode                              --akskAuthMode(1-AK, 2-FastAkSk, 3-AKSK)(默认1)");
            System.out.println("-keyPin                                --私钥授权码");
            System.out.println("-symAlgo                               --对称加密算法(AES, DES, DESEDE, DESEDE3, SM1, SM4, SSF33)");
            System.out.println("-workMode                              --对称加密模式(CBC, ECB, GCM)");
            System.out.println("--traceId, -trace                      --traceId");
            System.out.println("--originDataType, -oriType             --原文类型(Original, Digest)");
            System.out.println("--signKeyLookupType, -signKeyType      --签名/验签证书查询类型(CertId, CertDn, CertSn, CertAlias, Cert, KeyIndex, KeyId, Key)");
            System.out.println("--signKeyLookupValue, -signKeyValue    --签名/验签证书查询条件");
            System.out.println("--encKeyLookupType, -encKeyType        --加密/解密证书查询类型(CertId, CertDn, CertSn, CertAlias, Cert, KeyIndex, KeyId, Key)");
            System.out.println("--encKeyLookupValue, -encKeyValue      --加密/解密证书查询条件");
            System.out.println("--sm2UserId, -u                        --sm2UserId");
            System.out.println("--originData, -origin                  --原文");
            System.out.println("--signedData, -sign                    --签名数据");
            System.out.println("--encryptedData, -enc                  --加密(数字信封)数据");
            System.out.println("--digestAlg, -digest                   --摘要算法(Sm3, Sha1, Sha256, Sha384, Sha512, Md5, Raw, Default)");
            System.out.println("--signMessageMode, -signMode           --p7签名类型(Attach, Detach)");
            System.out.println("--cipherPadding, -padding              --填充模式(NoPadding, ZeroBytePadding, PKCS5Padding, PKCS7Padding, X923)");
            System.out.println("--b64Iv, -iv                           --偏移量");
            System.out.println("--withAuthAttr, -attr                  --是否验证(0-否, 1-是)(默认0)");
            System.out.println("--energySupplier, -es                  --算力提供(Cpu, HardDevice)");
            System.out.println("-certChainCheck                        --证书链检查(0-否 1-是)(默认0)");
            System.out.println("-whiteCertCheck                        --证书白名单检查(0-否 1-是)(默认0)");
            System.out.println("-timeValidCheck                        --证书时间有效性检查(0-否 1-是)(默认0)");
            System.out.println("-blackCertCheck                        --证书黑名单检查(0-否 1-是)(默认0)");
            System.out.println("-verifyCertCheck                       --检查证书(0-否 1-是)(默认0)");
            System.out.println("-sm2SignatureFormat                    --SM2签名格式");
            System.out.println("-sm2CipherFormat                       --SM2加密格式");
        }
    }
}
