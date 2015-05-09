package com.netcracker.rest.utils;

/* 23:01 09.05.2015 by Viktor Taranenko */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author Cloud
 */


public final class SecuritySettings {

	private final static class MySecretKey implements SecretKey {

		private byte[] key = new byte[]{0xC, 0xA, 0xF, 0xE, 0xB, 0xA, 0xB, 0xE}; // ����
		// �� ������ ����� ����� ����� 8 ����, ��� ����������� ���������� ���
		// ���������� ��������

		public String getAlgorithm() {
			return "DES";
		}

		public String getFormat() {
			return "RAW";
		}

		public byte[] getEncoded() {
			return key;
		}
	}

	private static SecretKey key;

	private static Cipher ecipher;
	private static Cipher dcipher;

	static {
		try {
			key = new MySecretKey();
			ecipher = Cipher.getInstance("DES");
			dcipher = Cipher.getInstance("DES");
			ecipher.init(Cipher.ENCRYPT_MODE, key);
			dcipher.init(Cipher.DECRYPT_MODE, key);
		} catch (InvalidKeyException ex) {
			Logger.getLogger(SecuritySettings.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(SecuritySettings.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchPaddingException ex) {
			Logger.getLogger(SecuritySettings.class.getName()).log(Level.SEVERE, null, ex);
		}
	}


	/**
	 * ������� ����������
	 * @param str ������ ��������� ������
	 * @return ������������� ������ � ������� Base64
	 */
	public static String encrypt(String str) {
		try {
			byte[] utf8 = str.getBytes("UTF8");
			byte[] enc = ecipher.doFinal(utf8);
			return new sun.misc.BASE64Encoder().encode(enc);
		} catch (IllegalBlockSizeException ex) {
			Logger.getLogger(SecuritySettings.class.getName()).log(Level.SEVERE, null, ex);
		} catch (BadPaddingException ex) {
			Logger.getLogger(SecuritySettings.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(SecuritySettings.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	/**
	 * ������� �������������
	 * @param str ������������� ������ � ������� Base64
	 * @return �������������� ������
	 */
	public static String decrypt(String str)  {
		try {
			byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
			byte[] utf8 = dcipher.doFinal(dec);
			return new String(utf8, "UTF8");
		} catch (IllegalBlockSizeException ex) {
			Logger.getLogger(SecuritySettings.class.getName()).log(Level.SEVERE, null, ex);
		} catch (BadPaddingException ex) {
			Logger.getLogger(SecuritySettings.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(SecuritySettings.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}
