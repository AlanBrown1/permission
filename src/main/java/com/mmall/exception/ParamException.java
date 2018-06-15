package com.mmall.exception;

/**
 * @author Alan Brown
 * @date 2018/5/24 15:34
 */
// 参数不合要求的异常
public class ParamException extends RuntimeException{
	public ParamException() {
		super();
	}

	public ParamException(String message) {
		super(message);
	}

	public ParamException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParamException(Throwable cause) {
		super(cause);
	}

	protected ParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
