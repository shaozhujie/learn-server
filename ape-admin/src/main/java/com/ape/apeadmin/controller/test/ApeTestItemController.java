package com.ape.apeadmin.controller.test;

import com.alibaba.fastjson2.JSONObject;
import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.*;
import com.ape.apesystem.service.ApeTestItemService;
import com.ape.apesystem.service.ApeTestService;
import com.ape.apesystem.service.ApeTestStudentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 考试题目controller
 * @date 2023/11/20 02:51
 */
@Controller
@ResponseBody
@RequestMapping("item")
public class ApeTestItemController {

    @Autowired
    private ApeTestItemService apeTestItemService;
    @Autowired
    private ApeTestService apeTestService;
    @Autowired
    private ApeTestStudentService apeTestStudentService;

    /** 分页获取考试题目 */
    @Log(name = "分页获取考试题目", type = BusinessType.OTHER)
    @PostMapping("getApeTestItemPage")
    public Result getApeTestItemPage(@RequestBody ApeTestItem apeTestItem) {
        Page<ApeTestItem> page = new Page<>(apeTestItem.getPageNumber(),apeTestItem.getPageSize());
        QueryWrapper<ApeTestItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(apeTestItem.getTitle()),ApeTestItem::getTitle,apeTestItem.getTitle())
                .eq(StringUtils.isNotBlank(apeTestItem.getTestId()),ApeTestItem::getTestId,apeTestItem.getTestId());
        Page<ApeTestItem> apeTestItemPage = apeTestItemService.page(page, queryWrapper);
        return Result.success(apeTestItemPage);
    }

    /** 根据id获取考试题目 */
    @Log(name = "根据id获取考试题目", type = BusinessType.OTHER)
    @GetMapping("getApeTestItemById")
    public Result getApeTestItemById(@RequestParam("id")String id) {
        ApeTestItem apeTestItem = apeTestItemService.getById(id);
        return Result.success(apeTestItem);
    }

    @GetMapping("getApeTestItemByTestId")
    public Result getApeTestItemByTestId(@RequestParam("id")String id) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        ApeTest test = apeTestService.getById(id);
        QueryWrapper<ApeTestItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeTestItem::getTestId,id).orderByAsc(ApeTestItem::getSort);
        List<ApeTestItem> apeTestItem = apeTestItemService.list(queryWrapper);
        List<ApeTestStudent> apeTestStudents = new ArrayList<>();
        for (ApeTestItem item : apeTestItem) {
            QueryWrapper<ApeTestStudent> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ApeTestStudent::getUserId,userInfo.getId())
                    .eq(ApeTestStudent::getItemId,item.getId());
            ApeTestStudent apeTestStudent = apeTestStudentService.getOne(wrapper);
            if (apeTestStudent == null) {
                apeTestStudent = new ApeTestStudent();
                apeTestStudent.setItemId(item.getId());
                apeTestStudent.setTestId(item.getTestId());
                apeTestStudent.setTitle(item.getTitle());
                apeTestStudent.setContent(item.getContent());
                apeTestStudent.setSort(item.getSort());
                apeTestStudent.setType(item.getType());
                apeTestStudent.setScore(item.getScore());
                apeTestStudent.setKeyword(item.getKeyword());
                apeTestStudent.setAnswer(item.getAnswer());
                apeTestStudent.setUserId(userInfo.getId());
            }
            apeTestStudents.add(apeTestStudent);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("testItem",apeTestStudents);
        jsonObject.put("test",test);
        return Result.success(jsonObject);
    }

    /** 保存考试题目 */
    @Log(name = "保存考试题目", type = BusinessType.INSERT)
    @PostMapping("saveApeTestItem")
    public Result saveApeTestItem(@RequestBody ApeTestItem apeTestItem) {
        boolean save = apeTestItemService.save(apeTestItem);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑考试题目 */
    @Log(name = "编辑考试题目", type = BusinessType.UPDATE)
    @PostMapping("editApeTestItem")
    public Result editApeTestItem(@RequestBody ApeTestItem apeTestItem) {
        boolean save = apeTestItemService.updateById(apeTestItem);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除考试题目 */
    @GetMapping("removeApeTestItem")
    @Log(name = "删除考试题目", type = BusinessType.DELETE)
    public Result removeApeTestItem(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeTestItemService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("考试题目id不能为空！");
        }
    }

}