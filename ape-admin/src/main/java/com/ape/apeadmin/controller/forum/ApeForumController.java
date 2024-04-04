package com.ape.apeadmin.controller.forum;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.ApeForum;
import com.ape.apesystem.domain.ApeUser;
import com.ape.apesystem.service.ApeForumService;
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
 * @description: 论坛controller
 * @date 2024/01/18 08:55
 */
@Controller
@ResponseBody
@RequestMapping("forum")
public class ApeForumController {

    @Autowired
    private ApeForumService apeForumService;

    /** 分页获取论坛 */
    @Log(name = "分页获取论坛", type = BusinessType.OTHER)
    @PostMapping("getApeForumPage")
    public Result getApeForumPage(@RequestBody ApeForum apeForum) {
        Page<ApeForum> page = new Page<>(apeForum.getPageNumber(),apeForum.getPageSize());
        QueryWrapper<ApeForum> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(apeForum.getName()),ApeForum::getName,apeForum.getName())
                .like(StringUtils.isNotBlank(apeForum.getContent()),ApeForum::getContent,apeForum.getContent())
                .eq(StringUtils.isNotBlank(apeForum.getUserId()),ApeForum::getUserId,apeForum.getUserId())
                .orderByDesc(ApeForum::getCreateTime);
        Page<ApeForum> apeForumPage = apeForumService.page(page, queryWrapper);
        return Result.success(apeForumPage);
    }

    /** 根据id获取论坛 */
    @Log(name = "根据id获取论坛", type = BusinessType.OTHER)
    @GetMapping("getApeForumById")
    public Result getApeForumById(@RequestParam("id")String id) {
        ApeForum apeForum = apeForumService.getById(id);
        return Result.success(apeForum);
    }

    /** 保存论坛 */
    @Log(name = "保存论坛", type = BusinessType.INSERT)
    @PostMapping("saveApeForum")
    public Result saveApeForum(@RequestBody ApeForum apeForum) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        apeForum.setUserId(userInfo.getId());
        boolean save = apeForumService.save(apeForum);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑论坛 */
    @Log(name = "编辑论坛", type = BusinessType.UPDATE)
    @PostMapping("editApeForum")
    public Result editApeForum(@RequestBody ApeForum apeForum) {
        boolean save = apeForumService.updateById(apeForum);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除论坛 */
    @GetMapping("removeApeForum")
    @Log(name = "删除论坛", type = BusinessType.DELETE)
    public Result removeApeForum(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeForumService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("论坛id不能为空！");
        }
    }

}