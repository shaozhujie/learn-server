package com.ape.apesystem.mapper;

import com.ape.apesystem.domain.ApeUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 用户mapper
 * @date 2023/8/28 8:41
 */
public interface ApeUserMapper extends BaseMapper<ApeUser> {

    /**
    * 分页查询用户
    */
    Page<ApeUser> getUserPage(Page<ApeUser> page, @Param("ew")ApeUser apeUser);

}
