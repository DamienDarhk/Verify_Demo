package com.vn.ncb.service.info;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vn.ncb.service.info.controller.ReceiveQRController;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(DemoApplication.class, args);
//		
//		ReceiveQRController rec = new ReceiveQRController();
//		rec.VerifyNapas(null);
//		
	}

}
