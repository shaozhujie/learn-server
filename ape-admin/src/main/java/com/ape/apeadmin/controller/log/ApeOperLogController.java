package com.ape.apeadmin.controller.log;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apesystem.domain.ApeLoginLog;
import com.ape.apesystem.domain.ApeOperateLog;
import com.ape.apesystem.service.ApeOperateLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 操作日志controller
 * @date 2023/9/25 8:43
 */
@Controller
@ResponseBody
@RequestMapping("operLog")
public class ApeOperLogController {

    @Autowired
    private ApeOperateLogService apeOperateLogService;

    /** 查询 */
    @Log(name = "查询操作日志", type = BusinessType.OTHER)
    @PostMapping("getLogPage")
    public Result getLogPage(@RequestBody ApeOperateLog apeOperateLog) {
        Page<ApeOperateLog> page = new Page<>(apeOperateLog.getPageNumber(),apeOperateLog.getPageSize());
        QueryWrapper<ApeOperateLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(apeOperateLog.getType()!=null,ApeOperateLog::getType,apeOperateLog.getType())
                .like(StringUtils.isNotBlank(apeOperateLog.getOperUserAccount()),ApeOperateLog::getOperUserAccount,apeOperateLog.getOperUserAccount())
                .ge(apeOperateLog.getStartTime() != null,ApeOperateLog::getOperTime,apeOperateLog.getStartTime())
                .le(apeOperateLog.getEndTime() != null,ApeOperateLog::getOperTime,apeOperateLog.getEndTime())
                .orderByDesc(ApeOperateLog::getOperTime);
        Page<ApeOperateLog> logPage = apeOperateLogService.page(page, queryWrapper);
        return Result.success(logPage);
    }

    /** 删除 */
    @Log(name = "删除操作日志", type = BusinessType.DELETE)
    @GetMapping("removeLog")
    public Result removeLog(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeOperateLogService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("日志id不能为空！");
        }
    }

    /** 清空 */
    @Log(name = "清空操作日志", type = BusinessType.DELETE)
    @GetMapping("clearLog")
    public Result clearLog() {
        boolean remove = apeOperateLogService.remove(null);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

}
