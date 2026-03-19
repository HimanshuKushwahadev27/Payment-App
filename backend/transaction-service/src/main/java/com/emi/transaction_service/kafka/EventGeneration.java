
package com.emi.transaction_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.emi.events.notification.NotificationEvent;
import com.emi.events.transactions.TransactionEvent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class EventGeneration {

	private final KafkaTemplate<String, TransactionEvent> kafkaPayoutGeneration;
	private final KafkaTemplate<String, TransactionEvent> kafkaLedgerPayout;
	private final KafkaTemplate<String, TransactionEvent> kafkaLedgerDeposit;
	private final KafkaTemplate<String, TransactionEvent> kafkaLedgerPayoutFailure;
	private final KafkaTemplate<String, TransactionEvent> kafkaLedgerDepositFailure;
	private final KafkaTemplate<String , NotificationEvent> kafkaPayoutSuccess;
	private final KafkaTemplate<String , NotificationEvent> kafkaPayoutFailure;
	private final KafkaTemplate<String , NotificationEvent> kafkaDepositSuccess;
	private final KafkaTemplate<String , NotificationEvent> kafkaDepositFailure;

	public void eventPayout(TransactionEvent event) {
		kafkaPayoutGeneration.send("Payout-generation-event" , event)	;	
	}

	
	public void eventUpdateLedgerPayout(TransactionEvent event) {
		kafkaLedgerPayout.send("Payout-ledger-update", event);
	}


	public void eventUpdateLedgerDeposit(TransactionEvent depositEvent) {
		kafkaLedgerDeposit.send("Deposit-ledger-update", depositEvent)	;	
	}
	
	public void eventUpdateLedgerPayoutFailure(TransactionEvent event) {
		kafkaLedgerPayoutFailure.send("Payout-ledger-update-failure", event);
	}


	public void eventUpdateLedgerDepositFailure(TransactionEvent depositEvent) {
		kafkaLedgerDepositFailure.send("Deposit-ledger-update-failure", depositEvent)	;	
	}

	public void eventPayoutSuccessNotification(NotificationEvent event) {
		kafkaPayoutSuccess.send("transaction-topics", event);
	}
	
	public void eventPayoutFailureNotification(NotificationEvent event) {
		kafkaPayoutFailure.send("transaction-topics", event);
	}
	
	public void eventDepositSuccessNotification(NotificationEvent event) {
		kafkaDepositSuccess.send("transaction-topics", event);
	}
	
	public void eventDepositFailureNotification(NotificationEvent event) {
		kafkaDepositFailure.send("transaction-topics", event);
	}
	


}
