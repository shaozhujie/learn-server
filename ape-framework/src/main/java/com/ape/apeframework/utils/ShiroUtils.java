package com.ape.apeframework.utils;

import com.ape.apesystem.domain.ApeUser;
import org.apache.shiro.SecurityUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: shiro工具类
 * @date 2023/9/12 10:52
 */
public class ShiroUtils {

    /**
    * @description: 获取当前登陆用户
    * @param:
    * @return:
    * @author shaozhujie
    * @date: 2023/9/12 10:54
    */
    public static ApeUser getUserInfo(){
        return (ApeUser) SecurityUtils.getSubject().getPrincipal();
    }

}
