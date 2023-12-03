package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeTest;
import com.ape.apesystem.mapper.ApeTestMapper;
import com.ape.apesystem.service.ApeTestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 考试service实现类
 * @date 2023/11/20 11:28
 */
@Service
public class ApeTestServiceImpl extends ServiceImpl<ApeTestMapper, ApeTest> implements ApeTestService {
    @Override
    public Map<String,Object> getStudentTotalScore(String taskId, String userId) {
        return baseMapper.getStudentTotalScore(taskId,userId);
    }
}