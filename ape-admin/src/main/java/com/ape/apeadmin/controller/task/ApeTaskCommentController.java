package com.ape.apeadmin.controller.comment;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.ApeTaskComment;
import com.ape.apesystem.domain.ApeUser;
import com.ape.apesystem.service.ApeTaskCommentService;
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
 * @description: 课程评论controller
 * @date 2023/11/21 08:12
 */
@Controller
@ResponseBody
@RequestMapping("comment")
public class ApeTaskCommentController {

    @Autowired
    private ApeTaskCommentService apeTaskCommentService;

    /** 分页获取课程评论 */
    @Log(name = "分页获取课程评论", type = BusinessType.OTHER)
    @PostMapping("getApeTaskCommentPage")
    public Result getApeTaskCommentPage(@RequestBody ApeTaskComment apeTaskComment) {
        Page<ApeTaskComment> page = new Page<>(apeTaskComment.getPageNumber(),apeTaskComment.getPageSize());
        QueryWrapper<ApeTaskComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(apeTaskComment.getTaskId()),ApeTaskComment::getTaskId,apeTaskComment.getTaskId())
                .like(StringUtils.isNotBlank(apeTaskComment.getContent()),ApeTaskComment::getContent,apeTaskComment.getContent())
                .like(StringUtils.isNotBlank(apeTaskComment.getCreateBy()),ApeTaskComment::getCreateBy,apeTaskComment.getCreateBy());
        Page<ApeTaskComment> apeTaskCommentPage = apeTaskCommentService.page(page, queryWrapper);
        return Result.success(apeTaskCommentPage);
    }

    /** 根据id获取课程评论 */
    @Log(name = "根据id获取课程评论", type = BusinessType.OTHER)
    @GetMapping("getApeTaskCommentById")
    public Result getApeTaskCommentById(@RequestParam("id")String id) {
        ApeTaskComment apeTaskComment = apeTaskCommentService.getById(id);
        return Result.success(apeTaskComment);
    }

    @GetMapping("getApeTaskCommentListByTaskId")
    public Result getApeTaskCommentListByTaskId(@RequestParam("id")String id) {
        QueryWrapper<ApeTaskComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeTaskComment::getTaskId,id).orderByDesc(ApeTaskComment::getCreateTime);
        List<ApeTaskComment> commentList = apeTaskCommentService.list(queryWrapper);
        return Result.success(commentList);
    }

    /** 保存课程评论 */
    @Log(name = "保存课程评论", type = BusinessType.INSERT)
    @PostMapping("saveApeTaskComment")
    public Result saveApeTaskComment(@RequestBody ApeTaskComment apeTaskComment) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        apeTaskComment.setAvatar(userInfo.getAvatar());
        boolean save = apeTaskCommentService.save(apeTaskComment);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑课程评论 */
    @Log(name = "编辑课程评论", type = BusinessType.UPDATE)
    @PostMapping("editApeTaskComment")
    public Result editApeTaskComment(@RequestBody ApeTaskComment apeTaskComment) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        apeTaskComment.setAvatar(userInfo.getAvatar());
        boolean save = apeTaskCommentService.updateById(apeTaskComment);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除课程评论 */
    @GetMapping("removeApeTaskComment")
    @Log(name = "删除课程评论", type = BusinessType.DELETE)
    public Result removeApeTaskComment(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeTaskCommentService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("课程评论id不能为空！");
        }
    }

}