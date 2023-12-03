package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeClassification;
import com.ape.apesystem.mapper.ApeClassificationMapper;
import com.ape.apesystem.service.ApeClassificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 分类service实现类
 * @date 2023/11/17 02:15
 */
@Service
public class ApeClassificationServiceImpl extends ServiceImpl<ApeClassificationMapper, ApeClassification> implements ApeClassificationService {
}