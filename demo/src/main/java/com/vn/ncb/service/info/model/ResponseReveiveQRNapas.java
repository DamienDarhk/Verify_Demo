package com.vn.ncb.service.info.model;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseReveiveQRNapas {

	@SerializedName("header")
	private Header header;
	@SerializedName("payload")
	private Payload payload;
	@SerializedName("result")
	private Result result;

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Header {
		
		@SerializedName("operation-id")
		private String operationId;

		@SerializedName("requested-by")
		private RequestedBy requestedBy;
		
		@SerializedName("signature")
		private String signature;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RequestedBy implements Serializable {
		@SerializedName("requestor")
		private Requestor requestor;
		@SerializedName("reference-id")
		private String referenceId;
		@SerializedName("operation")
		private String operation;
	}
	
	
	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Requestor implements Serializable {
		@SerializedName("id")
		private String id;
	}
	
	
	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Result {
		@SerializedName("id")
		private String id;

		@SerializedName("code")
		private String code;

		@SerializedName("message")
		private String message;
	}
	
	

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class Payload {

		@SerializedName("payment")
		private Payment payment;
		
		@SerializedName("amount")
		private String amount;

		@SerializedName("currency")
		private String currency;
		
		@SerializedName("settlement_date")
		private String settlement_date;

		@SerializedName("sender_account")
		private String sender_account;

		@SerializedName("sender")
		private Sender sender;

		@SerializedName("participant")
		private Participant participant;

		@SerializedName("recipient_account")
		private String recipient_account;
		
		@SerializedName("order_info")
		private OrderInfo order_info;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Payment implements Serializable {
		@SerializedName("type")
		private String type;

		@SerializedName("channel")
		private String channel;

		@SerializedName("transaction_local_date_time")
		private String transaction_local_date_time;

		@SerializedName("trace")
		private String trace;

		@SerializedName("payment_reference")
		private String payment_reference;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Sender implements Serializable {

		@SerializedName("full_name")
		private String full_name;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Participant implements Serializable {

		@SerializedName("originating_institution_id")
		private String originating_institution_id;

		@SerializedName("receiving_institution_id")
		private String receiving_institution_id;

		@SerializedName("merchant_id")
		private String merchant_id;

		@SerializedName("card_acceptor_id")
		private String card_acceptor_id;

		@SerializedName("card_acceptor_name")
		private String card_acceptor_name;

		@SerializedName("card_acceptor_city")
		private String card_acceptor_city;

		@SerializedName("card_acceptor_country")
		private String card_acceptor_country;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderInfo implements Serializable {

		@SerializedName("bill_number")
		private String bill_number;
	}

}
