package com.emi.transaction_service.mapper;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.emi.events.notification.EventType;
import com.emi.events.notification.NotificationChannel;
import com.emi.events.notification.NotificationData;
import com.emi.events.notification.NotificationEvent;
import com.emi.events.notification.TransactionStatus;
import com.emi.events.notification.TransactionType;
import com.emi.events.payment.PaymentEvent;
import com.emi.events.transactions.TransactionEvent;
import com.emi.transaction_service.entity.Transaction;

@Component
public class EventMapper {

	public TransactionEvent toEntity(Transaction transaction) {
		return new TransactionEvent(
				UUID.randomUUID().toString().toString(),
				transaction.getId().toString(),
				transaction.getCurrency(),
				transaction.getToAccountId(),
				transaction.getUserKeycloakId().toString(),
				transaction.getAmount().longValue(),
				transaction.getType(),
				transaction.getStatus()
				);
	}

	public NotificationEvent getSuccessWithdraw(Transaction transaction, TransactionEvent event) {
		
		NotificationData data = new NotificationData();
		
		data.setTransactionId(transaction.getId().toString());
		data.setAmount(transaction.getAmount().longValue());
		data.setCurrency(transaction.getCurrency());
		data.setTransactionType(TransactionType.WITHDRAW);
		data.setStatus(TransactionStatus.SUCCESS);
		data.setTimestamp(Instant.now().toEpochMilli());
		data.setToAccountId(transaction.getToAccountId());
		data.setFromAccountId(transaction.getFromAccountId());
		data.setMessage("Money has been successfully withdrawn");
		
		NotificationEvent notification = new NotificationEvent();
		
		notification.setEventId(UUID.randomUUID().toString());
		notification.setEventType(EventType.TRANSACTION_SUCCESS);
		notification.setUserId(transaction.getUserKeycloakId().toString());
		notification.setChannel(NotificationChannel.EMAIL);
		notification.setData(data);
		return notification;
	}

	public NotificationEvent getFailureWithdraw(Transaction transaction, TransactionEvent event) {
		NotificationData data = new NotificationData();
		
		data.setTransactionId(transaction.getId().toString());
		data.setAmount(transaction.getAmount().longValue());
		data.setCurrency(transaction.getCurrency());
		data.setTransactionType(TransactionType.WITHDRAW);
		data.setStatus(TransactionStatus.FAILED);
		data.setTimestamp(Instant.now().toEpochMilli());
		data.setToAccountId(transaction.getToAccountId());
		data.setFromAccountId(transaction.getFromAccountId());
		data.setMessage("FAILURE PLEASE RETRY AFTER SOMETIME");
		
		NotificationEvent notification = new NotificationEvent();
		
		notification.setEventId(UUID.randomUUID().toString());
		notification.setEventType(EventType.TRANSACTION_FAILED);
		notification.setUserId(transaction.getUserKeycloakId().toString());
		notification.setChannel(NotificationChannel.EMAIL);
		notification.setData(data);
		return notification;
	}

	public NotificationEvent getFailurePayment(Transaction transaction, PaymentEvent event) {
		NotificationData data = new NotificationData();
		
		data.setTransactionId(transaction.getId().toString());
		data.setAmount(transaction.getAmount().longValue());
		data.setCurrency(transaction.getCurrency());
		data.setTransactionType(TransactionType.DEPOSIT);
		data.setStatus(TransactionStatus.FAILED);
		data.setTimestamp(Instant.now().toEpochMilli());
		data.setToAccountId(transaction.getToAccountId());
		data.setFromAccountId(transaction.getFromAccountId());
		data.setMessage("FAILURE PLEASE RETRY AFTER SOMETIME");
		
		NotificationEvent notification = new NotificationEvent();
		
		notification.setEventId(UUID.randomUUID().toString());
		notification.setEventType(EventType.TRANSACTION_FAILED);
		notification.setUserId(transaction.getUserKeycloakId().toString());
		notification.setChannel(NotificationChannel.EMAIL);
		notification.setData(data);
		return notification;
	}

	public NotificationEvent getSuccessPayment(Transaction transaction, PaymentEvent event) {
		NotificationData data = new NotificationData();
		
		data.setTransactionId(transaction.getId().toString());
		data.setAmount(transaction.getAmount().longValue());
		data.setCurrency(transaction.getCurrency());
		data.setTransactionType(TransactionType.DEPOSIT);
		data.setStatus(TransactionStatus.SUCCESS);
		data.setTimestamp(Instant.now().toEpochMilli());
		data.setToAccountId(transaction.getToAccountId());
		data.setFromAccountId(transaction.getFromAccountId());
		data.setMessage("DEPOSIT IS SUCCESSFULL");
		
		NotificationEvent notification = new NotificationEvent();
		
		notification.setEventId(UUID.randomUUID().toString());
		notification.setEventType(EventType.TRANSACTION_SUCCESS);
		notification.setUserId(transaction.getUserKeycloakId().toString());
		notification.setChannel(NotificationChannel.EMAIL);
		notification.setData(data);
		return notification;
	}



	

}
