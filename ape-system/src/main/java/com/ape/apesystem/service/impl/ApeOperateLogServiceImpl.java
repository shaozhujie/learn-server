package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeOperateLog;
import com.ape.apesystem.mapper.ApeOperateLogMapper;
import com.ape.apesystem.service.ApeOperateLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 操作日志service实现类
 * @date 2023/9/22 9:55
 */
@Service
public class ApeOperateLogServiceImpl extends ServiceImpl<ApeOperateLogMapper, ApeOperateLog> implements ApeOperateLogService {
}
