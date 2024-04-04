package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeForum;
import com.ape.apesystem.mapper.ApeForumMapper;
import com.ape.apesystem.service.ApeForumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 论坛service实现类
 * @date 2024/01/18 08:55
 */
@Service
public class ApeForumServiceImpl extends ServiceImpl<ApeForumMapper, ApeForum> implements ApeForumService {
}