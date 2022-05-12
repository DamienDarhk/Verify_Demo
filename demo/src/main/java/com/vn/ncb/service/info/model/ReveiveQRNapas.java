package com.vn.ncb.service.info.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReveiveQRNapas implements Serializable {
	
	@JsonProperty("header")
	private Header header;

	@JsonProperty("payload")
	private Payload payload;

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Header implements Serializable {
		
		@JsonProperty("requestor")
		private Requestor requestor;
		
		@JsonProperty("reference-id")
		private String referenceId;
		
		@JsonProperty("timestamp")
		private Number timestamp;
		
		@JsonProperty("operation")
		private String operation;

		@JsonProperty("signature")
		private String signature;
		
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Requestor implements Serializable {

		@JsonProperty("id")
		private String id;
		
		@JsonProperty("name")
		private String name;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class Payload implements Serializable {

		@JsonProperty("payment")
		private Payment payment;
		
		@JsonProperty("amount")
		private String amount;

		@JsonProperty("currency")
		private String currency;

		@JsonProperty("sender_account")
		private String sender_account;
		
		@JsonProperty("sender")
		private Sender sender;
		
		@JsonProperty("participant")
		private Participant participant;

		@JsonProperty("recipient_account")
		private String recipient_account;

		@JsonProperty("order_info")
		private OrderInfo order_info;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Payment implements Serializable {

		@JsonProperty("generation_method")
		private String generation_method;
		
		@JsonProperty("type")
		private String type;

		@JsonProperty("channel")
		private String channel;

		@JsonProperty("transaction_local_date_time")
		private String transaction_local_date_time;

		@JsonProperty("trace")
		private String trace;

		@JsonProperty("payment_reference")
		private String payment_reference;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Sender implements Serializable {

		@JsonProperty("full_name")
		private String full_name;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Participant implements Serializable {

		@JsonProperty("originating_institution_id")
		private String originating_institution_id;

		@JsonProperty("receiving_institution_id")
		private String receiving_institution_id;

		@JsonProperty("merchant_id")
		private String merchant_id;

		@JsonProperty("card_acceptor_id")
		private String card_acceptor_id;

		@JsonProperty("card_acceptor_name")
		private String card_acceptor_name;

		@JsonProperty("card_acceptor_city")
		private String card_acceptor_city;

		@JsonProperty("card_acceptor_country")
		private String card_acceptor_country;

		@JsonProperty("merchant_category_code")
		private String merchant_category_code;

	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderInfo implements Serializable {

		@JsonProperty("bill_number")
		private String bill_number;
	}

}
