package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeTask;
import com.ape.apesystem.mapper.ApeTaskMapper;
import com.ape.apesystem.service.ApeTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 课程service实现类
 * @date 2023/11/17 11:28
 */
@Service
public class ApeTaskServiceImpl extends ServiceImpl<ApeTaskMapper, ApeTask> implements ApeTaskService {
}