package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeHomework;
import com.ape.apesystem.mapper.ApeHomeworkMapper;
import com.ape.apesystem.service.ApeHomeworkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 作业service实现类
 * @date 2023/11/18 09:06
 */
@Service
public class ApeHomeworkServiceImpl extends ServiceImpl<ApeHomeworkMapper, ApeHomework> implements ApeHomeworkService {
    @Override
    public List<String> getStudentHomeWork(String taskId, String userId) {
        return baseMapper.getStudentHomeWork(taskId, userId);
    }

    @Override
    public List<String> getTotalAssignCount(String taskId) {
        return baseMapper.getTotalAssignCount(taskId);
    }
}