package com.ape.apeframework.aspectj;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.enums.BusinessStatus;
import com.ape.apecommon.utils.UserAgentUtil;
import com.ape.apeframework.event.LogEvent;
import com.ape.apeframework.utils.RequestUtils;
import com.ape.apeframework.utils.ServletUtils;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.ApeLoginLog;
import com.ape.apesystem.domain.ApeOperateLog;
import com.ape.apesystem.domain.ApeUser;
import com.ape.apesystem.service.ApeLoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 记录操作日志
 * @date 2023/9/21 16:47
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Resource
    private ApplicationEventPublisher eventPublisher;

    /**
     * 以 controller 包下定义的所有请求为切入点
     */
    @Pointcut("@annotation(com.ape.apecommon.annotation.Log)")
    public void logPointCut() {
    }

    @AfterReturning(pointcut = "logPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        handleLog(joinPoint, null, result);
    }

    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }

            ApeUser currentUser = ShiroUtils.getUserInfo();

            ApeOperateLog operLog = new ApeOperateLog();
            operLog.setState(BusinessStatus.SUCCESS.ordinal());
            operLog.setOperTime(new Date());

            ServletRequestAttributes servletContainer = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletContainer.getRequest();
            String ipAddr = RequestUtils.getRemoteHost(request);
            operLog.setOperIp(ipAddr);
            // 返回参数
            operLog.setResultParam(JSON.toJSONString(jsonResult));

            operLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
            if (currentUser != null) {
                operLog.setOperUserAccount(currentUser.getLoginAccount());
                operLog.setOperUserId(currentUser.getId());
                operLog.setOperUserName(currentUser.getUserName());
            }
            if (e != null) {
                operLog.setState(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operLog.setRequestType(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(controllerLog, operLog);
            // 保存数据库
            eventPublisher.publishEvent(new LogEvent(operLog));
        } catch (Exception exp) {
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    public void getControllerMethodDescription(Log log, ApeOperateLog operLog) throws Exception {
        // 设置action动作
        operLog.setType(log.type().ordinal());
        // 设置标题
        operLog.setName(log.name());
        // 设置操作人类别
        operLog.setOperType(log.operType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(operLog);
        }
    }

    private void setRequestValue(ApeOperateLog operLog) throws Exception {
        Map<String, String[]> map = ServletUtils.getRequest().getParameterMap();
        String params = JSON.toJSONString(map);
        operLog.setOperParam(StringUtils.substring(params, 0, 2000));
    }

    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

}
