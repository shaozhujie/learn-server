package com.ape.apeadmin.controller.index;

import com.alibaba.fastjson2.JSONObject;
import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.ApeTask;
import com.ape.apesystem.domain.ApeTaskStudent;
import com.ape.apesystem.domain.ApeUser;
import com.ape.apesystem.service.ApeAccountService;
import com.ape.apesystem.service.ApeTaskService;
import com.ape.apesystem.service.ApeTaskStudentService;
import com.ape.apesystem.service.ApeUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 9:09
 */
@Controller
@ResponseBody
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private ApeUserService apeUserService;
    @Autowired
    private ApeTaskService apeTaskService;
    @Autowired
    private ApeTaskStudentService apeTaskStudentService;
    @Autowired
    private ApeAccountService apeAccountService;

    /** 获取首页统计数据 */
    @GetMapping("getIndexAchievement")
    public Result getIndexAchievement() {
        int count = apeTaskService.count();
        QueryWrapper<ApeUser> query = new QueryWrapper<>();
        query.lambda().eq(ApeUser::getUserType,1);
        int count1 = apeUserService.count(query);
        QueryWrapper<ApeUser> query2 = new QueryWrapper<>();
        query.lambda().eq(ApeUser::getUserType,2);
        int count2 = apeUserService.count(query2);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count",count);
        jsonObject.put("count1",count1);
        jsonObject.put("count2",count2);
        jsonObject.put("total",count1 + count2);
        return Result.success(jsonObject);
    }

    //后台首页

    @GetMapping("getIndexData")
    public Result getIndexData(@RequestParam("type") Integer type) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        JSONObject jsonObject = new JSONObject();
        //用户数量
        QueryWrapper<ApeUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeUser::getUserType,1).or()
                .eq(ApeUser::getUserType,2);
        int count = apeUserService.count(queryWrapper);
        jsonObject.put("userNum",count);
        //教师数量
        QueryWrapper<ApeUser> query = new QueryWrapper<>();
        query.lambda().eq(ApeUser::getUserType,1);
        int count1 = apeUserService.count(query);
        jsonObject.put("teacherNum",count1);
        if (type == 0) {
            //学生数量
            QueryWrapper<ApeUser> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ApeUser::getUserType,2);
            int num = apeUserService.count(wrapper);
            jsonObject.put("studentNum",num);
        } else {
            //学生数量
            QueryWrapper<ApeTaskStudent> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ApeTaskStudent::getTeacherId,userInfo.getId())
                    .eq(ApeTaskStudent::getState,0).groupBy(ApeTaskStudent::getUserId);
            List<ApeTaskStudent> taskStudentList = apeTaskStudentService.list(wrapper);
            jsonObject.put("studentNum",taskStudentList.size());
        }
        return Result.success(jsonObject);
    }

    @GetMapping("getIndexSexData")
    public Result getIndexSexData(@RequestParam("type") Integer type) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        List<JSONObject> list = new ArrayList<>();
        int nan = 0;
        int nv = 0;
        if (type == 0) {
            QueryWrapper<ApeUser> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ApeUser::getUserType,2)
                    .eq(ApeUser::getSex,0);
            nan = apeUserService.count(wrapper);
            QueryWrapper<ApeUser> wrapper1 = new QueryWrapper<>();
            wrapper1.lambda().eq(ApeUser::getUserType,2)
                    .eq(ApeUser::getSex,1);
            nv = apeUserService.count(wrapper1);
        } else {
            QueryWrapper<ApeTaskStudent> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ApeTaskStudent::getTeacherId,userInfo.getId())
                    .eq(ApeTaskStudent::getState,0).groupBy(ApeTaskStudent::getUserId);
            List<ApeTaskStudent> taskStudentList = apeTaskStudentService.list(wrapper);
            for (ApeTaskStudent apeTaskStudent : taskStudentList) {
                ApeUser user = apeUserService.getById(apeTaskStudent.getUserId());
                if (user.getSex() == 0) {
                    nan += 1;
                } else {
                    nv += 1;
                }
            }

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value",nan);
        jsonObject.put("name","男");
        list.add(jsonObject);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("value",nv);
        jsonObject1.put("name","女");
        list.add(jsonObject1);
        return Result.success(list);
    }

    @GetMapping("getTaskChart")
    public Result getTaskChart(@RequestParam("type")Integer type) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        List<String> tasks = new ArrayList<>();
        List<Integer> nums =  new ArrayList<>();
        if (type == 0) {
            QueryWrapper<ApeTask> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().orderByAsc(ApeTask::getCreateTime).last("limit 10");
            List<ApeTask> taskList = apeTaskService.list(queryWrapper);
            for (ApeTask task : taskList) {
                tasks.add(task.getName());
                QueryWrapper<ApeTaskStudent> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(ApeTaskStudent::getTaskId,task.getId())
                        .eq(ApeTaskStudent::getState,0).groupBy(ApeTaskStudent::getUserId);
                List<ApeTaskStudent> list = apeTaskStudentService.list(wrapper);
                nums.add(list.size());
            }
        } else {
            QueryWrapper<ApeTask> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().orderByAsc(ApeTask::getCreateTime).eq(ApeTask::getTeacherId,userInfo.getId()).last("limit 10");
            List<ApeTask> taskList = apeTaskService.list(queryWrapper);
            for (ApeTask task : taskList) {
                tasks.add(task.getName());
                QueryWrapper<ApeTaskStudent> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(ApeTaskStudent::getTaskId,task.getId())
                        .eq(ApeTaskStudent::getState,0).groupBy(ApeTaskStudent::getUserId);
                List<ApeTaskStudent> list = apeTaskStudentService.list(wrapper);
                nums.add(list.size());
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tasks",tasks);
        jsonObject.put("nums",nums);
        return Result.success(jsonObject);
    }

    @GetMapping("getTaskIndexList")
    public Result getTaskIndexList(@RequestParam("type")Integer type) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        List<ApeTask> taskList = new ArrayList<>();
        if (type == 0) {
            QueryWrapper<ApeTask> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().orderByDesc(ApeTask::getCreateTime).last("limit 5");
            taskList = apeTaskService.list(queryWrapper);
        } else {
            QueryWrapper<ApeTask> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().orderByDesc(ApeTask::getCreateTime).eq(ApeTask::getTeacherId,userInfo.getId()).last("limit 5");
            taskList = apeTaskService.list(queryWrapper);
        }
        return Result.success(taskList);
    }

}
