package com.ape.apeadmin.controller.login;

import com.alibaba.fastjson2.JSONObject;
import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.constant.Constants;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.utils.JwtUtil;
import com.ape.apecommon.utils.PasswordUtils;
import com.ape.apecommon.utils.UserAgentUtil;
import com.ape.apeframework.event.LoginLogEvent;
import com.ape.apeframework.utils.RedisUtils;
import com.ape.apeframework.utils.RequestUtils;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.ApeLoginLog;
import com.ape.apesystem.domain.ApeMenu;
import com.ape.apesystem.domain.ApeUser;
import com.ape.apesystem.service.ApeLoginLogService;
import com.ape.apesystem.service.ApeMenuService;
import com.ape.apesystem.service.ApeUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 登陆
 * @date 2023/9/8 9:04
 */
@Controller
@ResponseBody
@RequestMapping("login")
public class LoginController {

    @Autowired
    private ApeUserService apeUserService;

    @Autowired
    private ApeLoginLogService apeLoginLogService;

    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ApeMenuService apeMenuService;

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping()
    public Result login(HttpServletRequest request,@RequestBody JSONObject jsonObject) {
        String ipAddr = RequestUtils.getRemoteHost(request);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        QueryWrapper<ApeUser> query = new QueryWrapper<>();
        query.lambda().eq(ApeUser::getLoginAccount,username);
        ApeUser apeUser = apeUserService.getOne(query);
        if (apeUser == null) {
            saveLoginLog(request,"用户名不存在",username,ipAddr,1);
            return Result.fail("用户名不存在！");
        }
        //比较加密后得密码
        boolean decrypt = PasswordUtils.decrypt(password, apeUser.getPassword() + "$" + apeUser.getSalt());
        if (!decrypt) {
            saveLoginLog(request,"用户名或密码错误",username,ipAddr,1);
            return Result.fail("用户名或密码错误！");
        }
        if (apeUser.getStatus() == 1) {
            saveLoginLog(request,"用户被禁用",username,ipAddr,1);
            return Result.fail("用户被禁用！");
        }
        //密码正确生成token返回
        String token = JwtUtil.sign(apeUser.getId(), password);
        JSONObject json = new JSONObject();
        json.put("token", token);
        saveLoginLog(request,"登陆成功",username,ipAddr,0);
        return Result.success(json);
    }

    @PostMapping("register")
    public Result register(@RequestBody ApeUser apeUser) {
        boolean account = checkAccount(apeUser);
        if (!account) {
            return Result.fail("登陆账号已存在不可重复！");
        }
        //密码加盐加密
        String encrypt = PasswordUtils.encrypt(apeUser.getPassword());
        String[] split = encrypt.split("\\$");
        apeUser.setPassword(split[0]);
        apeUser.setSalt(split[1]);
        apeUser.setAvatar("/img/avatar.jpg");
        apeUser.setPwdUpdateDate(new Date());
        boolean save = apeUserService.save(apeUser);
        if (save) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    /** 校验用户 */
    public boolean checkAccount(ApeUser apeUser) {
        QueryWrapper<ApeUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeUser::getLoginAccount,apeUser.getLoginAccount())
                .eq(ApeUser::getUserType,apeUser.getUserType());
        int count = apeUserService.count(queryWrapper);
        return count <= 0;
    }

    @Log(name = "登出", type = BusinessType.OTHER)
    @GetMapping("logout")
    public Result logout() {
        ApeUser user = ShiroUtils.getUserInfo();
        redisUtils.remove(Constants.PREFIX_USER_TOKEN + user.getId());
        return Result.success();
    }

    @Log(name = "验证密码", type = BusinessType.OTHER)
    @GetMapping("verPassword")
    public Result verPassword(HttpServletRequest request,@RequestParam("password") String password) {
        ApeUser user = ShiroUtils.getUserInfo();
        ApeUser apeUser = apeUserService.getById(user.getId());
        String ipAddr = RequestUtils.getRemoteHost(request);
        if (apeUser.getStatus() == 1) {
            saveLoginLog(request,"用户被禁用",apeUser.getLoginAccount(),ipAddr,1);
            return Result.fail("用户被禁用！");
        }
        boolean decrypt = PasswordUtils.decrypt(password, apeUser.getPassword() + "$" + apeUser.getSalt());
        if (!decrypt) {
            saveLoginLog(request,"验证密码错误",apeUser.getLoginAccount(),ipAddr,1);
            return Result.fail("用户名或密码错误！");
        }
        saveLoginLog(request,"验证成功",apeUser.getLoginAccount(),ipAddr,0);
        return Result.success();
    }


    public void saveLoginLog(HttpServletRequest request,String msg,String username,String ipAddr,Integer state) {
        String agent = request.getHeader("User-Agent");
        String userAgent = UserAgentUtil.getUserAgent(agent);
        String browser = UserAgentUtil.judgeBrowser(userAgent);
        ApeLoginLog apeLoginLog = new ApeLoginLog();
        apeLoginLog.setUserName(username);
        apeLoginLog.setLoginIp(ipAddr);
        apeLoginLog.setBrowser(browser);
        apeLoginLog.setOs(userAgent);
        apeLoginLog.setStatus(state);
        apeLoginLog.setLoginTime(new Date());
        apeLoginLog.setMsg(msg);
        eventPublisher.publishEvent(new LoginLogEvent(apeLoginLog));
    }

}
