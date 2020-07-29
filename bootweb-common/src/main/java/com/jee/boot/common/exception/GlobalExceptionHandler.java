package com.jee.boot.common.exception;

import com.jee.boot.common.constant.HttpStatus;
import com.jee.boot.common.core.result.R;
import com.jee.boot.common.core.result.RCodeEnum;
import com.jee.boot.common.exception.base.BaseException;
import com.jee.boot.common.exception.user.UserPasswordNotMatchException;
import com.jee.boot.common.utils.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**-------- 通用异常处理方法 --------**/
	@ExceptionHandler(Exception.class)
	public R error(Exception e) {
		log.error(e.getMessage(), e);
		return R.error(e.getMessage());
	}

	/**
	 * 基础异常
	 */
	public R error(BaseException e){
		log.error(e.getMessage(), e);
		return R.error(e.getMessage());
	}




	/**-------- 自定义定异常处理方法 --------**/
	@ExceptionHandler(GlobalException.class)
	public R error(GlobalException e) {
		log.error(e.getMessage(), e);
		return R.error(e.getMessage()).code(e.getCode());
	}

	/**-------- 指定异常处理方法 --------**/
	@ExceptionHandler(BindException.class)
	public R error(BindException e) {
		log.error(e.getMessage(), e);
		List<ObjectError> allErrors = e.getAllErrors();
		ObjectError error = allErrors.get(0);
		String msg = error.getDefaultMessage();
		return R.error(msg);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public R error(MethodArgumentNotValidException e){
		log.error(e.getMessage(), e);
		BindingResult result = e.getBindingResult();
		if (result.hasErrors()){
			List<ObjectError> allErrors = result.getAllErrors();
			ObjectError error = allErrors.get(0);
			return R.error(error.getDefaultMessage());
		}
		return R.error();
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public R error(HttpRequestMethodNotSupportedException e){
		log.error(e.getMessage(), e);
		return R.setResult(RCodeEnum.REQUEST_TYPE_ERROR);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public R error(NoHandlerFoundException e) {
		log.error(e.getMessage(), e);
		return R.error(HttpStatus.NOT_FOUND, MessageUtils.message("handler.no.found"));
	}

	@ExceptionHandler(UserPasswordNotMatchException.class)
	public R error(UserPasswordNotMatchException e) {
		log.error(e.getMessage(), e);
		return R.setResult(RCodeEnum.USER_NOT_MATCH);
	}

	/**
	 * 演示模式异常
	 */
	@ExceptionHandler(DemoModeException.class)
	public R demoModeException(DemoModeException e) {
		return R.error(MessageUtils.message("demo.model"));
	}
}
