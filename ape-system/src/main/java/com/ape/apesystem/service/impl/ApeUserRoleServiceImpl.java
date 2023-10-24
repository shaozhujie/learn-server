package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeUserRole;
import com.ape.apesystem.mapper.ApeUserRoleMapper;
import com.ape.apesystem.service.ApeUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 用户角色关系service实现类
 * @date 2023/8/31 14:37
 */
@Service
public class ApeUserRoleServiceImpl extends ServiceImpl<ApeUserRoleMapper, ApeUserRole> implements ApeUserRoleService {

    /**
     * 根据账号获取角色
     */
    @Override
    public Set<String> getUserRolesSet(String loginAccount) {
        return baseMapper.getUserRolesSet(loginAccount);
    }

}
