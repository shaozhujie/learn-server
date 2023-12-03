package com.ape.apesystem.mapper;

import com.ape.apesystem.domain.ApeHomework;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 作业mapper
 * @date 2023/11/18 09:06
 */
public interface ApeHomeworkMapper extends BaseMapper<ApeHomework> {
    List<String> getStudentHomeWork(@Param("taskId") String taskId, @Param("userId") String userId);

    List<String> getTotalAssignCount(@Param("taskId") String taskId);
}