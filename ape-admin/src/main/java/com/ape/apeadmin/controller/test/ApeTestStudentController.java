package com.ape.apeadmin.controller.test;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.ApeTest;
import com.ape.apesystem.domain.ApeTestStudent;
import com.ape.apesystem.domain.ApeUser;
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
import java.util.Date;
import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 用户考试题目controller
 * @date 2023/11/24 10:23
 */
@Controller
@ResponseBody
@RequestMapping("student")
public class ApeTestStudentController {

    @Autowired
    private ApeTestStudentService apeTestStudentService;
    @Autowired
    private ApeTestService apeTestService;

    /** 分页获取用户考试题目 */
    @Log(name = "分页获取用户考试题目", type = BusinessType.OTHER)
    @PostMapping("getApeTestStudentPage")
    public Result getApeTestStudentPage(@RequestBody ApeTestStudent apeTestStudent) {
        Page<ApeTestStudent> page = new Page<>(apeTestStudent.getPageNumber(),apeTestStudent.getPageSize());
        QueryWrapper<ApeTestStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(apeTestStudent.getItemId()),ApeTestStudent::getItemId,apeTestStudent.getItemId())
                .eq(StringUtils.isNotBlank(apeTestStudent.getTestId()),ApeTestStudent::getTestId,apeTestStudent.getTestId())
                .like(StringUtils.isNotBlank(apeTestStudent.getTitle()),ApeTestStudent::getTitle,apeTestStudent.getTitle())
                .eq(apeTestStudent.getSort() != null,ApeTestStudent::getSort,apeTestStudent.getSort())
                .eq(apeTestStudent.getType() != null,ApeTestStudent::getType,apeTestStudent.getType())
                .eq(StringUtils.isNotBlank(apeTestStudent.getUserId()),ApeTestStudent::getUserId,apeTestStudent.getUserId());
        Page<ApeTestStudent> apeTestStudentPage = apeTestStudentService.page(page, queryWrapper);
        return Result.success(apeTestStudentPage);
    }

    /** 根据id获取用户考试题目 */
    @Log(name = "根据id获取用户考试题目", type = BusinessType.OTHER)
    @GetMapping("getApeTestStudentById")
    public Result getApeTestStudentById(@RequestParam("id")String id) {
        ApeTestStudent apeTestStudent = apeTestStudentService.getById(id);
        return Result.success(apeTestStudent);
    }

    @GetMapping("getTestUserState")
    public Result getTestUserState(@RequestParam("id")String id) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<ApeTestStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeTestStudent::getTestId,id)
                .eq(ApeTestStudent::getUserId,userInfo.getId());
        int count = apeTestStudentService.count(queryWrapper);
        if (count > 0) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    /** 保存用户考试题目 */
    @Log(name = "保存用户考试题目", type = BusinessType.INSERT)
    @PostMapping("saveApeTestStudent")
    public Result saveApeTestStudent(@RequestBody JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        List<ApeTestStudent> apeTestStudents = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            ApeTestStudent object = jsonArray.getJSONObject(i).toJavaObject(ApeTestStudent.class);
            if (StringUtils.isNotBlank(object.getSolution())) {
                if (object.getType() == 0 || object.getType() == 1 || object.getType() == 2 || object.getType() == 3 ) {
                    if (object.getAnswer().equals(object.getSolution())) {
                        object.setPoint(object.getScore());
                    }
                }
                if (object.getType() == 4) {
                    JSONArray parseArray = JSONArray.parseArray(object.getKeyword());
                    int score = 0;
                    for (int j = 0; j < parseArray.size();j++) {
                        JSONObject item = parseArray.getJSONObject(j);
                        if (object.getSolution().contains(item.getString("option"))) {
                            score += item.getInteger("value");
                        }
                    }
                    object.setPoint(score);
                }
            }
            apeTestStudents.add(object);
        }
        if (apeTestStudents.size() > 0) {
            ApeTestStudent student = apeTestStudents.get(0);
            ApeTest test = apeTestService.getById(student.getTestId());
            Date date = new Date();
            if (test.getEndTime().before(date)) {
                return Result.fail("考试截止时间已过无法提交");
            }
        }
        boolean save = apeTestStudentService.saveBatch(apeTestStudents);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑用户考试题目 */
    @Log(name = "编辑用户考试题目", type = BusinessType.UPDATE)
    @PostMapping("editApeTestStudent")
    public Result editApeTestStudent(@RequestBody JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        List<ApeTestStudent> apeTestStudents = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            ApeTestStudent object = jsonArray.getJSONObject(i).toJavaObject(ApeTestStudent.class);
            apeTestStudents.add(object);
        }
        boolean save = apeTestStudentService.updateBatchById(apeTestStudents);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    @GetMapping("getTestStudentItem")
    public Result getTestStudent(@RequestParam("testId")String testId,@RequestParam("userId")String userId) {
        QueryWrapper<ApeTestStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeTestStudent::getTestId,testId)
                .eq(ApeTestStudent::getUserId,userId);
        List<ApeTestStudent> studentList = apeTestStudentService.list(queryWrapper);
        return Result.success(studentList);
    }

    /** 删除用户考试题目 */
    @GetMapping("removeApeTestStudent")
    @Log(name = "删除用户考试题目", type = BusinessType.DELETE)
    public Result removeApeTestStudent(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeTestStudentService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("用户考试题目id不能为空！");
        }
    }

}