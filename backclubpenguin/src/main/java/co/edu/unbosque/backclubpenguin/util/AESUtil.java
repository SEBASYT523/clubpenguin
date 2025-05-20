package co.edu.unbosque.backclubpenguin.util;

import java.security.InvalidAlgorithmParameterException;

import java.security.InvalidKeyException;

import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;

import javax.crypto.Cipher;

import javax.crypto.IllegalBlockSizeException;

import javax.crypto.NoSuchPaddingException;

import javax.crypto.spec.GCMParameterSpec;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;

import static org.apache.commons.codec.binary.Base64.encodeBase64;

import static org.apache.commons.codec.binary.Base64.decodeBase64;

public class AESUtil {

	private final static String ALGORITMO = "AES";

	private final static String TIPOCIFRADO = "AES/GCM/NoPadding";

	public static String encrypt(String llave, String iv, String texto) {

		Cipher cipher = null;

		try {

			cipher = Cipher.getInstance(TIPOCIFRADO);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {

			e.printStackTrace();

		}

		SecretKeySpec secretKeySpec = new SecretKeySpec(llave.getBytes(), ALGORITMO);

		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv.getBytes());

		try {

			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);

		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {

			e.printStackTrace();

		}

		byte[] encrypted = null;

		try {

			encrypted = cipher.doFinal(texto.getBytes());

		} catch (IllegalBlockSizeException | BadPaddingException e) {

			e.printStackTrace();

		}

		return new String(encodeBase64(encrypted));

	}

	public static String decrypt(String llave, String iv, String encrypted) {

		Cipher cipher = null;

		try {

			cipher = Cipher.getInstance(TIPOCIFRADO);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {

			e.printStackTrace();

		}

		SecretKeySpec secretKeySpec = new SecretKeySpec(llave.getBytes(), ALGORITMO);

		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv.getBytes());

		try {

			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);

		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {

			e.printStackTrace();

		}

		byte[] enc = decodeBase64(encrypted);

		byte[] decrypted = null;

		try {

			decrypted = cipher.doFinal(enc);

			return new String(decrypted);

		} catch (IllegalBlockSizeException | BadPaddingException e) {

			e.printStackTrace();

		}

		return "";

	}

	public static String decrypt(String encrypted) {

		String iv="";

		String key="";
		

		return decrypt(key, iv, encrypted);

	}

	public static String encrypt(String plainText) {

		String iv="";

		String key="";

		return encrypt(key, iv, plainText);

	}

	public static String hahingToMD5(String content) {
		return DigestUtils.md5Hex(content);
	}

	public static String hahingToSHA1(String content) {
		return DigestUtils.sha1Hex(content);
	}

	public static String hahingToSHA256(String content) {
		return DigestUtils.sha256Hex(content);
	}

	public static String hahingToSHA384(String content) {
		return DigestUtils.sha384Hex(content);
	}

	public static String hahingToSHA512(String content) {
		return DigestUtils.sha512Hex(content);
	}

	public static String hashinBCrypt(String content) {
		return BCrypt.hashpw(content, BCrypt.gensalt());
	}

	public static boolean validatePassword(String plainPassword, String hashedPassword) {
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}


}