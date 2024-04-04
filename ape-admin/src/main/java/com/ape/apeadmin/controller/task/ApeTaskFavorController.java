package com.ape.apeadmin.controller.task;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.ApeTask;
import com.ape.apesystem.domain.ApeTaskFavor;
import com.ape.apesystem.domain.ApeUser;
import com.ape.apesystem.service.ApeTaskFavorService;
import com.ape.apesystem.service.ApeTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 课程收藏controller
 * @date 2024/01/18 01:51
 */
@Controller
@ResponseBody
@RequestMapping("favor")
public class ApeTaskFavorController {

    @Autowired
    private ApeTaskFavorService apeTaskFavorService;
    @Autowired
    private ApeTaskService apeTaskService;

    /** 分页获取课程收藏 */
    @Log(name = "分页获取课程收藏", type = BusinessType.OTHER)
    @PostMapping("getApeTaskFavorPage")
    public Result getApeTaskFavorPage(@RequestBody ApeTaskFavor apeTaskFavor) {
        ApeUser user = ShiroUtils.getUserInfo();
        apeTaskFavor.setUserId(user.getId());
        Page<ApeTaskFavor> page = new Page<>(apeTaskFavor.getPageNumber(),apeTaskFavor.getPageSize());
        QueryWrapper<ApeTaskFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(apeTaskFavor.getTaskId()),ApeTaskFavor::getTaskId,apeTaskFavor.getTaskId())
                .eq(StringUtils.isNotBlank(apeTaskFavor.getUserId()),ApeTaskFavor::getUserId,apeTaskFavor.getUserId())
                .eq(StringUtils.isNotBlank(apeTaskFavor.getTaskName()),ApeTaskFavor::getTaskName,apeTaskFavor.getTaskName())
                .eq(StringUtils.isNotBlank(apeTaskFavor.getTaskImage()),ApeTaskFavor::getTaskImage,apeTaskFavor.getTaskImage())
                .eq(StringUtils.isNotBlank(apeTaskFavor.getTaskDesc()),ApeTaskFavor::getTaskDesc,apeTaskFavor.getTaskDesc());
        Page<ApeTaskFavor> apeTaskFavorPage = apeTaskFavorService.page(page, queryWrapper);
        return Result.success(apeTaskFavorPage);
    }



    /** 根据id获取课程收藏 */
    @Log(name = "根据id获取课程收藏", type = BusinessType.OTHER)
    @GetMapping("getApeTaskFavorById")
    public Result getApeTaskFavorById(@RequestParam("taskId")String taskId,@RequestParam("userId")String userId) {
        QueryWrapper<ApeTaskFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeTaskFavor::getTaskId,taskId).eq(ApeTaskFavor::getUserId,userId).last("limit 1");
        ApeTaskFavor apeTaskFavor = apeTaskFavorService.getOne(queryWrapper);
        if (apeTaskFavor != null) {
            return Result.success(apeTaskFavor);
        } else {
            return Result.fail();
        }
    }

    /** 保存课程收藏 */
    @Log(name = "保存课程收藏", type = BusinessType.INSERT)
    @PostMapping("saveApeTaskFavor")
    public Result saveApeTaskFavor(@RequestBody ApeTaskFavor apeTaskFavor) {
        ApeTask task = apeTaskService.getById(apeTaskFavor.getTaskId());
        apeTaskFavor.setTaskDesc(task.getTaskDescribe());
        apeTaskFavor.setTaskName(task.getName());
        apeTaskFavor.setTaskImage(task.getImage());
        boolean save = apeTaskFavorService.save(apeTaskFavor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑课程收藏 */
    @Log(name = "编辑课程收藏", type = BusinessType.UPDATE)
    @PostMapping("editApeTaskFavor")
    public Result editApeTaskFavor(@RequestBody ApeTaskFavor apeTaskFavor) {
        boolean save = apeTaskFavorService.updateById(apeTaskFavor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除课程收藏 */
    @GetMapping("removeApeTaskFavor")
    @Log(name = "删除课程收藏", type = BusinessType.DELETE)
    public Result removeApeTaskFavor(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeTaskFavorService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("课程收藏id不能为空！");
        }
    }

}