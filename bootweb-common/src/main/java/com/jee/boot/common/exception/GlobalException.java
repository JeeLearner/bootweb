package com.jee.boot.common.exception;

import com.jee.boot.common.core.result.RCodeEnum;

public class GlobalException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	
	private Integer code;

	public GlobalException(Integer code, String message) {
		super(message);
		this.code = code;
	}


	public GlobalException(RCodeEnum rCodeEnum){
		super(rCodeEnum.getMessage());
		this.code = rCodeEnum.getCode();
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "GlobalException{" + "code=" + code + ", message=" + this.getMessage() + '}';
	}
}
