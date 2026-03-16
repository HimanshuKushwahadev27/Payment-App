package com.emi.transaction_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.emi.events.transactions.TransactionEvent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class EventGeneration {

	private final KafkaTemplate<String, TransactionEvent> kafkaPayoutGeneration;
	private final KafkaTemplate<String, TransactionEvent> kafkaLedgerPayout;
	private final KafkaTemplate<String, TransactionEvent> kafkaLedgerDeposit;


	public void eventPayout(TransactionEvent event) {
		kafkaPayoutGeneration.send("Payout-generation-event" , event)	;	
	}

	
	public void eventUpdateLedgerPayout(TransactionEvent event) {
		kafkaLedgerPayout.send("Payout-ledger-update", event);
	}


	public void eventUpdateLedgerDeposit(TransactionEvent depositEvent) {
		kafkaLedgerDeposit.send("Deposit-ledger-update", depositEvent)	;	
	}

}
