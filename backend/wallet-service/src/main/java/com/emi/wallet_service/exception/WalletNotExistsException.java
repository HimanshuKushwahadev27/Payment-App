package com.emi.wallet_service.exception;

public class WalletNotExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WalletNotExistsException(String msg) {
		super(msg);
	}
}
