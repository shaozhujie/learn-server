package com.ape.apesystem.service;

import com.ape.apesystem.domain.ApeGenTable;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 代码生成service
 * @date 2023/10/10 9:23
 */
public interface ApeGenTableService extends IService<ApeGenTable> {

    /**
    * @description: 获取数据库表
    * @param:
    * @return:
    * @author shaozhujie
    * @date: 2023/10/10 15:36
    */
    Page<Map<String,Object>> getTables(ApeGenTable apeGenTable);

    /**
     * @description: 获取数据库表详情
     * @param:
     * @return:
     * @author shaozhujie
     * @date: 2023/10/10 15:36
     */
    Map<String, Object> getTablesInfo(String table);
}
