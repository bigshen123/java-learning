package com.bigshen.learningDemo.utils.encrypt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;


/**
 * 对称加解密 AES算法
 */
public class AES256Helper {

	  private static final Log log = LogFactory.getLog(AES256Helper.class);
	
	/** 成员变量：加密算法 */
	public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

	/** 成员变量：密钥 */
	private static final String sKey = ")O[NB]6,YF}+efcaj{+oESb9d8>Z'e9M";

	/** 成员变量：初始化向量 */
	private static final String ivParameter = "L+\\~f4,Ir)b$=pkf";

	public static final List<String> files = new ArrayList<>();

	// 原始文件
	public static final List<String> originalFiles = new ArrayList<>();

	public static byte[] keyByte = null;
	
//	static{
//		try {
//			keyByte = AES256Helper.generateKeyByte();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (InvalidKeySpecException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * 加密
	 * 
	 * @param data
	 *            明文
	 * @param key
	 *            密钥
	 * @return 密文
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		return cipher.doFinal(data);
	}

	/**
	 * 加密
	 * 
	 * @param data
	 *            明文
	 * @return 密文
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data) throws Exception {


		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
		SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(), "AES");
		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		return cipher.doFinal(data);
	}

	/**
	 * 解密
	 * 
	 * @param data
	 *            密文
	 * @param key
	 *            密钥
	 * @return 明文
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		return cipher.doFinal(data);
	}

	/**
	 * 解密
	 * 
	 * @param data
	 *            密文
	 * @return 明文
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data) throws Exception {


		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
		SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(), "AES");
		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		return cipher.doFinal(data);
	}

	public static byte[] generateKeyByte() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException {
		
		String rootDir = "/opt";
		String osName = System.getProperty("os.name");
		String fileSeparator = System.getProperty("file.separator");
		if(osName.startsWith("Windows")){
			rootDir = "D:";
		}
		String dirName = rootDir + fileSeparator + "dssystem" + fileSeparator + "fileconfig" + fileSeparator;
		
		List<String> locations = new ArrayList<>();
		locations.add(dirName + "file1" + fileSeparator + "file1.conf");
		locations.add(dirName + "file2" + fileSeparator + "file2.conf");
		locations.add(dirName + "file3" + fileSeparator + "file3.conf");
		locations.add(dirName + "file4" + fileSeparator + "file4.conf");
		locations.add(dirName + "file5" + fileSeparator + "file5.conf");

		files.clear();
		originalFiles.clear();

		List<int[]> bytes = new ArrayList<>();
		for (String location : locations) {
			FileInputStream fis = null;
			InputStreamReader reader = null;
			BufferedReader br = null;

			try {
				String hexString = "";
				fis = new FileInputStream(location);
				reader = new InputStreamReader(fis, "utf-8");
				br = new BufferedReader(reader);

				String line;
				while ((line = br.readLine()) != null) {
					hexString += line;
				}

				//去除utf-8文本中开头可能存在的BOM
				if(hexString.startsWith("\uFEFF")){
					hexString = hexString.substring(1);
				}

				originalFiles.add(hexString);
				files.add(location + ": " + EncrytionUtil.SHA256(hexString));

				int[] vals = KeyUtil.hexStringToInt(hexString);
				bytes.add(vals);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				if(log.isErrorEnabled()){
					log.error("Config File Not Found!");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
					fis.close();
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


		String keyChar = KeyUtil.xorInt(bytes);
		byte[] keyByte = KeyUtil.getPBKDF2(keyChar);

		return keyByte;
	}

	public static String formatLog(String message, String type){

		String user = System.getProperty("user.name");
		String ip = "127.0.0.1";

		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return String.format("[%s][%s][%s][%s]", ip, user, type, message);
	}

	/*
	 * public static byte[] generateDesKey(int length) throws Exception { //实例化
	 * KeyGenerator kgen = null; kgen = KeyGenerator.getInstance("AES");
	 * //设置密钥长度 kgen.getCategory(length); //生成密钥 SecretKey skey = kgen.generateKey();
	 * //返回密钥的二进制编码 return skey.getEncoded(); }
	 */

	public static void main(String[] args) throws Exception {
		String str = "dsportal";
		String mykey = ")O[NB]6,YF}+ef90j{+oESb9d8>Z'e9M";

		System.out.println(mykey.length());
		// 打印原文
		System.out.println("原文：" + str);
		// 密钥
		byte[] key;
		try {
			// 生成随机密钥
			key = mykey.getBytes();
			// 打印密钥
			System.out.print("密钥：");
			for (int i = 0; i < key.length; i++) {
				System.out.printf("%x", key[i]);
			}
			System.out.print("\n");
			// 加密
			byte[] data = AES256Helper.encrypt(str.getBytes());
			// byte[] data = AES256Helper.encrypt(str.getBytes(),key);
			// 打印密文
			System.out.print("加密后：");
			// hXJvVNVWgpZe+68v+DCZnA==
			//System.out.println(new BASE64Encoder().encode(data));
			// 294f5b4e425d362c59467d2b656639306a7b2b6f4553623964383e5a2765394d
			char[] testChar = Base64Helper.encode(data);
			System.out.println(Base64Helper.encode(data));

			String test = String.valueOf(testChar);
			// String test = "qLu6l0ZdQruoVX62LhHqvA==";
			byte[] test_data = Base64Helper.decode(test.toCharArray());
			// 解密密文
			data = AES256Helper.decrypt(test_data);
			// data = AES256Helper.decrypt(test_data, key);
			// 打印原文
			System.out.println("解密后：" + new String(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
