package com.emi.wallet_service.service;

import com.emi.events.transactions.TransactionEvent;
import com.emi.wallet_service.ResponseDto.LedgerResponseDto;

public interface LedgerService {

	public void updateLedgerPayout(TransactionEvent event);
	
	public void updateLedgerDeposit(TransactionEvent event);
	
	public LedgerResponseDto getAllUsersRecord();
}
