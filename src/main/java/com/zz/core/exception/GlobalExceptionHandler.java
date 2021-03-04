
package com.zz.core.exception;

import com.alibaba.fastjson.JSON;
import com.zz.core.api.ApiResult;
import com.zz.core.api.ApiResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 全局异常处理
 */
@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 非法参数验证异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ApiResult handleMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> list = new ArrayList<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            list.add(fieldError.getDefaultMessage());
        }
        Collections.sort(list);
        log.error("fieldErrors" + JSON.toJSONString(list));
        return ApiResult.fail(ApiResultCode.UNAUTHORIZED_AGENCY, list);
    }

    /**
     * 系统登录异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = SysLoginException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult sysLoginExceptionHandler(SysLoginException exception) {
        log.warn("系统登录异常:" + exception.getMessage());
        return ApiResult.fail(ApiResultCode.LOGIN_EXCEPTION);
    }


    /**
     * HTTP解析请求参数异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult httpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("httpMessageNotReadableException:", exception);
        return ApiResult.fail(ApiResultCode.PARAMETER_EXCEPTION, ApiResultCode.PARAMETER_PARSE_EXCEPTION);
    }

    /**
     * HTTP
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = HttpMediaTypeException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult httpMediaTypeException(HttpMediaTypeException exception) {
        log.error("httpMediaTypeException:", exception);
        return ApiResult.fail(ApiResultCode.PARAMETER_EXCEPTION, ApiResultCode.HTTP_MEDIA_TYPE_EXCEPTION);
    }

    /**
     * 自定义业务/数据异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {ZzException.class})
    @ResponseStatus(HttpStatus.OK)
    public ApiResult springBootPlusExceptionHandler(ZzException exception) {
        log.error("springBootPlusException:", exception);
        int errorCode;
        if (exception instanceof BusinessException) {
            errorCode = ApiResultCode.BUSINESS_EXCEPTION.getCode();
        } else if (exception instanceof DaoException) {
            errorCode = ApiResultCode.DAO_EXCEPTION.getCode();
        } else if (exception instanceof VerificationCodeException) {
            errorCode = ApiResultCode.VERIFICATION_CODE_EXCEPTION.getCode();
        } else {
            errorCode = ApiResultCode.SPRING_BOOT_PLUS_EXCEPTION.getCode();
        }
        return new ApiResult().setCode(errorCode).setMsg(exception.getMessage());
    }


    /**
     * 登陆授权异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult authenticationExceptionHandler(AuthenticationException exception) {
        log.error("authenticationException:", exception);
        return new ApiResult()
                .setCode(ApiResultCode.AUTHENTICATION_EXCEPTION.getCode())
                .setMsg(exception.getMessage());
    }


//    /**
//     * 未认证异常处理
//     *
//     * @param exception
//     * @return
//     */
//    @ExceptionHandler(value = UnauthenticatedException.class)
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResult unauthenticatedExceptionHandler(UnauthenticatedException exception) {
//        log.error("unauthenticatedException:", exception);
//        return ApiResult.fail(ApiResultCode.UNAUTHENTICATED_EXCEPTION);
//    }
//
//
//    /**
//     * 未授权异常处理
//     *
//     * @param exception
//     * @return
//     */
//    @ExceptionHandler(value = UnauthorizedException.class)
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResult unauthorizedExceptionHandler(UnauthorizedException exception) {
//        log.error("unauthorizedException:", exception);
//        return ApiResult.fail(ApiResultCode.UNAUTHORIZED_EXCEPTION);
//    }


    /**
     * 默认的异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult exceptionHandler(Exception exception) {
        log.error("exception:", exception);
        return ApiResult.fail(ApiResultCode.SYSTEM_EXCEPTION);
    }

}
