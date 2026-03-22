package com.emi.transaction_service.exception;

public class TransactionNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransactionNotFoundException (String msg) {
		super(msg);
	}
}
