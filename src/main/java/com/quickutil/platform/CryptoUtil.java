/**
 * 加解密工具
 * 
 * @class CryptoUtil
 * @author 0.5
 */
package com.quickutil.platform;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {

	private static final String ENCODING = "UTF-8";
	private static final String HmacSHA1 = "HmacSHA1";
	private static final String HmacMD5 = "HmacMD5";
	private static final String SHA1 = "SHA-1";
	private static final String MD5 = "MD5";
	private static Random random = new Random();

	/**
	 * 获取随机md5
	 * 
	 * @return
	 */
	public static String randomMd5Code() {
		return md5Encode((System.currentTimeMillis() + random.nextDouble() + "").getBytes());
	}

	/**
	 * 获取随机sha1
	 * 
	 * @return
	 */
	public static String randomSha1Code() {
		return sha1Encode((System.currentTimeMillis() + random.nextDouble() + "").getBytes());
	}

	/**
	 * 计算md5
	 * 
	 * @param bt-字节数组
	 * @return
	 */
	public static String md5Encode(byte[] bt) {
		try {
			return byte2hex(MessageDigest.getInstance(MD5).digest(bt));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 计算sha1
	 * 
	 * @param bt-字节数组
	 * @return
	 */
	public static String sha1Encode(byte[] bt) {
		try {
			return byte2hex(MessageDigest.getInstance(SHA1).digest(bt));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * hmac-md5签名
	 * 
	 * @param encryptContent-需要签名的内容
	 * @param encryptKey-用于签名的KEY
	 * @return
	 */
	public static String HmacMD5Encrypt(byte[] encryptContent, String encryptKey) {
		try {
			byte[] data = encryptKey.getBytes(ENCODING);
			SecretKey secretKey = new SecretKeySpec(data, HmacMD5);
			Mac mac = Mac.getInstance(HmacMD5);
			mac.init(secretKey);
			byte[] secretbt = mac.doFinal(encryptContent);
			return byte2hex(secretbt);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * hmac-sha1签名
	 * 
	 * @param encryptText-需要签名的内容
	 * @param encryptKey-用于签名的KEY
	 * @return
	 */
	public static String HmacSHA1Encrypt(byte[] encryptContent, String encryptKey) {
		try {
			byte[] data = encryptKey.getBytes(ENCODING);
			SecretKey secretKey = new SecretKeySpec(data, HmacSHA1);
			Mac mac = Mac.getInstance(HmacSHA1);
			mac.init(secretKey);
			byte[] secretbt = mac.doFinal(encryptContent);
			return byte2hex(secretbt);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * byte[]转16进制字符串
	 * 
	 * @param bt-字节数组
	 * @return
	 */
	public static String byte2hex(byte[] bt) {
		if (bt == null)
			return null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bt.length; ++i) {
			int x = bt[i] & 0xFF, h = x >>> 4, l = x & 0x0F;
			sb.append((char) (h + ((h < 10) ? '0' : 'a' - 10)));
			sb.append((char) (l + ((l < 10) ? '0' : 'a' - 10)));
		}
		return sb.toString();
	}

	/**
	 * 16进制字符串转byte[]
	 * 
	 * @param hexString-16进制字符串
	 * @return
	 */
	public static byte[] hex2byte(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] bt = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			bt[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return bt;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 非标准aes加密
	 * 
	 * @param content-需要加密的字节数组
	 * @param password-加密密钥
	 * @return
	 */
	public static byte[] aesEncrypt(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(password.getBytes());
			kgen.init(128, random);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			return cipher.doFinal(content); // 加密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 非标准aes字节数组解密
	 * 
	 * @param content-需要解密的字节数组
	 * @param password-解密密钥
	 * @return
	 */
	public static byte[] aesDecrypt(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(password.getBytes());
			kgen.init(128, random);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			return cipher.doFinal(content); // 解密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 非标准aes字符串加密
	 * 
	 * @param content-需要加密的字符串
	 * @param password-加密密钥
	 * @return
	 */
	public static String aesEncryptStr(String content, String password) {
		return byte2hex(aesEncrypt(content.getBytes(), password));
	}

	/**
	 * 非标准aes字符串解密
	 * 
	 * @param content-需要解密的字符串
	 * @param password-解密密钥
	 * @return
	 */
	public static String aesDecryptStr(String content, String password) {
		return new String(aesDecrypt(hex2byte(content), password));
	}

	/**
	 * 标准aes-ecb加密
	 * 
	 * @param content-需要加密的字节数组
	 * @param password-加密的密钥
	 * @return
	 */
	public static byte[] aesecbEncrypt(byte[] content, String password) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
			SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			return cipher.doFinal(content); // 加密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 标准aes-ecb解密
	 * 
	 * @param content-需要解密的字节数组
	 * @param password-解密的密钥
	 * @return
	 */
	public static byte[] aesecbDecrypt(byte[] content, String password) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
			SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			return cipher.doFinal(content); // 解密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 标准aes-ecb加密
	 * 
	 * @param content-需要加密的字节数组
	 * @param password-加密的密钥
	 * @return
	 */
	public static byte[] aescbcEncrypt(byte[] content, String password, byte[] iv) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
			SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
			return cipher.doFinal(content); // 加密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 标准aes-ecb解密
	 * 
	 * @param content-需要解密的字节数组
	 * @param password-解密的密钥
	 * @return
	 */
	public static byte[] aescbcDecrypt(byte[] content, String password, byte[] iv) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
			SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));// 初始化
			return cipher.doFinal(content); // 解密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * base64字符串转换为byte[]
	 * 
	 * @param baseString-base64字符串
	 * @return
	 */
	public static byte[] base64ToByte(String baseString) {
		try {
			return Base64.getDecoder().decode(baseString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * byte[]转换为base64字符串
	 * 
	 * @param bt-字节数组
	 * @return
	 */
	public static String byteToBase64(byte[] bt) {
		try {
			return Base64.getEncoder().encodeToString(bt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
