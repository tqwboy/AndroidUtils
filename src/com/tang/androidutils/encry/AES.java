package com.tang.androidutils.encry;

import java.security.SecureRandom;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.tang.androidutils.MyLog;

/**
 * AES加密
 * 
 * @author Hohenheim
 */
public class AES {
	/**
	 * 加密文本
	 * 
	 * @param 需要加密文本的内容
	 * @param secretKeyStr 秘钥
	 * @return 加密后的字节数据
	 */
	public byte[] encrypt(String content, String secretKeyStr) {
		return encrypt(content.getBytes(), secretKeyStr);
	}

	/**
	 * 加密数据
	 * 
	 * @param contentBytes 需要加密的数据
	 * @param secretKeyStr 秘钥
	 * @return 加密后的数据
	 */
	public byte[] encrypt(byte[] contentBytes, String secretKeyStr) {
		byte[] encryptResult = null;

		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
			sr.setSeed(secretKeyStr.getBytes());
			kgen.init(128, sr);

			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

			Cipher cipher = Cipher.getInstance("AES"); // 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, key); // 初始化
			encryptResult = cipher.doFinal(contentBytes);
		}
		catch (Exception e) {
			MyLog.e("Aes", e.toString());
		}

		return encryptResult;
	}

	/**
	 * 使用Base64转码，并兼容C#的AES加密
	 * 
	 * @param sSrc 需要加密的字符串
	 * @param secretKeyStr 秘钥
	 * @return 加密后的字符串
	 */
	public String compatibleCSharpEncrypt(byte[] contentBytes, String secretKeyStr) {
		String encryptResult = null;

		try {
			SecretKeySpec skeySpec = new SecretKeySpec(secretKeyStr.getBytes(), "AES");

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec); // 初始化
			byte[] encryptBytes = cipher.doFinal(contentBytes);

			Base64 base64 = new Base64();
			encryptResult = base64.encodeString(encryptBytes);
		}
		catch (Exception e) {
			MyLog.e("Aes", e.toString());
		}

		return encryptResult;
	}

	/**
	 * 解密
	 * 
	 * @param content 要解密的数据
	 * @param secretKeyStr 秘钥
	 */
	public byte[] decrypt(byte[] content, String secretKeyStr) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
			sr.setSeed(secretKeyStr.getBytes());
			kgen.init(128, sr);

			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

			Cipher cipher = Cipher.getInstance("AES"); // 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key); // 初始化
			byte[] result = cipher.doFinal(content);

			return result; // 解密后的数据
		}
		catch (Exception e) {
			MyLog.e("Aes", e.toString());
		}

		return null;
	}

	/**
	 * 将加密后的字节数据转换为字符串
	 * 
	 * @param data
	 * @return
	 */
	public String parseByte2HexStr(byte data[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			String hex = Integer.toHexString(data[i] & 0xFF);
			if (hex.length() == 1)
				hex = '0' + hex;

			sb.append(hex.toUpperCase(Locale.US));
		}

		return sb.toString();
	}

	/**
	 * 将字符串型密文转换为字节型密文
	 * 
	 * @param hexStr 密文
	 * @return
	 */
	public byte[] parseHexStr2Byte(String hexStr) {
		if (null == hexStr || hexStr.length() < 1)
			return null;

		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}

		return result;
	}
}