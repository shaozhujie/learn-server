package com.ape.apesystem.mapper;

import com.ape.apesystem.domain.ApeTest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 考试mapper
 * @date 2023/11/20 11:28
 */
public interface ApeTestMapper extends BaseMapper<ApeTest> {
    Map<String,Object> getStudentTotalScore(@Param("taskId") String taskId,@Param("userId") String userId);
}