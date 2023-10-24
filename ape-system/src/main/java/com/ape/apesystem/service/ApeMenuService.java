package com.ape.apesystem.service;

import com.ape.apesystem.domain.ApeMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 菜单service
 * @date 2023/8/30 9:23
 */
public interface ApeMenuService extends IService<ApeMenu> {

    /**
    * @description: 根据用户获取菜单权限
    * @param: id
    * @return:
    * @author shaozhujie
    * @date: 2023/9/13 9:39
    */
    List<ApeMenu> getMenuByUser(String id);

}
