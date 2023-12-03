package com.ape.apesystem.mapper;

import com.ape.apesystem.domain.ApeChapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 章节mapper
 * @date 2023/11/17 07:14
 */
public interface ApeChapterMapper extends BaseMapper<ApeChapter> {
    Integer getStudentVideo(@Param("taskId") String taskId, @Param("userId") String userId);
}