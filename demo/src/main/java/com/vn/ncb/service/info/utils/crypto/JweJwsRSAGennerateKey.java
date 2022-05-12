package com.vn.ncb.service.info.utils.crypto;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;

public class JweJwsRSAGennerateKey {
	private static final Logger LOGGER = LoggerFactory.getLogger(JweJwsRSAGennerateKey.class);

	public static String signStringJweJws(String planText, String napasQRSwitchEncryption, String ncbProdPrivate) throws JOSEException {
		String senderJws = "";
		PublicKeyReader pubkeyReader_jws = new PublicKeyReader(napasQRSwitchEncryption);
		PrivateKeyReader prikeyReader_jwe = new PrivateKeyReader(ncbProdPrivate);
		com.nimbusds.jose.Payload senderJwePayload = new com.nimbusds.jose.Payload(planText);
//		System.out.print("Payload : " + senderJwePayload + "\n");
		// pack JWE
		JWEHeader jweHeader = new JWEHeader(JWEAlgorithm.RSA1_5, EncryptionMethod.A128GCM);
		JWEObject jweObject = new JWEObject(jweHeader, senderJwePayload);
		RSAEncrypter encrypter = new RSAEncrypter((RSAPublicKey) pubkeyReader_jws.getPublicKeyFromCertInput());
		try {
			LOGGER.info("JweJws data - Start ");
			jweObject.encrypt(encrypter);
			String senderJwe = jweObject.serialize();
//			System.out.println("JWE : " + senderJwe + "\n");
			// pack JWS
			RSAPrivateKey keySignJWS = (RSAPrivateKey) prikeyReader_jwe.getPrivateKeyInput();
			JWSSigner signer = new RSASSASigner(keySignJWS);
			JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);
			com.nimbusds.jose.Payload pl = new com.nimbusds.jose.Payload(senderJwe);
			JWSObject jwsObject = new JWSObject(header, pl);
			jwsObject.sign(signer);
			senderJws = jwsObject.serialize();
//			System.out.println("JWS : " + senderJws + "\n");
//			System.out.println("*****************************\n");
			LOGGER.info("JweJws data - success: " + senderJws);
		} catch (Exception e) {
			senderJws = "";
			LOGGER.info("JweJws data - error: " + e.getMessage());
		}
		LOGGER.info("JweJws data - End ");
//		System.out.println("-------------------->>data: " + senderJws);
		return senderJws;
	}
}
