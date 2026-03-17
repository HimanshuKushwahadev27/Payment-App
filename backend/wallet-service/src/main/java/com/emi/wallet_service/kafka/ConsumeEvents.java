package com.emi.wallet_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.emi.events.payment.PaymentEvent;
import com.emi.events.transactions.TransactionEvent;
import com.emi.wallet_service.service.LedgerService;
import com.emi.wallet_service.service.WalletService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsumeEvents{

	private final WalletService walletService;
	private final LedgerService ledgerService;
	
	@KafkaListener(topics="Payment-withdraw-success-event")
	public void consumePayout(TransactionEvent event) {
		walletService.payout(event);
	}
	
	@KafkaListener(topics="Payment-success-event")
	public void consumeCharge(PaymentEvent event) {
		walletService.charge(event);
	}
	
	@KafkaListener(topics="Payout-ledger-update")
	public void eventUpdateLedgerPayout(TransactionEvent event) {
		ledgerService.updateLedgerPayout(event);
	}

	@KafkaListener(topics="Deposit-ledger-update")
	public void eventUpdateLedgerDeposit(TransactionEvent depositEvent) {
		ledgerService.updateLedgerDeposit(depositEvent);
		
	}

	
	

}
