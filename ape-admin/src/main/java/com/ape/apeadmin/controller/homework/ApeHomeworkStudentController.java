package com.ape.apeadmin.controller.homework;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.ApeChapter;
import com.ape.apesystem.domain.ApeHomework;
import com.ape.apesystem.domain.ApeHomeworkStudent;
import com.ape.apesystem.domain.ApeUser;
import com.ape.apesystem.service.ApeChapterService;
import com.ape.apesystem.service.ApeHomeworkService;
import com.ape.apesystem.service.ApeHomeworkStudentService;
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
import java.util.stream.Collectors;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 学生作业controller
 * @date 2023/11/22 04:28
 */
@Controller
@ResponseBody
@RequestMapping("student")
public class ApeHomeworkStudentController {

    @Autowired
    private ApeHomeworkStudentService apeHomeworkStudentService;
    @Autowired
    private ApeHomeworkService apeHomeworkService;
    @Autowired
    private ApeChapterService apeChapterService;

    /** 分页获取学生作业 */
    @Log(name = "分页获取学生作业", type = BusinessType.OTHER)
    @PostMapping("getApeHomeworkStudentPage")
    public Result getApeHomeworkStudentPage(@RequestBody ApeHomeworkStudent apeHomeworkStudent) {
        Page<ApeHomeworkStudent> page = new Page<>(apeHomeworkStudent.getPageNumber(),apeHomeworkStudent.getPageSize());
        QueryWrapper<ApeHomeworkStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(apeHomeworkStudent.getChapterId()),ApeHomeworkStudent::getChapterId,apeHomeworkStudent.getChapterId())
                .eq(StringUtils.isNotBlank(apeHomeworkStudent.getWorkId()),ApeHomeworkStudent::getWorkId,apeHomeworkStudent.getWorkId())
                .eq(StringUtils.isNotBlank(apeHomeworkStudent.getTitle()),ApeHomeworkStudent::getTitle,apeHomeworkStudent.getTitle())
                .eq(StringUtils.isNotBlank(apeHomeworkStudent.getUserId()),ApeHomeworkStudent::getUserId,apeHomeworkStudent.getUserId());
        Page<ApeHomeworkStudent> apeHomeworkStudentPage = apeHomeworkStudentService.page(page, queryWrapper);
        return Result.success(apeHomeworkStudentPage);
    }

    @GetMapping("getApeHomeworkStudentFlag")
    public Result getApeHomeworkStudentFlag(@RequestParam("id") String id) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<ApeHomeworkStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeHomeworkStudent::getChapterId,id)
                .eq(ApeHomeworkStudent::getUserId,userInfo.getId());
        int count = apeHomeworkStudentService.count(queryWrapper);
        if (count > 0) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @GetMapping("getHomeworkStudentFlag")
    public Result getHomeworkStudentFlag(@RequestParam("id") String id,@RequestParam("userId")String userId) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<ApeHomeworkStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeHomeworkStudent::getChapterId,id)
                .eq(ApeHomeworkStudent::getUserId,userId);
        int count = apeHomeworkStudentService.count(queryWrapper);
        if (count > 0) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @GetMapping("getMyApeHomework")
    public Result getMyApeHomework() {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<ApeHomeworkStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeHomeworkStudent::getUserId,userInfo.getId())
                .groupBy(ApeHomeworkStudent::getChapterId);
        List<ApeHomeworkStudent> studentList = apeHomeworkStudentService.list(queryWrapper);
        for (ApeHomeworkStudent student : studentList) {
            ApeChapter chapter = apeChapterService.getById(student.getChapterId());
            student.setTaskName(chapter.getTaskName());
        }
        return Result.success(studentList);
    }

    @PostMapping("getApeHomeworkStudentList")
    public Result getApeHomeworkStudentList(@RequestBody ApeHomeworkStudent apeHomeworkStudent) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<ApeHomework> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeHomework::getChapterId,apeHomeworkStudent.getChapterId()).orderByAsc(ApeHomework::getSort);
        List<ApeHomework> homeworkList = apeHomeworkService.list(queryWrapper);
        List<ApeHomeworkStudent> homeworkStudentList = new ArrayList<>();
        for (ApeHomework homework : homeworkList) {
            QueryWrapper<ApeHomeworkStudent> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ApeHomeworkStudent::getUserId,userInfo.getId())
                            .eq(ApeHomeworkStudent::getWorkId,homework.getId());
            ApeHomeworkStudent homeworkStudent = apeHomeworkStudentService.getOne(wrapper);
            if (homeworkStudent == null) {
                homeworkStudent = new ApeHomeworkStudent();
                homeworkStudent.setChapterId(homework.getChapterId());
                homeworkStudent.setChapterName(homework.getChapterName());
                homeworkStudent.setWorkId(homework.getId());
                homeworkStudent.setTitle(homework.getTitle());
                homeworkStudent.setSort(homework.getSort());
                homeworkStudent.setAnswer(homework.getAnswer());
                homeworkStudent.setType(homework.getType());
                homeworkStudent.setScore(homework.getScore());
                homeworkStudent.setContent(homework.getContent());
                homeworkStudent.setUserId(userInfo.getId());
                homeworkStudent.setSolution("");
                homeworkStudent.setPoint(0);
            }
            homeworkStudentList.add(homeworkStudent);
        }
        return Result.success(homeworkStudentList);
    }



    /** 根据id获取学生作业 */
    @Log(name = "根据id获取学生作业", type = BusinessType.OTHER)
    @GetMapping("getApeHomeworkStudentById")
    public Result getApeHomeworkStudentById(@RequestParam("id")String id) {
        ApeHomeworkStudent apeHomeworkStudent = apeHomeworkStudentService.getById(id);
        return Result.success(apeHomeworkStudent);
    }

    /** 保存学生作业 */
    @Log(name = "保存学生作业", type = BusinessType.INSERT)
    @PostMapping("saveApeHomeworkStudent")
    public Result saveApeHomeworkStudent(@RequestBody JSONObject jsonObject) {
        JSONArray homework = jsonObject.getJSONArray("homework");
        List<ApeHomeworkStudent> list = new ArrayList<>();
        for (int i = 0; i < homework.size(); i++) {
            ApeHomeworkStudent homeworkStudent = homework.getJSONObject(i).toJavaObject(ApeHomeworkStudent.class);
            if (StringUtils.isNotBlank(homeworkStudent.getSolution())) {
                if (homeworkStudent.getAnswer().equals(homeworkStudent.getSolution())) {
                    homeworkStudent.setPoint(homeworkStudent.getScore());
                }
            }
            list.add(homeworkStudent);
        }
        boolean save = apeHomeworkStudentService.saveBatch(list);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑学生作业 */
    @Log(name = "编辑学生作业", type = BusinessType.UPDATE)
    @PostMapping("editApeHomeworkStudent")
    public Result editApeHomeworkStudent(@RequestBody ApeHomeworkStudent apeHomeworkStudent) {
        boolean save = apeHomeworkStudentService.updateById(apeHomeworkStudent);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除学生作业 */
    @GetMapping("removeApeHomeworkStudent")
    @Log(name = "删除学生作业", type = BusinessType.DELETE)
    public Result removeApeHomeworkStudent(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeHomeworkStudentService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("学生作业id不能为空！");
        }
    }

    @GetMapping("getWrongWork")
    public Result getWrongWork(@RequestParam("id") String id) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<ApeHomeworkStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeHomeworkStudent::getChapterId,id)
                .eq(ApeHomeworkStudent::getUserId,userInfo.getId());
        List<ApeHomeworkStudent> studentList = apeHomeworkStudentService.list(queryWrapper);
        studentList = studentList.stream().filter(item -> {
            return !item.getAnswer().equals(item.getSolution());
        }).collect(Collectors.toList());
        return Result.success(studentList);
    }

}