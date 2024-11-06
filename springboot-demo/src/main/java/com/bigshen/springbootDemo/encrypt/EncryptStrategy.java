package com.bigshen.springbootDemo.encrypt;

/**
 * @author byj
 * @date 2024/10/22
 * @Description
 */
public interface EncryptStrategy {
    String getNameKey() ;

    String getPwdKey() ;
    String encrypt(String originalValue, String key);

    Boolean support(int type);
}
