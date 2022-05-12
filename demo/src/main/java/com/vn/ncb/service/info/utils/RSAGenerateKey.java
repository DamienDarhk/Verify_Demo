package com.vn.ncb.service.info.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vn.ncb.service.info.utils.crypto.PublicKeyReader;

public class RSAGenerateKey {

	private static final Logger LOGGER = LoggerFactory.getLogger(RSAGenerateKey.class);

	public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
		Signature publicSignature = Signature.getInstance("SHA256withRSA");
		publicSignature.initVerify(publicKey);
		publicSignature.update(plainText.getBytes(UTF_8));

		byte[] signatureBytes = Base64.getDecoder().decode(signature);

		return publicSignature.verify(signatureBytes);
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
