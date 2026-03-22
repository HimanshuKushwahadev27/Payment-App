package com.emi.wallet_service.service;

import java.util.UUID;

import com.emi.events.transactions.TransactionEvent;
import com.emi.wallet_service.ResponseDto.LedgerResponseDto;

public interface LedgerService {

	public void updateLedgerPayout(TransactionEvent event);
	
	public void updateLedgerDeposit(TransactionEvent event);
	
	public LedgerResponseDto getUsersRecord( UUID accountId);

	public void updateLedgerPayoutFailure(TransactionEvent event);

	public void updateLedgerDepositFailure(TransactionEvent depositEvent);
}
