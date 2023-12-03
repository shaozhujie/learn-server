package com.ape.apeadmin.controller.article;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apesystem.domain.ApeArticle;
import com.ape.apesystem.domain.ApeArticleFavor;
import com.ape.apesystem.service.ApeArticleFavorService;
import com.ape.apesystem.service.ApeArticleService;
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
 * @description: 笔记收藏controller
 * @date 2023/11/22 08:59
 */
@Controller
@ResponseBody
@RequestMapping("favor")
public class ApeArticleFavorController {

    @Autowired
    private ApeArticleFavorService apeArticleFavorService;
    @Autowired
    private ApeArticleService apeArticleService;

    /** 分页获取笔记收藏 */
    @Log(name = "分页获取笔记收藏", type = BusinessType.OTHER)
    @PostMapping("getApeArticleFavorPage")
    public Result getApeArticleFavorPage(@RequestBody ApeArticleFavor apeArticleFavor) {
        Page<ApeArticleFavor> page = new Page<>(apeArticleFavor.getPageNumber(),apeArticleFavor.getPageSize());
        QueryWrapper<ApeArticleFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(apeArticleFavor.getArticleId()),ApeArticleFavor::getArticleId,apeArticleFavor.getArticleId())
                .eq(StringUtils.isNotBlank(apeArticleFavor.getUserId()),ApeArticleFavor::getUserId,apeArticleFavor.getUserId());
        Page<ApeArticleFavor> apeArticleFavorPage = apeArticleFavorService.page(page, queryWrapper);
        for (ApeArticleFavor articleFavor : apeArticleFavorPage.getRecords()) {
            ApeArticle article = apeArticleService.getById(articleFavor.getArticleId());
            articleFavor.setTitle(article.getTitle());
            articleFavor.setCreateBy(article.getCreateBy());
            articleFavor.setCreateTime(article.getCreateTime());
            articleFavor.setAvatar(article.getAvatar());
            articleFavor.setArticleDesc(article.getArticleDesc());
        }
        return Result.success(apeArticleFavorPage);
    }

    /** 根据id获取笔记收藏 */
    @Log(name = "根据id获取笔记收藏", type = BusinessType.OTHER)
    @GetMapping("getApeArticleFavorById")
    public Result getApeArticleFavorById(@RequestParam("id")String id) {
        ApeArticleFavor apeArticleFavor = apeArticleFavorService.getById(id);
        return Result.success(apeArticleFavor);
    }

    /** 保存笔记收藏 */
    @Log(name = "保存笔记收藏", type = BusinessType.INSERT)
    @PostMapping("saveApeArticleFavor")
    public Result saveApeArticleFavor(@RequestBody ApeArticleFavor apeArticleFavor) {
        boolean save = apeArticleFavorService.save(apeArticleFavor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑笔记收藏 */
    @Log(name = "编辑笔记收藏", type = BusinessType.UPDATE)
    @PostMapping("editApeArticleFavor")
    public Result editApeArticleFavor(@RequestBody ApeArticleFavor apeArticleFavor) {
        boolean save = apeArticleFavorService.updateById(apeArticleFavor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除笔记收藏 */
    @PostMapping("removeApeArticleFavor")
    @Log(name = "删除笔记收藏", type = BusinessType.DELETE)
    public Result removeApeArticleFavor(@RequestBody ApeArticleFavor apeArticleFavor) {
        QueryWrapper<ApeArticleFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeArticleFavor::getArticleId,apeArticleFavor.getArticleId())
                .eq(ApeArticleFavor::getUserId,apeArticleFavor.getUserId());
        boolean remove = apeArticleFavorService.remove(queryWrapper);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

}