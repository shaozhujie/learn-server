package com.ape.apeadmin.controller.post;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apesystem.domain.ApePost;
import com.ape.apesystem.service.ApePostService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 岗位controller
 * @date 2023/8/30 16:33
 */
@Controller
@ResponseBody
@RequestMapping("post")
public class ApePostController {

    @Autowired
    private ApePostService apePostService;

    /** 分页获取岗位 */
    @Log(name = "分页获取岗位", type = BusinessType.OTHER)
    @PostMapping("getPostPage")
    public Result getPostPage(@RequestBody ApePost apePost){
        QueryWrapper<ApePost> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(apePost.getPostName()),ApePost::getPostName,apePost.getPostName())
                .like(StringUtils.isNotBlank(apePost.getPostCode()),ApePost::getPostCode,apePost.getPostCode())
                .eq(apePost.getStatus() != null,ApePost::getStatus,apePost.getStatus()).orderByAsc(ApePost::getPostSort);
        Page<ApePost> page = new Page<>(apePost.getPageNumber(),apePost.getPageSize());
        Page<ApePost> postPage = apePostService.page(page, queryWrapper);
        return Result.success(postPage);
    }

    /** 获取岗位列表 */
    @Log(name = "获取岗位列表", type = BusinessType.OTHER)
    @PostMapping("getPostList")
    public Result getPostList(@RequestBody ApePost apePost) {
        QueryWrapper<ApePost> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(StringUtils.isNotBlank(apePost.getPostName()),ApePost::getPostName,apePost.getPostName())
                .like(StringUtils.isNotBlank(apePost.getPostCode()),ApePost::getPostCode,apePost.getPostCode())
                .eq(apePost.getStatus() != null,ApePost::getStatus,apePost.getStatus()).orderByAsc(ApePost::getPostSort);
        List<ApePost> apePostList = apePostService.list(wrapper);
        return Result.success(apePostList);
    }

    /** 根据id获取岗位 */
    @Log(name = "根据id获取岗位", type = BusinessType.OTHER)
    @GetMapping("getPostById")
    public Result getPostById(@RequestParam("id")String id) {
        ApePost apePost = apePostService.getById(id);
        return Result.success(apePost);
    }

    /** 保存岗位 */
    @Log(name = "保存岗位", type = BusinessType.INSERT)
    @PostMapping("savePost")
    public Result savePost(@RequestBody ApePost apePost) {
        QueryWrapper<ApePost> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApePost::getPostCode,apePost.getPostCode());
        int count = apePostService.count(queryWrapper);
        if (count > 0) {
            return Result.fail("岗位编码已存在！");
        }
        boolean save = apePostService.save(apePost);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑岗位 */
    @Log(name = "编辑岗位", type = BusinessType.UPDATE)
    @PostMapping("editPost")
    public Result editPost(@RequestBody ApePost apePost) {
        QueryWrapper<ApePost> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApePost::getPostCode,apePost.getPostCode());
        int count = apePostService.count(queryWrapper);
        if (count > 0) {
            return Result.fail("岗位编码已存在！");
        }
        boolean update = apePostService.updateById(apePost);
        if (update) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除岗位 */
    @Log(name = "删除岗位", type = BusinessType.DELETE)
    @GetMapping("removePost")
    public Result removePost(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            List<String> asList = Arrays.asList(ids.split(","));
            boolean remove = apePostService.removeByIds(asList);
            if (remove) {
                return Result.success();
            } else {
                return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
            }
        } else {
            return Result.fail("岗位id不能为空！");
        }

    }

}
