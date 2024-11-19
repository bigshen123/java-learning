package com.bigshen.learningDemo.utils.pqc;

import kl.nbase.emengine.conf.EngineConfig;
import kl.nbase.emengine.conf.file.FileConfig;
import kl.nbase.emengine.entity.param.CommonAsymSignParam;
import kl.nbase.emengine.service.ClusterEngine;
import kl.nbase.security.entity.algo.AsymAlgo;
import kl.nbase.security.entity.algo.HashAlgo;
import kl.nbase.security.entity.data.impl.OriginalData;
import kl.nbase.security.entity.data.impl.SignatureData;
import kl.nbase.security.entity.key.asym.pri.impl.SecurityPrivateKey;
import kl.nbase.security.entity.param.crypto.asym.sign.IAsymSignParam;
import kl.nbase.security.entity.param.crypto.asym.sign.impl.SM2SignParam;
import kl.nbase.security.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author byj
 * @date 2024/11/15
 * @Description
 */
public class PQCDemo {

    public static final String originData = "pqc demo";
    public static ClusterEngine engine = initEngine();

    public static void main(String[] args) {

        // SM2
        System.out.println("--------------------开始SM2 begin--------------------");
        testSm2();
        System.out.println("--------------------SM2 end--------------------");

        // Dilithium3
        System.out.println("--------------------Dilithium3 begin--------------------");
        testDilithium3();
        System.out.println("--------------------Dilithium3 end--------------------");

    }

    private static void testDilithium3() {
        // Dilithium3 算法
        AsymAlgo algo = AsymAlgo.DILITHIUM3;
        KeyPair keypair = engine.genKeyPair(algo);
        PublicKey dilithium3PublicKey = keypair.getPublic();
        PrivateKey dilithium3PrivateKey = keypair.getPrivate();
        System.out.println("dilithium3 pubKey: " + Hex.toHexString(dilithium3PublicKey.getEncoded()));
        System.out.println("dilithium3 pubKey length: " + Hex.toHexString(dilithium3PublicKey.getEncoded()));
        System.out.println("dilithium3 priKey: " + dilithium3PublicKey.getEncoded().length);
        System.out.println("dilithium3 priKey: length" + dilithium3PrivateKey.getEncoded().length);

        // Dilithium3 签名
        long start = System.currentTimeMillis();
        SecurityPrivateKey securityPrivateKey = new SecurityPrivateKey(dilithium3PrivateKey);
        IAsymSignParam<SignatureData> signParam = new CommonAsymSignParam().getSignParam(securityPrivateKey.getAlgo());
        SignatureData signatureData = engine.sign(securityPrivateKey, OriginalData.wrap(originData.getBytes(StandardCharsets.UTF_8)), signParam);
        long end = System.currentTimeMillis();
        System.out.println("dilithium3 signTime：" + (end - start) + "ms");
        System.out.println("dilithium3 signed data: " + Hex.toHexString(signatureData.getSignedData()));
        System.out.println("dilithium3 signLength：" + signatureData.getSignedData().length);


        // Dilithium3 校验签名
        start = System.currentTimeMillis();
        boolean verifyResult = engine.verify(dilithium3PublicKey, originData.getBytes(StandardCharsets.UTF_8), signatureData.getSignedData(), null, null);
        end = System.currentTimeMillis();
        System.out.println("dilithium3 verifyTime：" + (end - start) + "ms");
        System.out.println("dilithium3 verify result: " + verifyResult);
    }

    private static void testSm2() {
        AsymAlgo hssAlgo = AsymAlgo.SM2;
        KeyPair sm2Keypair = engine.genKeyPair(hssAlgo);
        PublicKey sm2PublicKey = sm2Keypair.getPublic();
        PrivateKey sm2PrivateKey = sm2Keypair.getPrivate();
        System.out.println("sm2 pubKey: " + Hex.toHexString(sm2PublicKey.getEncoded()));
        System.out.println("sm2 pubKey length: " + sm2PublicKey.getEncoded().length);
        System.out.println("sm2 priKey: " + Hex.toHexString(sm2PrivateKey.getEncoded()));
        System.out.println("sm2 priKey length: " + sm2PrivateKey.getEncoded().length);

        // SM2签名
        long start = System.currentTimeMillis();
        SecurityPrivateKey sm2SecurityPrivateKey = new SecurityPrivateKey(sm2PrivateKey);
        IAsymSignParam<SignatureData> sm2SignParam = new SM2SignParam(HashAlgo.SM3, null, null);
        SignatureData sm2SignatureData = engine.sign(sm2SecurityPrivateKey, OriginalData.wrap(originData.getBytes(StandardCharsets.UTF_8)), sm2SignParam);
        long end = System.currentTimeMillis();
        System.out.println("SM2 signTime：" + (end - start) + "ms");
        System.out.println("sm2 signed data: " + Hex.toHexString(sm2SignatureData.getSignedData()));

        // SM2校验签名
        start = System.currentTimeMillis();
        boolean sm2VerifyResult = engine.verify(sm2PublicKey, originData.getBytes(StandardCharsets.UTF_8), sm2SignatureData.getSignedData(), HashAlgo.SM3, null);
        System.out.println("sm2 verifyTime：" + (end - start) + "ms");
        System.out.println("sm2 verify result: " + sm2VerifyResult);
    }

    private static ClusterEngine initEngine() {
        EngineConfig fileEngineConfig = new EngineConfig();
        fileEngineConfig.setEngineType("FileEngine");
        fileEngineConfig.setFileConfig(new FileConfig().setGenerateJks(false));
        return new ClusterEngine(fileEngineConfig);
    }
}
