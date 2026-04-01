package com.emi.wallet_service.service;

import java.util.UUID;

import com.emi.events.transactions.TransactionEvent;
import com.emi.wallet_service.ResponseDto.LedgerResponseDto;
import com.stripe.exception.StripeException;

public interface LedgerService {

	public void updateLedgerPayout(TransactionEvent event);
	
	public void updateLedgerDeposit(TransactionEvent event) throws StripeException;
	
	public LedgerResponseDto getUsersRecord( UUID accountId, UUID userKeycloakId);

	public void updateLedgerPayoutFailure(TransactionEvent event);

	public void updateLedgerDepositFailure(TransactionEvent depositEvent) throws StripeException;
}
