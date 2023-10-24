package com.ape.apeframework.config;

import com.ape.apecommon.constant.Constants;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apecommon.utils.JwtUtil;
import com.ape.apeframework.custom.JwtToken;
import com.ape.apesystem.domain.ApeUser;
import com.ape.apesystem.service.ApeRoleMenuService;
import com.ape.apesystem.service.ApeUserRoleService;
import com.ape.apesystem.service.ApeUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 自定义realm
 * @date 2023/9/7 14:38
 */
@Component
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private ApeUserService apeUserService;

    @Autowired
    private ApeUserRoleService apeUserRoleService;

    @Autowired
    private ApeRoleMenuService apeRoleMenuService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
    * @description: 授权
    * @param: principals
    * @return:
    * @author shaozhujie
    * @date: 2023/9/7 15:11
    */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //根据用户名从自己的数据库中获取role和permission信息
        ApeUser apeUser = null;
        String loginAccount = null;
        if (principals != null) {
            apeUser = (ApeUser) principals.getPrimaryPrincipal();
            loginAccount = apeUser.getLoginAccount();
        }
        // 设置用户拥有的角色集合，比如“admin,test”
        Set<String> roleSet = apeUserRoleService.getUserRolesSet(loginAccount);
        simpleAuthorizationInfo.setRoles(roleSet);
        for (String role : roleSet) {
            // 设置用户拥有的权限集合，比如“sys:role:add,sys:user:add”
            Set<String> menuSet = apeRoleMenuService.getRoleMenusSet(role);
            simpleAuthorizationInfo.addStringPermissions(menuSet);
        }
        return simpleAuthorizationInfo;
    }

    /**
    * @description: 认证
    * @param: token
    * @return:
    * @author shaozhujie
    * @date: 2023/9/7 15:11
    */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();
        if (accessToken == null) {
            throw new AuthenticationException(ResultCode.COMMON_NO_TOKEN.getMessage());
        }
        // 校验token有效性
        ApeUser tokenEntity = this.checkUserTokenIsEffect(accessToken);
        return new SimpleAuthenticationInfo(tokenEntity, accessToken, getName());
    }

    /**
    * @description: * 校验token的有效性
     *springboot2.3.+新增了一个配置项server.error.includeMessage，默认是NEVER，
     *因此默认是不是输出message的，只要开启就可以了,否则无法拿到shiro抛出异常信息message
    * @param: token
    * @return:
    * @author shaozhujie
    * @date: 2023/9/14 11:12
    */
    public ApeUser checkUserTokenIsEffect(String token) throws AuthenticationException {
        // 解密获得username，用于和数据库进行对比
        String userId = JwtUtil.getUserId(token);
        if (userId == null) {
            throw new AuthenticationException(ResultCode.COMMON_TOKEN_ILLEGAL.getMessage());
        }

        // 查询用户信息
        ApeUser loginUser = apeUserService.getById(userId);
        if (loginUser == null) {
            throw new UnknownAccountException(ResultCode.COMMON_USER_NOT_EXIST.getMessage());
        }
        // 判断用户状态
        if (loginUser.getStatus() != 0) {
            throw new LockedAccountException(ResultCode.COMMON_ACCOUNT_LOCKED.getMessage());
        }
        // 校验token是否超时失效 & 或者账号密码是否错误
        if (!jwtTokenRefresh(token, userId, loginUser.getPassword())) {
            throw new IncorrectCredentialsException(ResultCode.COMMON_TOKEN_FAILURE.getMessage());
        }
        return loginUser;
    }

    /**
    * @description: * JWTToken刷新生命周期 （实现： 用户在线操作不掉线功能）
     * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)，缓存有效期设置为Jwt有效时间的2倍
     * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
     * 3、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
     * 4、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
     * 注意： 前端请求Header中设置Authorization保持不变，校验有效性以缓存中的token为准。
     * 用户过期时间 = Jwt有效时间 * 2。
    * @param: token
    	userId
    	password
    * @return:
    * @author shaozhujie
    * @date: 2023/9/14 11:12
    */
    public boolean jwtTokenRefresh(String token, String userId, String password) {
        //如果缓存中的token为空，直接返回失效异常
        String cacheToken = stringRedisTemplate.opsForValue().get(Constants.PREFIX_USER_TOKEN + userId);
        if (!StringUtils.isBlank(cacheToken)) {
            // 校验token有效性
            if (!JwtUtil.verify(cacheToken, userId, password)) {
                JwtUtil.sign(userId, password);
            }
            return true;
        }
        return false;
    }

    /**
    * @description: 清除当前用户的权限认证缓存
    * @param: principals
    * @return:
    * @author shaozhujie
    * @date: 2023/9/7 15:10
    */
    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

}
