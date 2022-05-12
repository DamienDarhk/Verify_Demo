package com.vn.ncb.service.info.utils.crypto;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;

import javax.xml.bind.DatatypeConverter;

/*
 * To change this license Header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Phonglx
 */
public class PrivateKeyReader {

	// Private key file using PKCS #1 encoding
	public static final String P1_BEGIN_MARKER = "-----BEGIN RSA PRIVATE KEY"; //$NON-NLS-1$
	public static final String P1_END_MARKER = "-----END RSA PRIVATE KEY"; //$NON-NLS-1$

	// Private key file using PKCS #8 encoding
	public static final String P8_BEGIN_MARKER = "-----BEGIN PRIVATE KEY"; //$NON-NLS-1$
	public static final String P8_END_MARKER = "-----END PRIVATE KEY"; //$NON-NLS-1$

//    private static Map<String, PrivateKey> keyCache
//            = Collections.synchronizedMap(new HashMap<String, PrivateKey>());

	protected final String fileName;

	/**
	 * Create a PEM private key file reader.
	 *
	 * @param fileName The name of the PEM file
	 */
	public PrivateKeyReader(String fileName) {
		this.fileName = fileName;
	}

	PrivateKeyReader() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	/**
	 * Get a Private Key from string
	 *
	 * @return Private key
	 * @throws IOException
	 */

	public PrivateKey getPrivateKeyFromString(String prikeyString) throws IOException, GeneralSecurityException {
		PrivateKey key = null;
		boolean isRSAKey = false;
		try {

			InputStream is = new ByteArrayInputStream(prikeyString.getBytes());
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder builder = new StringBuilder();
			boolean inKey = false;
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				if (!inKey) {
					if (line.startsWith("-----BEGIN ") && line.endsWith(" PRIVATE KEY-----")) {
						inKey = true;
						isRSAKey = line.contains("RSA");
					}
					continue;
				} else {
					if (line.startsWith("-----END ") && line.endsWith(" PRIVATE KEY-----")) {
						inKey = false;
						isRSAKey = line.contains("RSA");
						break;
					}
					builder.append(line);
				}
			}
			KeySpec keySpec = null;
			byte[] encoded = DatatypeConverter.parseBase64Binary(builder.toString());
			if (isRSAKey) {
				keySpec = getRSAKeySpec(encoded);
			} else {
				keySpec = new PKCS8EncodedKeySpec(encoded);
			}
			KeyFactory kf = KeyFactory.getInstance("RSA");
			key = kf.generatePrivate(keySpec);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return key;
	}

	/**
	 * Get a Private Key for the file.
	 *
	 * @return Private key
	 * @throws IOException
	 */
	public PrivateKey getPrivateKey() throws IOException, GeneralSecurityException {
		PrivateKey key = null;
		FileInputStream fis = null;
		boolean isRSAKey = false;
		BufferedReader br = null;
		try {
			File f = new File(fileName);
			fis = new FileInputStream(f);

			br = new BufferedReader(new InputStreamReader(fis));
			StringBuilder builder = new StringBuilder();
			boolean inKey = false;
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				if (!inKey) {
					if (line.startsWith("-----BEGIN ") && line.endsWith(" PRIVATE KEY-----")) {
						inKey = true;
						isRSAKey = line.contains("RSA");
					}
					continue;
				} else {
					if (line.startsWith("-----END ") && line.endsWith(" PRIVATE KEY-----")) {
						inKey = false;
						isRSAKey = line.contains("RSA");
						break;
					}
					builder.append(line);
				}
			}
			KeySpec keySpec = null;
			byte[] encoded = DatatypeConverter.parseBase64Binary(builder.toString());
			if (isRSAKey) {
				keySpec = getRSAKeySpec(encoded);
			} else {
				keySpec = new PKCS8EncodedKeySpec(encoded);
			}
			KeyFactory kf = KeyFactory.getInstance("RSA");
			key = kf.generatePrivate(keySpec);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception ign) {
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (Exception ign) {
				}
			}
		}
		return key;
	}

	/**
	 * Get a Private Key for the file.
	 *
	 * @return Private key
	 * @throws IOException
	 */
	public PrivateKey getPrivateKeyInput() throws IOException, GeneralSecurityException {
		PrivateKey key = null;
		boolean isRSAKey = false;
		InputStream ins = null;
		try {
			ins = PrivateKeyReader.class.getResourceAsStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(ins));
			StringBuilder builder = new StringBuilder();
			boolean inKey = false;
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				if (!inKey) {
					if (line.startsWith("-----BEGIN ") && line.endsWith(" PRIVATE KEY-----")) {
						inKey = true;
						isRSAKey = line.contains("RSA");
					}
					continue;
				} else {
					if (line.startsWith("-----END ") && line.endsWith(" PRIVATE KEY-----")) {
						inKey = false;
						isRSAKey = line.contains("RSA");
						break;
					}
					builder.append(line);
				}
			}
			KeySpec keySpec = null;
			byte[] encoded = DatatypeConverter.parseBase64Binary(builder.toString());
			if (isRSAKey) {
				keySpec = getRSAKeySpec(encoded);
			} else {
				keySpec = new PKCS8EncodedKeySpec(encoded);
			}
			KeyFactory kf = KeyFactory.getInstance("RSA");
			key = kf.generatePrivate(keySpec);
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (Exception ign) {
				}
			}
		}
		return key;
	}

	/**
	 * Convert PKCS#1 encoded private key into RSAPrivateCrtKeySpec.
	 *
	 * <p/>
	 * The ASN.1 syntax for the private key with CRT is
	 *
	 * <pre>
	 * --
	 * -- Representation of RSA private key with information for the CRT algorithm.
	 * --
	 * RSAPrivateKey ::= SEQUENCE {
	 *   version           Version,
	 *   modulus           INTEGER,  -- n
	 *   publicExponent    INTEGER,  -- e
	 *   privateExponent   INTEGER,  -- d
	 *   prime1            INTEGER,  -- p
	 *   prime2            INTEGER,  -- q
	 *   exponent1         INTEGER,  -- d mod (p-1)
	 *   exponent2         INTEGER,  -- d mod (q-1)
	 *   coefficient       INTEGER,  -- (inverse of q) mod p
	 *   otherPrimeInfos   OtherPrimeInfos OPTIONAL
	 * }
	 * </pre>
	 *
	 * @param keyBytes PKCS#1 encoded key
	 * @return KeySpec
	 * @throws IOException
	 */
	private RSAPrivateCrtKeySpec getRSAKeySpec(byte[] keyBytes) throws IOException {

		DerParser parser = new DerParser(keyBytes);

		Asn1Object sequence = parser.read();
		if (sequence.getType() != DerParser.SEQUENCE) {
			throw new IOException("Invalid DER: not a sequence"); //$NON-NLS-1$
		}
		// Parse inside the sequence
		parser = sequence.getParser();

		parser.read(); // Skip version
		BigInteger modulus = parser.read().getInteger();
		BigInteger publicExp = parser.read().getInteger();
		BigInteger privateExp = parser.read().getInteger();
		BigInteger prime1 = parser.read().getInteger();
		BigInteger prime2 = parser.read().getInteger();
		BigInteger exp1 = parser.read().getInteger();
		BigInteger exp2 = parser.read().getInteger();
		BigInteger crtCoef = parser.read().getInteger();

		RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1,
				exp2, crtCoef);

		return keySpec;
	}
}
