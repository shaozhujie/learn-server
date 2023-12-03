package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeMessage;
import com.ape.apesystem.mapper.ApeMessageMapper;
import com.ape.apesystem.service.ApeMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 留言表service实现类
 * @date 2023/11/16 08:50
 */
@Service
public class ApeMessageServiceImpl extends ServiceImpl<ApeMessageMapper, ApeMessage> implements ApeMessageService {
}