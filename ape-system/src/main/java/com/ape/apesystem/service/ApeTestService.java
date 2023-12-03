package com.ape.apesystem.service;

import com.ape.apesystem.domain.ApeTest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 考试service
 * @date 2023/11/20 11:28
 */
public interface ApeTestService extends IService<ApeTest> {
    Map<String,Object> getStudentTotalScore(String taskId, String userId);
}