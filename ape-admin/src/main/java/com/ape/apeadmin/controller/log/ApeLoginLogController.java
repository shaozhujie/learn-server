package com.ape.apeadmin.controller.log;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apesystem.domain.ApeLoginLog;
import com.ape.apesystem.service.ApeLoginLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 登陆日志controller
 * @date 2023/9/25 8:35
 */
@Controller
@ResponseBody
@RequestMapping("loginLog")
public class ApeLoginLogController {

    @Autowired
    private ApeLoginLogService apeLoginLogService;

    /** 查询 */
    @Log(name = "查询登陆日志", type = BusinessType.OTHER)
    @PostMapping("getLogPage")
    public Result getLogPage(@RequestBody ApeLoginLog apeLoginLog) {
        Page<ApeLoginLog> page = new Page<>(apeLoginLog.getPageNumber(),apeLoginLog.getPageSize());
        QueryWrapper<ApeLoginLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(apeLoginLog.getUserName()),ApeLoginLog::getUserName,apeLoginLog.getUserName())
                .eq(apeLoginLog.getStatus() != null,ApeLoginLog::getStatus,apeLoginLog.getStatus())
                .ge(apeLoginLog.getStartTime() != null,ApeLoginLog::getLoginTime,apeLoginLog.getStartTime())
                .le(apeLoginLog.getEndTime() != null,ApeLoginLog::getLoginTime,apeLoginLog.getEndTime())
                .orderByDesc(ApeLoginLog::getLoginTime);
        Page<ApeLoginLog> logPage = apeLoginLogService.page(page, queryWrapper);
        return Result.success(logPage);
    }

    /** 删除 */
    @Log(name = "删除登陆日志", type = BusinessType.DELETE)
    @GetMapping("removeLog")
    public Result removeLog(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeLoginLogService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("日志id不能为空！");
        }
    }

    /** 清空 */
    @Log(name = "清空登陆日志", type = BusinessType.DELETE)
    @GetMapping("clearLog")
    public Result clearLog() {
        boolean remove = apeLoginLogService.remove(null);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

}
