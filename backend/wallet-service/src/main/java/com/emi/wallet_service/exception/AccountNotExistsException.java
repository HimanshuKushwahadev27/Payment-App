package com.emi.wallet_service.exception;

public class AccountNotExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountNotExistsException(String msg) {
		super(msg);
	}
}
