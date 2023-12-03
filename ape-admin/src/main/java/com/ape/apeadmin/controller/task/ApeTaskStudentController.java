package com.ape.apeadmin.controller.task;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.*;
import com.ape.apesystem.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 课程报名controller
 * @date 2023/11/21 03:15
 */
@Controller
@ResponseBody
@RequestMapping("student")
public class ApeTaskStudentController {

    @Autowired
    private ApeTaskStudentService apeTaskStudentService;
    @Autowired
    private ApeTaskService apeTaskService;
    @Autowired
    private ApeHomeworkStudentService apeHomeworkStudentService;
    @Autowired
    private ApeChapterService apeChapterService;
    @Autowired
    private ApeChapterVideoService apeChapterVideoService;
    @Autowired
    private ApeTestStudentService apeTestStudentService;
    @Autowired
    private ApeTestService apeTestService;
    @Autowired
    private ApeHomeworkService apeHomeworkService;

    /** 分页获取课程报名 */
    @Log(name = "分页获取课程报名", type = BusinessType.OTHER)
    @PostMapping("getApeTaskStudentPage")
    public Result getApeTaskStudentPage(@RequestBody ApeTaskStudent apeTaskStudent) {
        Page<ApeTaskStudent> page = new Page<>(apeTaskStudent.getPageNumber(),apeTaskStudent.getPageSize());
        QueryWrapper<ApeTaskStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(apeTaskStudent.getTaskId()),ApeTaskStudent::getTaskId,apeTaskStudent.getTaskId())
                .like(StringUtils.isNotBlank(apeTaskStudent.getUserName()),ApeTaskStudent::getUserName,apeTaskStudent.getUserName())
                .eq(apeTaskStudent.getState() != null,ApeTaskStudent::getState,apeTaskStudent.getState());
        Page<ApeTaskStudent> apeTaskStudentPage = apeTaskStudentService.page(page, queryWrapper);
        return Result.success(apeTaskStudentPage);
    }

    @PostMapping("getTaskStudentPage")
    public Result getTaskStudentPage(@RequestBody ApeTaskStudent apeTaskStudent) {
        ApeChapter chapter = apeChapterService.getById(apeTaskStudent.getChapterId());
        Page<ApeTaskStudent> page = new Page<>(apeTaskStudent.getPageNumber(),apeTaskStudent.getPageSize());
        QueryWrapper<ApeTaskStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(apeTaskStudent.getTaskId()),ApeTaskStudent::getTaskId,apeTaskStudent.getTaskId())
                .like(StringUtils.isNotBlank(apeTaskStudent.getUserName()),ApeTaskStudent::getUserName,apeTaskStudent.getUserName())
                .eq(apeTaskStudent.getState() != null,ApeTaskStudent::getState,apeTaskStudent.getState());
        Page<ApeTaskStudent> apeTaskStudentPage = apeTaskStudentService.page(page, queryWrapper);
        for (ApeTaskStudent taskStudent : apeTaskStudentPage.getRecords()) {
            QueryWrapper<ApeHomeworkStudent> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ApeHomeworkStudent::getChapterId,chapter.getId())
                    .eq(ApeHomeworkStudent::getUserId,taskStudent.getUserId());
            int count = apeHomeworkStudentService.count(wrapper);
            if (count > 0) {
                taskStudent.setHomework("已完成");
            } else {
                taskStudent.setHomework("未完成");
            }
            QueryWrapper<ApeChapterVideo> videoQueryWrapper = new QueryWrapper<>();
            videoQueryWrapper.lambda().eq(ApeChapterVideo::getChapterId,chapter.getId())
                    .eq(ApeChapterVideo::getUserId,taskStudent.getUserId());
            int count1 = apeChapterVideoService.count(videoQueryWrapper);
            if (count1 > 0) {
                taskStudent.setVideo("已观看");
            } else {
                taskStudent.setVideo("未观看");
            }
        }
        return Result.success(apeTaskStudentPage);
    }

    /** 分页获取课程报名 */
    @Log(name = "分页获取我的课程", type = BusinessType.OTHER)
    @PostMapping("getApeMyTaskPage")
    public Result getApeMyTaskPage(@RequestBody ApeTaskStudent apeTaskStudent) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        apeTaskStudent.setUserId(userInfo.getId());
        Page<ApeTaskStudent> page = new Page<>(apeTaskStudent.getPageNumber(),apeTaskStudent.getPageSize());
        QueryWrapper<ApeTaskStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(apeTaskStudent.getUserId()),ApeTaskStudent::getUserId,apeTaskStudent.getUserId())
                .eq(apeTaskStudent.getState() != null,ApeTaskStudent::getState,apeTaskStudent.getState());
        Page<ApeTaskStudent> apeTaskStudentPage = apeTaskStudentService.page(page, queryWrapper);
        for (ApeTaskStudent taskStudent : apeTaskStudentPage.getRecords()) {
            ApeTask task = apeTaskService.getById(taskStudent.getTaskId());
            taskStudent.setTaskDescribe(task.getTaskDescribe());
            taskStudent.setNum(task.getNum());
            taskStudent.setImage(task.getImage());
        }
        return Result.success(apeTaskStudentPage);
    }

    @PostMapping("getApeMyTaskList")
    public Result getApeMyTaskList(@RequestBody ApeTaskStudent apeTaskStudent) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        apeTaskStudent.setUserId(userInfo.getId());
        QueryWrapper<ApeTaskStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(apeTaskStudent.getUserId()),ApeTaskStudent::getUserId,apeTaskStudent.getUserId())
                .eq(apeTaskStudent.getState() != null,ApeTaskStudent::getState,apeTaskStudent.getState());
        List<ApeTaskStudent> apeTaskStudentList = apeTaskStudentService.list(queryWrapper);
        for (ApeTaskStudent taskStudent : apeTaskStudentList) {
            ApeTask task = apeTaskService.getById(taskStudent.getTaskId());
            taskStudent.setTaskDescribe(task.getTaskDescribe());
            taskStudent.setNum(task.getNum());
            taskStudent.setImage(task.getImage());
        }
        return Result.success(apeTaskStudentList);
    }

    /** 根据id获取课程报名 */
    @Log(name = "根据id获取课程报名", type = BusinessType.OTHER)
    @GetMapping("getApeTaskStudentById")
    public Result getApeTaskStudentById(@RequestParam("id")String id) {
        ApeTaskStudent apeTaskStudent = apeTaskStudentService.getById(id);
        return Result.success(apeTaskStudent);
    }

    /** 保存课程报名 */
    @Log(name = "保存课程报名", type = BusinessType.INSERT)
    @PostMapping("saveApeTaskStudent")
    public Result saveApeTaskStudent(@RequestBody ApeTaskStudent apeTaskStudent) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        apeTaskStudent.setUserId(userInfo.getId());
        apeTaskStudent.setUserName(userInfo.getUserName());
        ApeTask task = apeTaskService.getById(apeTaskStudent.getTaskId());
        apeTaskStudent.setTaskName(task.getName());
        apeTaskStudent.setTeacherId(task.getTeacherId());
        apeTaskStudent.setTeacherName(task.getTeacherName());
        boolean save = apeTaskStudentService.save(apeTaskStudent);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    @GetMapping("getTaskStudent")
    public Result getTaskStudent(@RequestParam("id") String id) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<ApeTaskStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeTaskStudent::getTaskId,id)
                .eq(ApeTaskStudent::getUserId,userInfo.getId()).last("limit 1");
        ApeTaskStudent taskStudent = apeTaskStudentService.getOne(queryWrapper);
        if (taskStudent != null) {
            return Result.success(taskStudent.getState());
        } else {
            return Result.success(2);
        }
    }

    /** 编辑课程报名 */
    @Log(name = "编辑课程报名", type = BusinessType.UPDATE)
    @PostMapping("editApeTaskStudent")
    public Result editApeTaskStudent(@RequestBody ApeTaskStudent apeTaskStudent) {
        ApeTaskStudent student = apeTaskStudentService.getById(apeTaskStudent.getId());
        ApeTask task = apeTaskService.getById(student.getTaskId());
        if (apeTaskStudent.getState() == 0) {
            task.setNum(task.getNum() + 1);
        } else {
            task.setNum(task.getNum() - 1);
        }
        apeTaskService.updateById(task);
        boolean save = apeTaskStudentService.updateById(apeTaskStudent);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除课程报名 */
    @GetMapping("removeApeTaskStudent")
    @Log(name = "删除课程报名", type = BusinessType.DELETE)
    public Result removeApeTaskStudent(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeTaskStudentService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("课程报名id不能为空！");
        }
    }

    @PostMapping("getAdoptTaskStudent")
    public Result getAdoptTaskStudent(@RequestBody ApeTaskStudent apeTaskStudent) {
        ApeTask task = apeTaskService.getById(apeTaskStudent.getTaskId());
        QueryWrapper<ApeTaskStudent> query = new QueryWrapper<>();
        query.lambda().eq(ApeTaskStudent::getTaskId,apeTaskStudent.getTaskId())
                .eq(ApeTaskStudent::getState,0);
        Page<ApeTaskStudent> page = new Page<>(apeTaskStudent.getPageNumber(),apeTaskStudent.getPageSize());
        Page<ApeTaskStudent> studentPage = apeTaskStudentService.page(page, query);
        QueryWrapper<ApeChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeChapter::getTaskId,apeTaskStudent.getTaskId());
        int count2 = apeChapterService.count(queryWrapper);
        List<String> count3 = apeHomeworkService.getTotalAssignCount(apeTaskStudent.getTaskId());
        for (ApeTaskStudent student : studentPage.getRecords()) {
            //获取考试得分
            Map<String,Object> score = apeTestService.getStudentTotalScore(student.getTaskId(),student.getUserId());
            if (score != null) {
                student.setTestScore(score.get("point").toString());
                student.setTotalScore(score.get("total").toString());
            } else {
                student.setTestScore("0");
                student.setTotalScore("0");
            }
            //获取已看视频数量
            Integer count = apeChapterService.getStudentVideo(student.getTaskId(),student.getUserId());
            if (count == null) {
                count = 0;
            }
            student.setVideoCount(count);
            //获取已做作业数量
            List<String> count1 = apeHomeworkService.getStudentHomeWork(student.getTaskId(),student.getUserId());
            student.setAssignCount(count1.size());
            student.setVideoNum(count2);
            student.setAssign(count3.size());
            student.setProportion(task.getProportion());
        }
        return Result.success(studentPage);
    }

}