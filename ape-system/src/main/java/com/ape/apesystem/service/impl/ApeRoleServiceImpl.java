package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeRole;
import com.ape.apesystem.mapper.ApeRoleMapper;
import com.ape.apesystem.service.ApeRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 角色service实现类
 * @date 2023/8/31 10:18
 */
@Service
public class ApeRoleServiceImpl extends ServiceImpl<ApeRoleMapper, ApeRole> implements ApeRoleService {
}
