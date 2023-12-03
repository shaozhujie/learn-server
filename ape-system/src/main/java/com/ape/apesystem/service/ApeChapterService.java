package com.ape.apesystem.service;

import com.ape.apesystem.domain.ApeChapter;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 章节service
 * @date 2023/11/17 07:14
 */
public interface ApeChapterService extends IService<ApeChapter> {
    Integer getStudentVideo(String taskId, String userId);
}