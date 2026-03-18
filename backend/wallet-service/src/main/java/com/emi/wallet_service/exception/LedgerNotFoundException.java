package com.emi.wallet_service.exception;

public class LedgerNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LedgerNotFoundException(String msg) {
		super(msg);
	}
}
