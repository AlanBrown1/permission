package com.mmall.exception;

/**
 * @author Alan Brown
 * @date 2018/5/24 14:08
 */

public class PermissionException extends RuntimeException {
	public PermissionException() {
		super();
	}

	public PermissionException(String message) {
		super(message);
	}

	public PermissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public PermissionException(Throwable cause) {
		super(cause);
	}

	protected PermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
