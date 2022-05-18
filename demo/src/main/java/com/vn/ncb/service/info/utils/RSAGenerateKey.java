package com.vn.ncb.service.info.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Base64;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vn.ncb.service.info.utils.crypto.PublicKeyReader;

public class RSAGenerateKey {

	private static final Logger LOGGER = LoggerFactory.getLogger(RSAGenerateKey.class);

	public static KeyPair generateKeyPair() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048, new SecureRandom());
		KeyPair pair = generator.generateKeyPair();
		return pair;
	}

	public static KeyPair getKeyPairFromKeyStore(String keyStoreQRNCB, String passKeyKeyStoreNCB,
			String aliasKeyStoreNCB) throws Exception {
		InputStream ins = RSAGenerateKey.class.getResourceAsStream(keyStoreQRNCB);
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(ins, passKeyKeyStoreNCB.toCharArray()); // Keystore password
		KeyStore.PasswordProtection keyPassword = // Key password
				new KeyStore.PasswordProtection(passKeyKeyStoreNCB.toCharArray());
		KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(aliasKeyStoreNCB,
				keyPassword);
		java.security.cert.Certificate cert = keyStore.getCertificate(aliasKeyStoreNCB);

		PublicKey publicKey = cert.getPublicKey();
		PrivateKey privateKey = privateKeyEntry.getPrivateKey();

		return new KeyPair(publicKey, privateKey);
	}

	public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

		byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));

		return Base64.getEncoder().encodeToString(cipherText);
	}

	public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(cipherText);

		Cipher decriptCipher = Cipher.getInstance("RSA");
		decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

		return new String(decriptCipher.doFinal(bytes), UTF_8);
	}

	public static String sign(String plainText, PrivateKey privateKey) throws Exception {
		Signature privateSignature = Signature.getInstance("SHA256withRSA");
		privateSignature.initSign(privateKey);
		privateSignature.update(plainText.getBytes(UTF_8));

		byte[] signature = privateSignature.sign();

		return Base64.getEncoder().encodeToString(signature);
	}

	public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
		Signature publicSignature = Signature.getInstance("SHA256withRSA");
		publicSignature.initVerify(publicKey);
		publicSignature.update(plainText.getBytes(UTF_8));

		byte[] signatureBytes = Base64.getDecoder().decode(signature);

		return publicSignature.verify(signatureBytes);
	}

	public static String generateSignString(String planText, String keyStoreQRNCB, String passKeyKeyStoreNCB,
			String aliasKeyStoreNCB) {
		String signature = "";
		try {
			LOGGER.info("Gennerate string sign data - Start ");
			KeyPair pair = getKeyPairFromKeyStore(keyStoreQRNCB, passKeyKeyStoreNCB, aliasKeyStoreNCB);
			signature = sign(planText, pair.getPrivate());
//			check verify signature
//			PublicKeyReader pubkeyReader_jws = new PublicKeyReader("/keys/ncb_public.cer");
//			boolean isCorrect = RsaExample.verify(planText, signature, pubkeyReader_jws.getPublicKeyFromCertInput());
//			System.out.println("Signature correct: " + isCorrect);
			LOGGER.info("Gennerate string sign data - sucess: " + signature);
		} catch (Exception e) {
			signature = "";
			LOGGER.info("Gennerate string sign data - error: " + e.getMessage());
		}
		LOGGER.info("Gennerate string sign data - done ");
		return signature;
	}

	public static Boolean verifySignDataNapas(String planText, String senderJws, String NapasQRSwitchSigning) {
		senderJws = senderJws.replace("\\n", "").replace("\n", "");
		boolean isVerify = false;
		try {
			LOGGER.info("Verify string sign data - Start ");
//			check verify signature
			PublicKeyReader pubkeyReader_jws = new PublicKeyReader(NapasQRSwitchSigning);
			isVerify = RSAGenerateKey.verify(planText, senderJws, pubkeyReader_jws.getPublicKeyFromCertInput());
			LOGGER.info("Verify string sign data:  " + isVerify);
		} catch (Exception e) {
			isVerify = false;
			LOGGER.info("Verify string sign data - error: " + e.getMessage());
		}
		return isVerify;
	}
}

