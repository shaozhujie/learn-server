package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeGenTable;
import com.ape.apesystem.mapper.ApeGenTableMapper;
import com.ape.apesystem.service.ApeGenTableService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 代码生成service实现类
 * @date 2023/10/10 9:23
 */
@Service
public class ApeGenTableServiceImpl extends ServiceImpl<ApeGenTableMapper, ApeGenTable> implements ApeGenTableService {

    /**
    * 获取数据库表
    */
    @Override
    public Page<Map<String, Object>> getTables(ApeGenTable apeGenTable) {
        Page<Map<String, Object>> page = new Page<>(apeGenTable.getPageNumber(),apeGenTable.getPageSize());
        QueryWrapper<ApeGenTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().groupBy(ApeGenTable::getTableName);
        List<ApeGenTable> apeGenTables = baseMapper.selectList(queryWrapper);
        List<String> tables = new ArrayList<>();
        apeGenTables.forEach(item -> {
            tables.add(item.getTableName());
        });
        return baseMapper.getTables(page,apeGenTable,tables);
    }

    /**
     * 获取数据库表详情
     */
    @Override
    public Map<String, Object> getTablesInfo(String table) {
        return baseMapper.getTablesInfo(table);
    }
}
