package com.ape.apesystem.mapper;

import com.ape.apesystem.domain.ApeDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 部门mapper
 * @date 2023/8/28 10:11
 */
public interface ApeDeptMapper extends BaseMapper<ApeDept> {

    /**
    * @description: 查询下拉列表
    * @return: List
    * @author shaozhujie
    * @date: 2023/8/29 10:43
    */
    List<Map<String, Object>> getDeptDrop();

    /**
     * @description: 查询一级部门
     * @return: List
     * @author shaozhujie
     * @date: 2023/8/29 10:43
     */
    List<Map<String, Object>> getDeptDropFirst();
}
