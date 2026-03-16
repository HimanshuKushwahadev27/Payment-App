package com.emi.transaction_service.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

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



	

}
