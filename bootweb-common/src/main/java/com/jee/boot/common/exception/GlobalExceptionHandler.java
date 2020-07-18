package com.jee.boot.common.exception;

import com.jee.boot.common.core.result.R;
import com.jee.boot.common.core.result.RCodeEnum;
import com.jee.boot.common.exception.base.BusinessException;
import com.jee.boot.common.utils.spring.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**-------- 通用异常处理方法 --------**/
	@ExceptionHandler(Exception.class)
	public R error(Exception e) {
		e.printStackTrace();
		return R.error();
	}

	/**
	 * 业务异常
	 */
	@ExceptionHandler(BusinessException.class)
	public Object businessException(HttpServletRequest request, BusinessException e){
		log.error(e.getMessage(), e);
		if (ServletUtils.isAjaxRequest(request)){
			// return R.error().msg(PermissionUtils.getMsg(e.getMessage()));
			return R.error().msg(e.getMessage());
		} else {
			ModelAndView view = new ModelAndView();
			view.setViewName("error/unauth");
			return view;
		}
	}




	/**-------- 自定义定异常处理方法 --------**/
	@ExceptionHandler(GlobalException.class)
	public R error(GlobalException e) {
		e.printStackTrace();
		return R.error().msg(e.getMessage()).code(e.getCode());
	}

	/**-------- 指定异常处理方法 --------**/
	@ExceptionHandler(BindException.class)
	public R error(BindException e) {
		e.printStackTrace();
		List<ObjectError> allErrors = e.getAllErrors();
		ObjectError error = allErrors.get(0);
		String msg = error.getDefaultMessage();
		return R.error().msg(msg);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public R error(MethodArgumentNotValidException e){
		e.printStackTrace();
		BindingResult result = e.getBindingResult();
		if (result.hasErrors()){
			List<ObjectError> allErrors = result.getAllErrors();
			ObjectError error = allErrors.get(0);
			return R.error().msg(error.getDefaultMessage());
		}
		return R.error();
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public R error(HttpRequestMethodNotSupportedException e){
		e.printStackTrace();
		return R.setResult(RCodeEnum.REQUEST_TYPE_ERROR);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public R error(NoHandlerFoundException e) {
		e.printStackTrace();
		return R.setResult(RCodeEnum.COMMON_NOFOUND_HANDLER);
	}


}
