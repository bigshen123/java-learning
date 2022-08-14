package com.bigshen.learningDemo.utils.encrypt;

import org.apache.commons.collections.CollectionUtils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;

public class KeyUtil {

	public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

	public static final String SALT = "hegh@dscomm.com.lb@dscomm.com.cn";

	// 生成密文的长度
	public static final int HASH_SIZE = 256;

	// 迭代次数
	public static final int PBKDF2_ITERATIONS = 1;

	/**
	 * 根据password和salt生成密文
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public static byte[] getPBKDF2(String plainText)
			throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
		// 将16进制字符串形式的salt转换成byte数组
		// byte[] bytes = DatatypeConverter.parseHexBinary(salt);
		byte[] bytes = SALT.getBytes("UTF-8");
		KeySpec spec = new PBEKeySpec(plainText.toCharArray(), bytes, PBKDF2_ITERATIONS, HASH_SIZE);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
		byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();
		// 将byte数组转换为16进制的字符串
		// System.out.println("PBKDF2 hexString:" + DatatypeConverter.printHexBinary(hash));
		return hash;
	}

	public static int[] xor(int[] b1, int[] b2) {

		int length = b1.length;

		int rb[] = new int[length];
		int i = 0;
		for (; i < length; i++) {
			rb[i] = (byte) (b1[i] ^ b2[i]);
		}
		
		return rb;
	}
	
	public static String xorInt(List<int[]> bytes) {

		if (CollectionUtils.isEmpty(bytes)) {
			return null;
		}

		int length = bytes.get(0).length;

		int[] rb = new int[length];

		for (int i = 0; i < bytes.size() - 1; i++) {
			rb = xor(bytes.get(i), bytes.get(i + 1));
			bytes.set(i + 1, rb);
		}

		return intToHexString(rb);
	}

	/**
	 * 十六进制字符串转换为整型
	 * @param hex
	 * @return
	 */
	public static int[] hexStringToInt(String hex){
		int[] array = new int[hex.length() / 2];
		for (int i = 0; i < array.length; i++) {
			String var1 = "0x" + hex.substring(2 * i, 2 * i + 2);
			array[i] = Integer.decode(var1);
		}
		return array;
	}
	
	/**
	 * 整型转换为十六进制字符串
	 * @param array
	 * @return
	 */
	public static String intToHexString(int[] array){
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int i = 0; i < array.length; i++) {
			if(array[i] < 16 && array[i] >= 0){
				stringBuilder.append("0");
			}
			byte b = Byte.valueOf((byte)array[i]);
			stringBuilder.append(Integer.toHexString(b & 0xff));
		}
		
		return stringBuilder.toString();
	}
}
