package com.ape.apesystem.mapper;

import com.ape.apesystem.domain.ApeGenTableColumn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 代码生成字段mapper
 * @date 2023/10/10 9:26
 */
public interface ApeGenTableColumnMapper extends BaseMapper<ApeGenTableColumn> {

    /**
     * @description: 获取表字段
     * @param: table
     * @return:
     * @author shaozhujie
     * @date: 2023/10/11 9:45
     */
    List<Map<String, Object>> getTableField(@Param("table") String table);
}
