package com.zz.core.aspect;


import com.alibaba.fastjson.JSON;
import com.zz.core.api.ApiResult;
import com.zz.core.api.ApiResultCode;
import com.zz.core.auth.ZzUserContextHolder;
import com.zz.core.constant.CoreConstant;
import com.zz.core.dto.LogMessage;
import com.zz.core.exception.ZzException;
import com.zz.core.rabbitmq.AmqpClient;
import com.zz.core.util.HttpServletRequestUtil;
import com.zz.core.util.ServletRequestIPUtil;
import com.zz.core.vo.LoginUserTokenVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class ControllerEndpointAspect extends AspectSupport {

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private AmqpClient amqpClient;

    @Pointcut("@annotation(com.zz.core.aspect.ControllerEndpoint)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws ZzException
    {
        Object result;
        Method targetMethod = resolveMethod(point);
        ControllerEndpoint annotation = targetMethod.getAnnotation(ControllerEndpoint.class);
        long start = System.currentTimeMillis();
        try {
            log.info(annotation.operation()+"-start");
            result = point.proceed();
            recordOpLog(annotation, point, targetMethod, start , result);
            log.info(annotation.operation()+"-end");
            return result;
        } catch (Throwable throwable) {
            String exceptionMessage = annotation.exceptionMessage();
            String message = throwable.getMessage();
            log.error(exceptionMessage, throwable);
            recordOpLog(annotation, point, targetMethod, start , null);
            throw new ZzException(exceptionMessage+":"+message);
        }
    }

    private void recordOpLog(ControllerEndpoint annotation, ProceedingJoinPoint point,Method targetMethod,
                             long start, Object result){
        String operation = annotation.operation();
        if (StringUtils.isNotBlank(operation)) {
            HttpServletRequest request = HttpServletRequestUtil.getRequest();
            String username = CoreConstant.UNKOWN;
            LoginUserTokenVo userVo  = ZzUserContextHolder.getUser();
            if (userVo != null) {
                username = userVo.getUserName();
            }
            saveLog(point, targetMethod, request, operation, username, start, result, annotation.recordResponse());
        }
    }

    public void saveLog(ProceedingJoinPoint point, Method method, HttpServletRequest request, String operation
            , String username, long start, Object result, boolean recordResponse) {
        try {
            LogMessage log = new LogMessage();
            String ip = ServletRequestIPUtil.getIpAddr(request);
            log.setIp(ip);
            log.setOriUrl(request.getRequestURL().toString());
            log.setUserName(username);
            log.setTime(System.currentTimeMillis() - start);
            log.setOperation(operation);
            String className = point.getTarget().getClass().getName();
            String methodName = method.getName();
            log.setMethod(className + "." + methodName + "()");
            log.setCreateTime(DateFormatUtils.format(new Date(), CoreConstant.DATE_TIME_PATTERN));
            log.setClient(appName);
            log.setTraceId(MDC.get(CoreConstant.UNIQUE_ID));
            // 处理设置注解上的参数
            Object[] args = point.getArgs();
            setRequestValue(log, args);

            if(result instanceof ApiResult){
                log.setStatus(((ApiResult) result).getCode());
            }else {
                log.setStatus(ApiResultCode.SUCCESS.getCode());
            }
            if(recordResponse){
                setResponseValue(log, result);
            }
            // 发送日志
            amqpClient.sendLog(log);
        } catch (Exception e){
            log.error("记录操作日志失败", e);
        }
    }

    /**
     *  获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(LogMessage operLog, Object[] args) throws Exception {
        List<?> param = new ArrayList<>(Arrays.asList(args)).stream().filter(p -> !(p instanceof ServletResponse))
                .collect(Collectors.toList());
        String params = JSON.toJSONString(param, false);
        operLog.setRequest(params);
    }

    /**
     *  记录结果，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setResponseValue(LogMessage operLog, Object result) throws Exception {
        if(result == null){
            return;
        }
        String response = JSON.toJSONString(result, false);
        operLog.setResponse(response);
    }
}



