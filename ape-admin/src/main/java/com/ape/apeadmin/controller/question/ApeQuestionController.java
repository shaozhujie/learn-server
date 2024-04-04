package com.ape.apeadmin.controller.question;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.ApeQuestion;
import com.ape.apesystem.domain.ApeTask;
import com.ape.apesystem.domain.ApeUser;
import com.ape.apesystem.service.ApeQuestionService;
import com.ape.apesystem.service.ApeTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 答疑controller
 * @date 2024/01/18 11:14
 */
@Controller
@ResponseBody
@RequestMapping("question")
public class ApeQuestionController {

    @Autowired
    private ApeQuestionService apeQuestionService;
    @Autowired
    private ApeTaskService apeTaskService;

    /** 分页获取答疑 */
    @Log(name = "分页获取答疑", type = BusinessType.OTHER)
    @PostMapping("getApeQuestionPage")
    public Result getApeQuestionPage(@RequestBody ApeQuestion apeQuestion) {
        Page<ApeQuestion> page = new Page<>(apeQuestion.getPageNumber(),apeQuestion.getPageSize());
        QueryWrapper<ApeQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(apeQuestion.getUserName()),ApeQuestion::getUserName,apeQuestion.getUserName())
                .like(StringUtils.isNotBlank(apeQuestion.getContent()),ApeQuestion::getContent,apeQuestion.getContent())
                .like(StringUtils.isNotBlank(apeQuestion.getAnswer()),ApeQuestion::getAnswer,apeQuestion.getAnswer())
                .eq(StringUtils.isNotBlank(apeQuestion.getTaskId()),ApeQuestion::getTaskId,apeQuestion.getTaskId())
                .eq(StringUtils.isNotBlank(apeQuestion.getTeacherId()),ApeQuestion::getTeacherId,apeQuestion.getTeacherId())
                .like(StringUtils.isNotBlank(apeQuestion.getTaskName()),ApeQuestion::getTaskName,apeQuestion.getTaskName());
        Page<ApeQuestion> apeQuestionPage = apeQuestionService.page(page, queryWrapper);
        return Result.success(apeQuestionPage);
    }

    /** 根据id获取答疑 */
    @Log(name = "根据id获取答疑", type = BusinessType.OTHER)
    @GetMapping("getApeQuestionById")
    public Result getApeQuestionById(@RequestParam("id")String id) {
        ApeQuestion apeQuestion = apeQuestionService.getById(id);
        return Result.success(apeQuestion);
    }

    /** 保存答疑 */
    @Log(name = "保存答疑", type = BusinessType.INSERT)
    @PostMapping("saveApeQuestion")
    public Result saveApeQuestion(@RequestBody ApeQuestion apeQuestion) {
        ApeUser user = ShiroUtils.getUserInfo();
        apeQuestion.setUserId(user.getId());
        apeQuestion.setUserName(user.getUserName());
        ApeTask task = apeTaskService.getById(apeQuestion.getTaskId());
        apeQuestion.setTaskId(task.getId());
        apeQuestion.setTaskName(task.getName());
        apeQuestion.setTeacherId(task.getTeacherId());
        boolean save = apeQuestionService.save(apeQuestion);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑答疑 */
    @Log(name = "编辑答疑", type = BusinessType.UPDATE)
    @PostMapping("editApeQuestion")
    public Result editApeQuestion(@RequestBody ApeQuestion apeQuestion) {
        boolean save = apeQuestionService.updateById(apeQuestion);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除答疑 */
    @GetMapping("removeApeQuestion")
    @Log(name = "删除答疑", type = BusinessType.DELETE)
    public Result removeApeQuestion(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeQuestionService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("答疑id不能为空！");
        }
    }

}