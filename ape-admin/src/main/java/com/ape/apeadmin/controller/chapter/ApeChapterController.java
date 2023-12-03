package com.ape.apeadmin.controller.chapter;

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

import java.util.ArrayList;
import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 章节controller
 * @date 2023/11/17 07:14
 */
@Controller
@ResponseBody
@RequestMapping("chapter")
public class ApeChapterController {

    @Autowired
    private ApeChapterService apeChapterService;
    @Autowired
    private ApeTaskService apeTaskService;
    @Autowired
    private ApeHomeworkService apeHomeworkService;
    @Autowired
    private ApeChapterVideoService apeChapterVideoService;
    @Autowired
    private ApeHomeworkStudentService apeHomeworkStudentService;

    /** 分页获取章节 */
    @Log(name = "分页获取章节", type = BusinessType.OTHER)
    @PostMapping("getApeChapterPage")
    public Result getApeChapterPage(@RequestBody ApeChapter apeChapter) {
        Page<ApeChapter> page = new Page<>(apeChapter.getPageNumber(),apeChapter.getPageSize());
        QueryWrapper<ApeChapter> queryWrapper = new QueryWrapper<>();
        if (apeChapter.getType() == 1) {
            QueryWrapper<ApeTask> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ApeTask::getTeacherId,ShiroUtils.getUserInfo().getId());
            List<ApeTask> taskList = apeTaskService.list(wrapper);
            List<String> list = new ArrayList<String>();
            for (ApeTask apeTask : taskList) {
                list.add(apeTask.getId());
            }
            queryWrapper.lambda()
                    .like(StringUtils.isNotBlank(apeChapter.getTaskName()),ApeChapter::getTaskName,apeChapter.getTaskName())
                    .like(StringUtils.isNotBlank(apeChapter.getName()),ApeChapter::getName,apeChapter.getName());
            if (list.size()>0) {
                queryWrapper.lambda().in(ApeChapter::getTaskId,list);
            } else {
                list.add(" ");
                queryWrapper.lambda().in(ApeChapter::getTaskId,list);
            }
        } else {
            queryWrapper.lambda()
                    .like(StringUtils.isNotBlank(apeChapter.getTaskName()),ApeChapter::getTaskName,apeChapter.getTaskName())
                    .like(StringUtils.isNotBlank(apeChapter.getName()),ApeChapter::getName,apeChapter.getName());
        }
        Page<ApeChapter> apeChapterPage = apeChapterService.page(page, queryWrapper);
        return Result.success(apeChapterPage);
    }

    @GetMapping("getApeChapterByTaskId")
    public Result getApeChapterByTaskId(@RequestParam("id")String id) {
        QueryWrapper<ApeChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeChapter::getTaskId,id);
        List<ApeChapter> chapterList = apeChapterService.list(queryWrapper);
        for (ApeChapter apeChapter : chapterList) {
            QueryWrapper<ApeHomework> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ApeHomework::getChapterId,apeChapter.getId());
            int count = apeHomeworkService.count(wrapper);
            if (count > 0) {
                apeChapter.setHomework(1);
            } else {
                apeChapter.setHomework(0);
            }
        }
        return Result.success(chapterList);
    }

    /** 根据id获取章节 */
    @Log(name = "根据id获取章节", type = BusinessType.OTHER)
    @GetMapping("getApeChapterById")
    public Result getApeChapterById(@RequestParam("id")String id) {
        ApeChapter apeChapter = apeChapterService.getById(id);
        return Result.success(apeChapter);
    }

    /** 保存章节 */
    @Log(name = "保存章节", type = BusinessType.INSERT)
    @PostMapping("saveApeChapter")
    public Result saveApeChapter(@RequestBody ApeChapter apeChapter) {
        if (StringUtils.isNotBlank(apeChapter.getTaskId())) {
            ApeTask task = apeTaskService.getById(apeChapter.getTaskId());
            apeChapter.setTaskName(task.getName());
        }
        boolean save = apeChapterService.save(apeChapter);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑章节 */
    @Log(name = "编辑章节", type = BusinessType.UPDATE)
    @PostMapping("editApeChapter")
    public Result editApeChapter(@RequestBody ApeChapter apeChapter) {
        if (StringUtils.isNotBlank(apeChapter.getTaskId())) {
            ApeTask task = apeTaskService.getById(apeChapter.getTaskId());
            apeChapter.setTaskName(task.getName());
        }
        boolean save = apeChapterService.updateById(apeChapter);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除章节 */
    @GetMapping("removeApeChapter")
    @Log(name = "删除章节", type = BusinessType.DELETE)
    public Result removeApeChapter(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeChapterService.removeById(id);
                QueryWrapper<ApeChapterVideo> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(ApeChapterVideo::getChapterId,id);
                apeChapterVideoService.remove(queryWrapper);
                QueryWrapper<ApeHomework> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.lambda().eq(ApeHomework::getChapterId,id);
                apeHomeworkService.remove(queryWrapper1);
                QueryWrapper<ApeHomeworkStudent> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().eq(ApeHomeworkStudent::getChapterId,id);
                apeHomeworkStudentService.remove(queryWrapper2);
            }
            return Result.success();
        } else {
            return Result.fail("章节id不能为空！");
        }
    }

    @GetMapping("getTaskChapterStudy")
    public Result getTaskChapterStudy(@RequestParam("id")String id) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<ApeChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeChapter::getTaskId,id);
        List<ApeChapter> chapterList = apeChapterService.list(queryWrapper);
        for (ApeChapter apeChapter : chapterList) {
            QueryWrapper<ApeChapterVideo> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ApeChapterVideo::getChapterId,apeChapter.getId())
                    .eq(ApeChapterVideo::getUserId,userInfo.getId());
            int count = apeChapterVideoService.count(wrapper);
            if (count > 0) {
                apeChapter.setVideoFlag("已完成");
            } else {
                apeChapter.setVideoFlag("未完成");
            }
            QueryWrapper<ApeHomework> wrapper2 = new QueryWrapper<>();
            wrapper2.lambda().eq(ApeHomework::getChapterId,apeChapter.getId());
            int count3 = apeHomeworkService.count(wrapper2);
            if (count3 <= 0) {
                apeChapter.setHome("没作业");
            } else {
                apeChapter.setHomework(1);
                QueryWrapper<ApeHomeworkStudent> wrapper1 = new QueryWrapper<>();
                wrapper1.lambda().eq(ApeHomeworkStudent::getChapterId,apeChapter.getId())
                        .eq(ApeHomeworkStudent::getUserId,userInfo.getId());
                int count1 = apeHomeworkStudentService.count(wrapper1);
                if (count1 > 0) {
                    apeChapter.setHome("已完成");
                } else {
                    apeChapter.setHome("未完成");
                }
            }
        }
        return Result.success(chapterList);
    }

}