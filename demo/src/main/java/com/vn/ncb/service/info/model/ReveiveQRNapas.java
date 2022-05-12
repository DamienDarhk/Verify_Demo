package com.vn.ncb.service.info.model;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

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
		private String requestorId;
		
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
		private Number amount;

		@JsonProperty("currency")
		private String currency;

		@JsonProperty("sender_account")
		private String senderAccount;
		
		@JsonProperty("sender")
		private Sender sender;
		
		@JsonProperty("participant")
		private Participant participant;

		@JsonProperty("recipient_account")
		private String recipientAccount;

		@JsonProperty("order_info")
		private OrderInfo orderInfo;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Payment implements Serializable {

		@JsonProperty("generation_method")
		private String generationMethod;
		
		@JsonProperty("type")
		private String type;

		@JsonProperty("channel")
		private String channel;

		@JsonProperty("transaction_local_date_time")
		private Date transactionLocalDateTime;

		@JsonProperty("trace")
		private String trace;

		@JsonProperty("payment_reference")
		private String paymentReference;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Sender implements Serializable {

		@JsonProperty("full_name")
		private String fullName;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Participant implements Serializable {

		@JsonProperty("originating_institution_id")
		private String originatingInstitutionId;

		@JsonProperty("receiving_institution_id")
		private String receivingInstitutionId;

		@JsonProperty("merchant_id")
		private String merchantId;

		@JsonProperty("card_acceptor_id")
		private String cardAcceptorId;

		@JsonProperty("card_acceptor_name")
		private String cardAcceptorName;

		@JsonProperty("card_acceptor_city")
		private String cardAcceptorCity;

		@JsonProperty("card_acceptor_country")
		private String cardAcceptorCountry;

		@JsonProperty("merchant_category_code")
		private String merchantCategoryCode;

	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderInfo implements Serializable {

		@JsonProperty("bill_number")
		private String billNumber;
	}

}
