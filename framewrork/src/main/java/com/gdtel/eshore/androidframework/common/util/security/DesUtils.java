package com.gdtel.eshore.androidframework.common.util.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.util.Base64;

import com.gdtel.eshore.androidframework.common.util.AppConstant;

/**
 * des加密
 * 
 * @author wudl
 */
public class DesUtils {

	private static final String PASSWORD_CRYPT_KEY = AppConstant.SECRET_KEY[0].substring(0, 8);

	private static byte[] bytes ;

	// private final static String DES = "DES";
	// private static final byte[] desKey;
	// 解密数据
	public static String decrypt(String message,int keyIndex ) throws Exception {

		//		byte[] bytesrc = convertHexString(message);
		String key = AppConstant.SECRET_KEY[keyIndex].substring(0,
				AppConstant.SECRET_KEY[keyIndex].length() >= 8 ? 8:AppConstant.SECRET_KEY[keyIndex].length());
		return decrypt(message, key);
	}

	public static String decrypt(String message, String key)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, UnsupportedEncodingException,
			InvalidKeySpecException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		byte[] bytesrc = Base64.decode(message, Base64.DEFAULT);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte);
	}

	public static byte[] encrypt(String message, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		return cipher.doFinal(message.getBytes("UTF-8"));
	}

	public static byte[] encrypt(String value,int keyIndex) {
		try {
			//			value = java.net.URLEncoder.encode(value, "utf-8");
			String key = AppConstant.SECRET_KEY[keyIndex].substring(0,
					AppConstant.SECRET_KEY[keyIndex].length() >= 8 ? 8:AppConstant.SECRET_KEY[keyIndex].length());
			bytes = encrypt(value, key);
			//			result = toHexString(bytes);
		} catch (Exception ex) {
			ex.printStackTrace();
			return bytes;
		}
		return bytes;
	}
	
	public static String encryptStr(String value,int keyIndex) {
		return toHexString(encrypt(value, keyIndex));
	}
	
	public static String encryptStr(String value,String key) throws Exception {
		return toHexString(encrypt(value, key));
	}

	public static byte[] convertHexString(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}
		return digest;
	}

	public static String toHexString(byte b[]) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2)
				plainText = "0" + plainText;
			hexString.append(plainText);
		}
		return hexString.toString();
	}

}
