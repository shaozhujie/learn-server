package com.ape.apeadmin.controller.forum;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.ApeForum;
import com.ape.apesystem.domain.ApeForumItem;
import com.ape.apesystem.domain.ApeUser;
import com.ape.apesystem.service.ApeForumItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 论坛讨论controller
 * @date 2024/01/18 09:21
 */
@Controller
@ResponseBody
@RequestMapping("item")
public class ApeForumItemController {

    @Autowired
    private ApeForumItemService apeForumItemService;

    /** 分页获取论坛讨论 */
    @Log(name = "分页获取论坛讨论", type = BusinessType.OTHER)
    @PostMapping("getApeForumItemPage")
    public Result getApeForumItemPage(@RequestBody ApeForumItem apeForumItem) {
        Page<ApeForumItem> page = new Page<>(apeForumItem.getPageNumber(),apeForumItem.getPageSize());
        QueryWrapper<ApeForumItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(apeForumItem.getForumId()),ApeForumItem::getForumId,apeForumItem.getForumId())
                .like(StringUtils.isNotBlank(apeForumItem.getUserName()),ApeForumItem::getUserName,apeForumItem.getUserName())
                .eq(StringUtils.isNotBlank(apeForumItem.getCreateBy()),ApeForumItem::getCreateBy,apeForumItem.getCreateBy())
                .eq(apeForumItem.getCreateTime() != null,ApeForumItem::getCreateTime,apeForumItem.getCreateTime())
                .orderByDesc(ApeForumItem::getCreateTime);
        Page<ApeForumItem> apeForumItemPage = apeForumItemService.page(page, queryWrapper);
        return Result.success(apeForumItemPage);
    }

    @PostMapping("getApeForumItemList")
    public Result getApeForumItemList(@RequestBody ApeForumItem apeForumItem) {
        QueryWrapper<ApeForumItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(apeForumItem.getForumId()),ApeForumItem::getForumId,apeForumItem.getForumId())
                .like(StringUtils.isNotBlank(apeForumItem.getUserName()),ApeForumItem::getUserName,apeForumItem.getUserName())
                .eq(StringUtils.isNotBlank(apeForumItem.getCreateBy()),ApeForumItem::getCreateBy,apeForumItem.getCreateBy())
                .eq(apeForumItem.getCreateTime() != null,ApeForumItem::getCreateTime,apeForumItem.getCreateTime())
                .orderByDesc(ApeForumItem::getCreateTime);
        List<ApeForumItem> apeForumItemPage = apeForumItemService.list(queryWrapper);
        return Result.success(apeForumItemPage);
    }

    /** 根据id获取论坛讨论 */
    @Log(name = "根据id获取论坛讨论", type = BusinessType.OTHER)
    @GetMapping("getApeForumItemById")
    public Result getApeForumItemById(@RequestParam("id")String id) {
        ApeForumItem apeForumItem = apeForumItemService.getById(id);
        return Result.success(apeForumItem);
    }

    /** 保存论坛讨论 */
    @Log(name = "保存论坛讨论", type = BusinessType.INSERT)
    @PostMapping("saveApeForumItem")
    public Result saveApeForumItem(@RequestBody ApeForumItem apeForumItem) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        apeForumItem.setUserId(userInfo.getId());
        apeForumItem.setUserAvatar(userInfo.getAvatar());
        apeForumItem.setUserName(userInfo.getUserName());
        boolean save = apeForumItemService.save(apeForumItem);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑论坛讨论 */
    @Log(name = "编辑论坛讨论", type = BusinessType.UPDATE)
    @PostMapping("editApeForumItem")
    public Result editApeForumItem(@RequestBody ApeForumItem apeForumItem) {
        boolean save = apeForumItemService.updateById(apeForumItem);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除论坛讨论 */
    @GetMapping("removeApeForumItem")
    @Log(name = "删除论坛讨论", type = BusinessType.DELETE)
    public Result removeApeForumItem(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeForumItemService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("论坛讨论id不能为空！");
        }
    }

}