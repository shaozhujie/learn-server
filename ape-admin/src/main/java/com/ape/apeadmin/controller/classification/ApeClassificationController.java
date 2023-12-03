package com.ape.apeadmin.controller.classification;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apesystem.domain.ApeClassification;
import com.ape.apesystem.service.ApeClassificationService;
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
 * @description: 分类controller
 * @date 2023/11/17 02:15
 */
@Controller
@ResponseBody
@RequestMapping("classification")
public class ApeClassificationController {

    @Autowired
    private ApeClassificationService apeClassificationService;

    /** 分页获取分类 */
    @Log(name = "分页获取分类", type = BusinessType.OTHER)
    @PostMapping("getApeClassificationPage")
    public Result getApeClassificationPage(@RequestBody ApeClassification apeClassification) {
        Page<ApeClassification> page = new Page<>(apeClassification.getPageNumber(),apeClassification.getPageSize());
        QueryWrapper<ApeClassification> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(apeClassification.getName()),ApeClassification::getName,apeClassification.getName());
        Page<ApeClassification> apeClassificationPage = apeClassificationService.page(page, queryWrapper);
        return Result.success(apeClassificationPage);
    }

    /** 获取分类列表 */
    @Log(name = "获取分类列表", type = BusinessType.OTHER)
    @GetMapping("getApeClassificationList")
    public Result getApeClassificationList() {
        List<ApeClassification> list = apeClassificationService.list();
        return Result.success(list);
    }

    /** 根据id获取分类 */
    @Log(name = "根据id获取分类", type = BusinessType.OTHER)
    @GetMapping("getApeClassificationById")
    public Result getApeClassificationById(@RequestParam("id")String id) {
        ApeClassification apeClassification = apeClassificationService.getById(id);
        return Result.success(apeClassification);
    }

    /** 保存分类 */
    @Log(name = "保存分类", type = BusinessType.INSERT)
    @PostMapping("saveApeClassification")
    public Result saveApeClassification(@RequestBody ApeClassification apeClassification) {
        boolean save = apeClassificationService.save(apeClassification);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑分类 */
    @Log(name = "编辑分类", type = BusinessType.UPDATE)
    @PostMapping("editApeClassification")
    public Result editApeClassification(@RequestBody ApeClassification apeClassification) {
        boolean save = apeClassificationService.updateById(apeClassification);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除分类 */
    @GetMapping("removeApeClassification")
    @Log(name = "删除分类", type = BusinessType.DELETE)
    public Result removeApeClassification(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeClassificationService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("分类id不能为空！");
        }
    }

}