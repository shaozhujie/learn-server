package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeGenTableColumn;
import com.ape.apesystem.mapper.ApeGenTableColumnMapper;
import com.ape.apesystem.service.ApeGenTableColumnService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 代码生成字段service实现类
 * @date 2023/10/10 9:28
 */
@Service
public class ApeGenTableColumnServiceImpl extends ServiceImpl<ApeGenTableColumnMapper, ApeGenTableColumn> implements ApeGenTableColumnService {

    /**
    * 获取表字段
    */
    @Override
    public List<Map<String, Object>> getTableField(String table) {
        return baseMapper.getTableField(table);
    }
}
