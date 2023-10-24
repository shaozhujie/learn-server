package com.ape.apeframework.event.listener;

import com.ape.apeframework.config.AsyncPoolConfig;
import com.ape.apeframework.event.LogEvent;
import com.ape.apeframework.event.LoginLogEvent;
import com.ape.apesystem.domain.ApeLoginLog;
import com.ape.apesystem.domain.ApeOperateLog;
import com.ape.apesystem.service.ApeLoginLogService;
import com.ape.apesystem.service.ApeOperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 监听事件
 * @date 2023/9/22 10:59
 */
@Slf4j
@Component
public class SystemEventListener {

    @Autowired
    private ApeOperateLogService apeOperateLogService;
    @Autowired
    private ApeLoginLogService apeLoginLogService;

    @Async(value = AsyncPoolConfig.TASK_EXECUTOR_NAME)
    @EventListener(classes = LogEvent.class)
    public void handleLogEvent(LogEvent event) {
        //处理日志业务逻辑
        ApeOperateLog operateLog = event.getSource();
        apeOperateLogService.save(operateLog);
    }

    @Async(value = AsyncPoolConfig.TASK_EXECUTOR_NAME)
    @EventListener(classes = LoginLogEvent.class)
    public void handleLoginLogEvent(LoginLogEvent event) {
        //处理日志业务逻辑
        ApeLoginLog loginLog = event.getSource();
        apeLoginLogService.save(loginLog);
    }


}
