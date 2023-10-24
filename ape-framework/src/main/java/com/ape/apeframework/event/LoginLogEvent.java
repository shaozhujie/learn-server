package com.ape.apeframework.event;

import com.ape.apesystem.domain.ApeLoginLog;
import com.ape.apesystem.domain.ApeOperateLog;
import org.springframework.context.ApplicationEvent;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 登陆日志监听类
 * @date 2023/9/23 10:06
 */
public class LoginLogEvent extends ApplicationEvent {

    private ApeLoginLog source;

    public LoginLogEvent(ApeLoginLog source) {
        super(source);
        this.source = source;
    }

    @Override
    public ApeLoginLog getSource() {
        return source;
    }

    public void setSource(ApeLoginLog source) {
        this.source = source;
    }
}
