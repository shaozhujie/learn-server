package com.ape.apeadmin.controller.dept;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apesystem.domain.ApeDept;
import com.ape.apesystem.service.ApeDeptService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 部门controller
 * @date 2023/8/28 11:37
 */
@Controller
@ResponseBody
@RequestMapping("dept")
public class ApeDeptController {

    @Autowired
    private ApeDeptService apeDeptService;

    /** 查询 */
    @Log(name = "查询部门列表", type = BusinessType.OTHER)
    @PostMapping("getDeptList")
    public Result getDeptPage(@RequestBody ApeDept apeDept) {
        //构造查询条件
        QueryWrapper<ApeDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(apeDept.getDeptName()),ApeDept::getDeptName,apeDept.getDeptName())
                .eq(apeDept.getStatus() != null,ApeDept::getStatus,apeDept.getStatus()).orderByAsc(ApeDept::getOrderNum);
        //查询
        List<ApeDept> apeDepts = apeDeptService.list(queryWrapper);
        //筛选出第一级
        List<ApeDept> first = apeDepts.stream().filter(item -> "0".equals(item.getParentId())).collect(Collectors.toList());
        if (first.size() <= 0) {
            return Result.success(apeDepts);
        } else {
            for (ApeDept dept : first) {
                filterDept(dept,apeDepts);
            }
            return Result.success(first);
        }
    }

    /** 递归查询下级部门 */
    public void filterDept(ApeDept apeDept,List<ApeDept> apeDepts) {
        List<ApeDept> depts = new ArrayList<>();
        for (ApeDept dept : apeDepts) {
            if (apeDept.getId().equals(dept.getParentId())) {
                depts.add(dept);
                filterDept(dept,apeDepts);
            }
        }
        apeDept.setChildren(depts);
    }

    /** 根据id查询部门 */
    @Log(name = "根据id查询部门", type = BusinessType.OTHER)
    @GetMapping("getById")
    public Result getById(@RequestParam("id") String id) {
        ApeDept apeDept = apeDeptService.getById(id);
        return Result.success(apeDept);
    }

    /** 保存 */
    @Log(name = "保存部门", type = BusinessType.INSERT)
    @PostMapping("saveDept")
    public Result saveDept(@RequestBody ApeDept apeDept) {
        boolean save = apeDeptService.save(apeDept);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑 */
    @Log(name = "编辑部门", type = BusinessType.UPDATE)
    @PostMapping("editDept")
    public Result editDept(@RequestBody ApeDept apeDept){
        boolean edit = apeDeptService.updateById(apeDept);
        if (edit) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除 */
    @Log(name = "删除部门", type = BusinessType.DELETE)
    @GetMapping("removeDept")
    public Result removeDept(@RequestParam("id")String id) {
        QueryWrapper<ApeDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeDept::getParentId,id);
        int count = apeDeptService.count(queryWrapper);
        if (count > 0) {
            return Result.fail("存在下级部门,请先删除下级部门！");
        }
        boolean remove = apeDeptService.removeById(id);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

}
