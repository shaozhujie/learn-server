package com.ape.apesystem.mapper;

import com.ape.apesystem.domain.ApeUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 用户角色关系mapper
 * @date 2023/8/31 14:34
 */
public interface ApeUserRoleMapper extends BaseMapper<ApeUserRole> {

    /**
     * 根据账号获取角色
     */
    Set<String> getUserRolesSet(@Param("loginAccount") String loginAccount);
}
