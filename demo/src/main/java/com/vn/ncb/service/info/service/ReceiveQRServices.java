package com.vn.ncb.service.info.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;

import com.vn.ncb.service.info.utils.RSAGenerateKey;
import com.vn.ncb.service.info.model.ReveiveQRNapas;

@Service
public class ReceiveQRServices {
	private static final Logger logger = LoggerFactory.getLogger(ReceiveQRServices.class);


	public boolean verifySignNapas(ReveiveQRNapas requestModel, Gson gson, String NapasQRSwitchSigning) {
		boolean checkVerify = false;
		try {
			String signature = requestModel.getHeader().getSignature();
			String payload = gson.toJson(requestModel.getPayload());
			logger.info("signature to sign: " + signature);
			logger.info("payload to sign: " + payload);
			checkVerify = RSAGenerateKey.verifySignDataNapas(payload, signature, NapasQRSwitchSigning);
			if (checkVerify) {
				logger.info("call api Napas - Verify done: ");
			} else {
				logger.info("call api Napas - Verify failed: ");
			}
		} catch (Exception e) {
			logger.info("Co loi validate : " + e.getMessage());
			logger.info(e.getMessage());
		}

		return checkVerify;
	}

}
