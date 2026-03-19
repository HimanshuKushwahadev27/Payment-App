package com.emi.notification_service.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.emi.events.notification.EventType;
import com.emi.events.notification.NotificationData;
import com.emi.events.notification.NotificationEvent;
import com.emi.notification_service.clients.UserClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final JavaMailSender javaMailSender;
	private final UserClient userClient;
	@KafkaListener(topics="transaction-topics")
	public void listen(NotificationEvent event) {
		String body;

		if (event.getEventType() == EventType.TRANSACTION_SUCCESS) {
		    body = this.successTemplate(event.getData(), BigDecimal.valueOf(event.getData().getAmount()));
		} else {
		    body = this.failureTemplate(event.getData(), BigDecimal.valueOf(event.getData().getAmount()));
		}		
		
		MimeMessagePreparator mimeMessagePreparator  =  mimeMessage -> {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setFrom("himanshumaurya447756@gmail.com");
			helper.setTo(userClient.getEmail(UUID.fromString((String)event.getUserId())));
			helper.setSubject(String.format(  
					"Transaction %s - %s",
			        event.getData().getStatus(),
			        event.getData().getTransactionId()));
			helper.setText(body,false);
		};
		
		try {
			javaMailSender.send(mimeMessagePreparator);
		}catch(MailException ex){
			throw new RuntimeException("Mail couldnt be sent to the user");
		}
	}
	
	
	public String successTemplate(NotificationData data, BigDecimal amount) {
		String body = String.format("""
		        Hi,

		        Your transaction was successful 🎉

		        Transaction Details:
		        ----------------------------
		        Transaction ID : %s
		        Type           : %s
		        Amount         : ₹%s
		        Currency       : %s
		        Status         : %s
		        From Account   : %s
		        To Account     : %s

		        %s

		        Regards,
		        EMI Team
		        """,
		        data.getTransactionId(),
		        data.getTransactionType(),
		        amount,
		        data.getCurrency(),
		        data.getStatus(),
		        data.getFromAccountId() != null ? data.getFromAccountId() : "N/A",
		        data.getToAccountId() != null ? data.getToAccountId() : "N/A",
		        data.getMessage() != null ? data.getMessage() : ""
		);
		
		return body;
	}
	
	
	public String failureTemplate(NotificationData data, BigDecimal amount) {
		String body = String.format("""
		        Hi,

		        Your transaction failed ❌

		        Transaction Details:
		        ----------------------------
		        Transaction ID : %s
		        Type           : %s
		        Amount         : ₹%s
		        Currency       : %s
		        Status         : %s

		        Reason:
		        %s

		        Please try again or contact support.

		        Regards,
		        EMI Team
		        """,
		        data.getTransactionId(),
		        data.getTransactionType(),
		        amount,
		        data.getCurrency(),
		        data.getStatus(),
		        data.getMessage() != null ? data.getMessage() : "Unknown error"
		);
		
		return body;
	}
}
