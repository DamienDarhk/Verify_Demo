package com.vn.ncb.service.info.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vn.ncb.service.info.model.ResponseReveiveQRNapas;
import com.vn.ncb.service.info.model.ReveiveQRNapas;
import com.vn.ncb.service.info.service.ReceiveQRServices;
import com.vn.ncb.service.info.utils.RSAGenerateKey;

@RestController
@RequestMapping(value = "/napas")
public class ReceiveQRController {
	private static final Logger logger = LoggerFactory.getLogger(ReceiveQRController.class);

	@Autowired
	ReceiveQRServices receiveQRServices;

	@Value("${napas.key.signature}")
	private String napasQRSwitchSigning;
	
	@Value("${ncb.keystore}")
	private String keyStoreQRNCB;
	
	@Value("${ncb.passKeyStore}")
	private String passKeyStore;
	
	@Value("${ncb.aliasKeyStore}")
	private String aliasKeyStore;

	@RequestMapping(value = "/ping")
	public String ping() {
		return "hello";
	}

	
	
	@RequestMapping(value = "/sign", method = RequestMethod.POST)
	public void VerifyNapas(@RequestBody ResponseReveiveQRNapas response) throws Exception {
		boolean responseValid = false;
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		try {
			logger.info("Request: " + gson.toJson(response));
			String signature = RSAGenerateKey.generateSignString(
					gson.toJson(response.getPayload()) + "," + gson.toJson(response.getResult()), keyStoreQRNCB,
					passKeyStore, aliasKeyStore);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
//	@RequestMapping(value = "/verify", method = RequestMethod.POST)
//	public void VerifyNapas(@RequestBody ReveiveQRNapas receiveModel) throws Exception {
//		boolean responseValid = false;
//		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//		try {
//			logger.info("Request: " + gson.toJson(receiveModel));
//			responseValid = receiveQRServices.verifySignNapas(receiveModel, gson, napasQRSwitchSigning);
//			
//			if(!responseValid) {
//				logger.info("Failed to verify signature");
//			} else {
//				logger.info("Success to verify signature");
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
}
