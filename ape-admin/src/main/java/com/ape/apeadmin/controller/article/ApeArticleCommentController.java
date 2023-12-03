package com.ape.apeadmin.controller.article;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.ApeArticleComment;
import com.ape.apesystem.domain.ApeUser;
import com.ape.apesystem.service.ApeArticleCommentService;
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
 * @description: 笔记评论controller
 * @date 2023/11/21 10:09
 */
@Controller
@ResponseBody
@RequestMapping("articleComment")
public class ApeArticleCommentController {

    @Autowired
    private ApeArticleCommentService apeArticleCommentService;

    /** 分页获取笔记评论 */
    @Log(name = "分页获取笔记评论", type = BusinessType.OTHER)
    @PostMapping("getApeArticleCommentPage")
    public Result getApeArticleCommentPage(@RequestBody ApeArticleComment apeArticleComment) {
        Page<ApeArticleComment> page = new Page<>(apeArticleComment.getPageNumber(),apeArticleComment.getPageSize());
        QueryWrapper<ApeArticleComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(apeArticleComment.getTaskId()),ApeArticleComment::getTaskId,apeArticleComment.getTaskId())
                .like(StringUtils.isNotBlank(apeArticleComment.getContent()),ApeArticleComment::getContent,apeArticleComment.getContent())
                .like(StringUtils.isNotBlank(apeArticleComment.getCreateBy()),ApeArticleComment::getCreateBy,apeArticleComment.getCreateBy());
        Page<ApeArticleComment> apeArticleCommentPage = apeArticleCommentService.page(page, queryWrapper);
        return Result.success(apeArticleCommentPage);
    }

    /** 根据id获取笔记评论 */
    @Log(name = "根据id获取笔记评论", type = BusinessType.OTHER)
    @GetMapping("getApeArticleCommentById")
    public Result getApeArticleCommentById(@RequestParam("id")String id) {
        ApeArticleComment apeArticleComment = apeArticleCommentService.getById(id);
        return Result.success(apeArticleComment);
    }

    @GetMapping("getApeArticleCommentByArticleId")
    public Result getApeArticleCommentByArticleId(@RequestParam("id")String id) {
        QueryWrapper<ApeArticleComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeArticleComment::getTaskId,id).orderByDesc(ApeArticleComment::getCreateTime);
        List<ApeArticleComment> commentList = apeArticleCommentService.list(queryWrapper);
        return Result.success(commentList);
    }

    /** 保存笔记评论 */
    @Log(name = "保存笔记评论", type = BusinessType.INSERT)
    @PostMapping("saveApeArticleComment")
    public Result saveApeArticleComment(@RequestBody ApeArticleComment apeArticleComment) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        apeArticleComment.setAvatar(userInfo.getAvatar());
        boolean save = apeArticleCommentService.save(apeArticleComment);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑笔记评论 */
    @Log(name = "编辑笔记评论", type = BusinessType.UPDATE)
    @PostMapping("editApeArticleComment")
    public Result editApeArticleComment(@RequestBody ApeArticleComment apeArticleComment) {
        boolean save = apeArticleCommentService.updateById(apeArticleComment);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除笔记评论 */
    @GetMapping("removeApeArticleComment")
    @Log(name = "删除笔记评论", type = BusinessType.DELETE)
    public Result removeApeArticleComment(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeArticleCommentService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("笔记评论id不能为空！");
        }
    }

}