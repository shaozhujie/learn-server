package com.ape.apesystem.mapper;

import com.ape.apesystem.domain.ApeGenTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 代码生成mapper
 * @date 2023/10/10 9:22
 */
public interface ApeGenTableMapper extends BaseMapper<ApeGenTable> {
    
    /**
    * @description: 获取数据库表
    * @param:
    * @return: 
    * @author shaozhujie
    * @date: 2023/10/10 15:37
    */
    Page<Map<String, Object>> getTables(Page<Map<String, Object>> page, @Param("ew") ApeGenTable apeGenTable,@Param("tables") List<String> tables);

    /**
     * @description: 获取数据库表详情
     * @param:
     * @return:
     * @author shaozhujie
     * @date: 2023/10/10 15:37
     */
    Map<String, Object> getTablesInfo(@Param("tableName") String table);
}
