package com.ape.apeadmin.controller.article;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.*;
import com.ape.apesystem.service.ApeArticleCommentService;
import com.ape.apesystem.service.ApeArticleFavorService;
import com.ape.apesystem.service.ApeArticleService;
import com.ape.apesystem.service.ApeTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 笔记controller
 * @date 2023/11/20 09:14
 */
@Controller
@ResponseBody
@RequestMapping("article")
public class ApeArticleController {

    @Autowired
    private ApeArticleService apeArticleService;
    @Autowired
    private ApeTaskService apeTaskService;
    @Autowired
    private ApeArticleFavorService apeArticleFavorService;
    @Autowired
    private ApeArticleCommentService apeArticleCommentService;

    /** 分页获取笔记 */
    @Log(name = "分页获取笔记", type = BusinessType.OTHER)
    @PostMapping("getApeArticlePage")
    public Result getApeArticlePage(@RequestBody ApeArticle apeArticle) {
        Page<ApeArticle> page = new Page<>(apeArticle.getPageNumber(),apeArticle.getPageSize());
        QueryWrapper<ApeArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(apeArticle.getUserId()),ApeArticle::getUserId,apeArticle.getUserId())
                .like(StringUtils.isNotBlank(apeArticle.getTitle()),ApeArticle::getTitle,apeArticle.getTitle())
                .eq(apeArticle.getState() != null,ApeArticle::getState,apeArticle.getState())
                .like(StringUtils.isNotBlank(apeArticle.getTaskName()),ApeArticle::getTaskName,apeArticle.getTaskName());
        if (apeArticle.getType() == 1) {
            QueryWrapper<ApeTask> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ApeTask::getTeacherId,ShiroUtils.getUserInfo().getId());
            List<ApeTask> taskList = apeTaskService.list(wrapper);
            List<String> list = new ArrayList<String>();
            for (ApeTask apeTask : taskList) {
                list.add(apeTask.getId());
            }
            if (list.size()>0) {
                queryWrapper.lambda().in(ApeArticle::getTaskId,list);
            } else {
                list.add(" ");
                queryWrapper.lambda().in(ApeArticle::getTaskId,list);
            }
        }
        Page<ApeArticle> apeArticlePage = apeArticleService.page(page, queryWrapper);
        return Result.success(apeArticlePage);
    }

    @GetMapping("getIndexArticleList")
    public Result getIndexArticleList() {
        QueryWrapper<ApeArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().last("limit 2");
        List<ApeArticle> articleList = apeArticleService.list(queryWrapper);
        return Result.success(articleList);
    }

    /** 根据id获取笔记 */
    @Log(name = "根据id获取笔记", type = BusinessType.OTHER)
    @GetMapping("getApeArticleById")
    public Result getApeArticleById(@RequestParam("id")String id) {
        ApeArticle apeArticle = apeArticleService.getById(id);
        ApeUser userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<ApeArticleFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeArticleFavor::getArticleId,id).eq(ApeArticleFavor::getUserId,userInfo.getId());
        ApeArticleFavor favor = apeArticleFavorService.getOne(queryWrapper);
        if (favor == null) {
            apeArticle.setFavor(0);
        } else {
            apeArticle.setFavor(1);
        }
        return Result.success(apeArticle);
    }

    /** 保存笔记 */
    @Log(name = "保存笔记", type = BusinessType.INSERT)
    @PostMapping("saveApeArticle")
    public Result saveApeArticle(@RequestBody ApeArticle apeArticle) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        apeArticle.setUserId(userInfo.getId());
        apeArticle.setAvatar(userInfo.getAvatar());
        if (StringUtils.isNotBlank(apeArticle.getTaskId())) {
            ApeTask task = apeTaskService.getById(apeArticle.getTaskId());
            apeArticle.setTaskName(task.getName());
        }
        boolean save = apeArticleService.save(apeArticle);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑笔记 */
    @Log(name = "编辑笔记", type = BusinessType.UPDATE)
    @PostMapping("editApeArticle")
    public Result editApeArticle(@RequestBody ApeArticle apeArticle) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        apeArticle.setAvatar(userInfo.getAvatar());
        if (StringUtils.isNotBlank(apeArticle.getTaskId())) {
            ApeTask task = apeTaskService.getById(apeArticle.getTaskId());
            apeArticle.setTaskName(task.getName());
        }
        boolean save = apeArticleService.updateById(apeArticle);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除笔记 */
    @GetMapping("removeApeArticle")
    @Log(name = "删除笔记", type = BusinessType.DELETE)
    public Result removeApeArticle(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeArticleService.removeById(id);
                QueryWrapper<ApeArticleComment> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(ApeArticleComment::getTaskId,id);
                apeArticleCommentService.remove(queryWrapper);
                QueryWrapper<ApeArticleFavor> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().eq(ApeArticleFavor::getArticleId,id);
                apeArticleFavorService.remove(queryWrapper2);
            }
            return Result.success();
        } else {
            return Result.fail("笔记id不能为空！");
        }
    }

}