package com.ape.apesystem.service;

import com.ape.apesystem.domain.ApeHomework;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 作业service
 * @date 2023/11/18 09:06
 */
public interface ApeHomeworkService extends IService<ApeHomework> {
    List<String> getStudentHomeWork(String taskId, String userId);

    List<String> getTotalAssignCount(String taskId);
}