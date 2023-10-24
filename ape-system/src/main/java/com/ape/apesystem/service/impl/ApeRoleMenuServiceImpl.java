package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeRoleMenu;
import com.ape.apesystem.mapper.ApeRoleMenuMapper;
import com.ape.apesystem.service.ApeRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 角色菜单关系service实现类
 * @date 2023/8/31 10:57
 */
@Service
public class ApeRoleMenuServiceImpl extends ServiceImpl<ApeRoleMenuMapper, ApeRoleMenu> implements ApeRoleMenuService {

    /**
    *  根据角色获取权限
    */
    @Override
    public Set<String> getRoleMenusSet(String role) {
        return baseMapper.getRoleMenusSet(role);
    }
}
