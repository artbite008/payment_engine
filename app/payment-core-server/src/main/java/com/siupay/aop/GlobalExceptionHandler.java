package com.siupay.aop;//package com.siupay.aop;
//
//
//import com.siupay.common.api.enums.PaymentSystem;
//import com.siupay.common.api.exception.ErrorCode;
//import com.siupay.common.api.exception.PaymentError;
//import com.siupay.common.api.exception.PaymentException;
//import io.vavr.control.Either;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.converter.HttpMessageConversionException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//
//@RestControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//
//	@ExceptionHandler(Throwable.class)
//	@ResponseBody
//	public Either<PaymentError,Object> handleStandardResponseErrorThrowable(HttpServletRequest request, HttpServletResponse response, Throwable e) {
//		String path = "";
//		if (request.getRequestURI() != null) {
//			path = request.getRequestURI();
//		}
//		log.error("GlobalExceptionHandler path : " + path, e);
//		String code;
//		String msg;
//		if (e instanceof PaymentException) {
//			response.setStatus(HttpStatus.BAD_REQUEST.value());
//			PaymentException pe = (PaymentException) e;
//			code = pe.getErrorCode().getCode();
//			msg = pe.getMessage();
//		} else if (e instanceof HttpMessageConversionException) {
//			response.setStatus(HttpStatus.BAD_REQUEST.value());
//			code = ErrorCode.PARAM_ERROR.getCode();
//			msg = ErrorCode.PARAM_ERROR.getMsg();
//		} else {
//			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			code = ErrorCode.SERVER_ERROR.getCode();
//			msg = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
//		}
//		//这里返回ok http code200，前端要求异常错误也要200code
//		response.setStatus(HttpStatus.OK.value());
//		PaymentError paymentError = new PaymentError(code,msg, PaymentSystem.PAYMENT_CORE.getSystemId());
//		return Either.left(paymentError);
//	}
//}
