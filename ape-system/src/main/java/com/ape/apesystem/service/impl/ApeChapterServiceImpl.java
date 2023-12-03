package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeChapter;
import com.ape.apesystem.mapper.ApeChapterMapper;
import com.ape.apesystem.service.ApeChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 章节service实现类
 * @date 2023/11/17 07:14
 */
@Service
public class ApeChapterServiceImpl extends ServiceImpl<ApeChapterMapper, ApeChapter> implements ApeChapterService {
    @Override
    public Integer getStudentVideo(String taskId, String userId) {
        return baseMapper.getStudentVideo(taskId, userId);
    }
}