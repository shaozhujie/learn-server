package com.ape.apeadmin.controller.school;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apesystem.domain.ApeSchool;
import com.ape.apesystem.service.ApeSchoolService;
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
 * @description: 学校表controller
 * @date 2023/11/16 08:03
 */
@Controller
@ResponseBody
@RequestMapping("school")
public class ApeSchoolController {

    @Autowired
    private ApeSchoolService apeSchoolService;

    /** 分页获取学校表 */
    @Log(name = "分页获取学校表", type = BusinessType.OTHER)
    @PostMapping("getApeSchoolPage")
    public Result getApeSchoolPage(@RequestBody ApeSchool apeSchool) {
        Page<ApeSchool> page = new Page<>(apeSchool.getPageNumber(),apeSchool.getPageSize());
        QueryWrapper<ApeSchool> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(apeSchool.getName()),ApeSchool::getName,apeSchool.getName());
        Page<ApeSchool> apeSchoolPage = apeSchoolService.page(page, queryWrapper);
        return Result.success(apeSchoolPage);
    }

    /** 获取学校列表 */
    @Log(name = "获取学校列表", type = BusinessType.OTHER)
    @GetMapping("getApeSchoolList")
    public Result getApeSchoolList() {
        List<ApeSchool> schoolList = apeSchoolService.list();
        return Result.success(schoolList);
    }

    /** 根据id获取学校表 */
    @Log(name = "根据id获取学校表", type = BusinessType.OTHER)
    @GetMapping("getApeSchoolById")
    public Result getApeSchoolById(@RequestParam("id")String id) {
        ApeSchool apeSchool = apeSchoolService.getById(id);
        return Result.success(apeSchool);
    }

    /** 保存学校表 */
    @Log(name = "保存学校表", type = BusinessType.INSERT)
    @PostMapping("saveApeSchool")
    public Result saveApeSchool(@RequestBody ApeSchool apeSchool) {
        boolean save = apeSchoolService.save(apeSchool);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑学校表 */
    @Log(name = "编辑学校表", type = BusinessType.UPDATE)
    @PostMapping("editApeSchool")
    public Result editApeSchool(@RequestBody ApeSchool apeSchool) {
        boolean save = apeSchoolService.updateById(apeSchool);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除学校表 */
    @GetMapping("removeApeSchool")
    @Log(name = "删除学校表", type = BusinessType.DELETE)
    public Result removeApeSchool(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeSchoolService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("学校表id不能为空！");
        }
    }

}