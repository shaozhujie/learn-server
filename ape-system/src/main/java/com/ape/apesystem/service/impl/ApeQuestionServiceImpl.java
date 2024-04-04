package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeQuestion;
import com.ape.apesystem.mapper.ApeQuestionMapper;
import com.ape.apesystem.service.ApeQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 答疑service实现类
 * @date 2024/01/18 11:14
 */
@Service
public class ApeQuestionServiceImpl extends ServiceImpl<ApeQuestionMapper, ApeQuestion> implements ApeQuestionService {
}