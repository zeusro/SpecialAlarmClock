package com.gdtel.eshore.androidframework.common.util.security;

import java.io.ByteArrayOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Des3Util {
	private static final String Algorithm = "DESede"; // 定义 加密算法,可用
														// DES,DESede,Blowfish

	private static final String hexString = "0123456789ABCDEF";
	private static final String ENCODING_UTF8 = "utf-8";

	// byte数组(用来生成密钥的)

	final static byte[] keyBytes = { 0x11, 0x22, 0x4F, 0x58, (byte) 0x88, 0x10,

	0x40, 0x38, 0x28, 0x25, 0x79, 0x51, (byte) 0xCB, (byte) 0xDD,

	0x55, 0x66, 0x77, 0x29, 0x74, (byte) 0x98, 0x30, 0x40, 0x36,

	(byte) 0xE2 };

	// 字节码转换成16进制字符串
	public static String byte2hex(byte bytes[]) {
		StringBuffer retString = new StringBuffer();
		for (int i = 0; i < bytes.length; ++i) {
			retString.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF))
					.substring(1));
		}
		return retString.toString();
	}

	// 将16进制字符串转换成字节码
	public static byte[] hex2byte(String hex) {
		byte[] bts = new byte[hex.length() / 2];
		for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2),
					16);
		}
		return bts;
	}

	/**
	 * 加密
	 * @param src
	 *            字节数组(根据给定的字节数组构造一个密钥。 )
	 * @return
	 */

	public static String encryptMode(String src) {

		try {

			// 根据给定的字节数组和算法构造一个密钥

			SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);

			// 加密

			Cipher c1 = Cipher.getInstance(Algorithm);

			c1.init(Cipher.ENCRYPT_MODE, deskey);

			return byte2hex(c1.doFinal(src.getBytes(ENCODING_UTF8))); // 返回16进制的密文

		} catch (java.security.NoSuchAlgorithmException e1) {

			e1.printStackTrace();

		} catch (javax.crypto.NoSuchPaddingException e2) {

			e2.printStackTrace();

		} catch (java.lang.Exception e3) {

			e3.printStackTrace();

		}

		return null;

	}

	/**
	 * 
	 * 解密
	 * 
	 * 
	 * @param src
	 *            需要解密的数据
	 * 
	 * @return
	 */

	public static String decryptMode(String src) {
		try {

			// 生成密钥

			SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);

			// 解密

			Cipher c1 = Cipher.getInstance(Algorithm);

			c1.init(Cipher.DECRYPT_MODE, deskey);

			return new String(c1.doFinal(hex2byte(src)),ENCODING_UTF8);

		} catch (java.security.NoSuchAlgorithmException e1) {

			e1.printStackTrace();

		} catch (javax.crypto.NoSuchPaddingException e2) {

			e2.printStackTrace();

		} catch (java.lang.Exception e3) {

			e3.printStackTrace();

		}

		return null;

	}

	/**
	 * 
	 * 字符串转为16进制
	 * 
	 * @param str
	 * 
	 * @return
	 */

	public static String encode(String str)

	{

		// 根据默认编码获取字节数组

		byte[] bytes = str.getBytes();

		StringBuilder sb = new StringBuilder(bytes.length * 2);

		// 将字节数组中每个字节拆解成2位16进制整数

		for (int i = 0; i < bytes.length; i++)

		{

			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));

			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));

		}

		return sb.toString();

	}

	/**
	 * 
	 * 
	 * 
	 * @param bytes
	 * 
	 * @return
	 * 
	 *         将16进制数字解码成字符串,适用于所有字符（包括中文）
	 */

	public static String decode(String bytes)

	{

		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				bytes.length() / 2);

		// 将每2位16进制整数组装成一个字节

		for (int i = 0; i < bytes.length(); i += 2)

			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
					.indexOf(bytes.charAt(i + 1))));

		return new String(baos.toByteArray());

	}

	// / <summary>
	// / 把字符串转换为字节数组
	// / </summary>
	// / <param name="strKey">源串</param>
	public static byte[] covertStringToHex(String strKey) {
		int strLen = strKey.length();
		// 字节长度是字符串的一半
		int byLen = strKey.length() >> 1;
		char[] strKeyAss = strKey.toCharArray();
		byte[] key = new byte[byLen];
		for (int i = 0, j = 0; i < byLen && j < strLen; i++) {
			key[i] = (byte) (convertCharToHex(strKeyAss[j++]) << 4);
			key[i] += (byte) (convertCharToHex(strKeyAss[j++]) & 0xF);
		}
		return key;
	}

	// / <summary>
	// /
	// / </summary>
	// / <param name="ch"></param>
	// / <returns></returns>
	public static int convertCharToHex(char ch) {
		if ((ch >= '0') && (ch <= '9'))
			return ch - 0x30;
		else if ((ch >= 'A') && (ch <= 'F'))
			return ch - 'A' + 10;
		else if ((ch >= 'a') && (ch <= 'f'))
			return ch - 'a' + 10;
		else
			return -1;
	}
}
