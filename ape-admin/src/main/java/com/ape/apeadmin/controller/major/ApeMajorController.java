package com.ape.apeadmin.controller.major;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apesystem.domain.ApeMajor;
import com.ape.apesystem.domain.ApeSchool;
import com.ape.apesystem.service.ApeMajorService;
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
 * @description: 专业表controller
 * @date 2023/11/16 08:31
 */
@Controller
@ResponseBody
@RequestMapping("major")
public class ApeMajorController {

    @Autowired
    private ApeMajorService apeMajorService;

    /** 分页获取专业表 */
    @Log(name = "分页获取专业表", type = BusinessType.OTHER)
    @PostMapping("getApeMajorPage")
    public Result getApeMajorPage(@RequestBody ApeMajor apeMajor) {
        Page<ApeMajor> page = new Page<>(apeMajor.getPageNumber(),apeMajor.getPageSize());
        QueryWrapper<ApeMajor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(apeMajor.getName()),ApeMajor::getName,apeMajor.getName());
        Page<ApeMajor> apeMajorPage = apeMajorService.page(page, queryWrapper);
        return Result.success(apeMajorPage);
    }

    /** 获取专业列表 */
    @Log(name = "获取专业列表", type = BusinessType.OTHER)
    @GetMapping("getApeMajorList")
    public Result getApeMajorList() {
        List<ApeMajor> majorList = apeMajorService.list();
        return Result.success(majorList);
    }

    /** 根据id获取专业表 */
    @Log(name = "根据id获取专业表", type = BusinessType.OTHER)
    @GetMapping("getApeMajorById")
    public Result getApeMajorById(@RequestParam("id")String id) {
        ApeMajor apeMajor = apeMajorService.getById(id);
        return Result.success(apeMajor);
    }

    /** 保存专业表 */
    @Log(name = "保存专业表", type = BusinessType.INSERT)
    @PostMapping("saveApeMajor")
    public Result saveApeMajor(@RequestBody ApeMajor apeMajor) {
        boolean save = apeMajorService.save(apeMajor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑专业表 */
    @Log(name = "编辑专业表", type = BusinessType.UPDATE)
    @PostMapping("editApeMajor")
    public Result editApeMajor(@RequestBody ApeMajor apeMajor) {
        boolean save = apeMajorService.updateById(apeMajor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除专业表 */
    @GetMapping("removeApeMajor")
    @Log(name = "删除专业表", type = BusinessType.DELETE)
    public Result removeApeMajor(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeMajorService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("专业表id不能为空！");
        }
    }

}