package com.ape.apeadmin.controller.menu;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.*;
import com.ape.apesystem.service.ApeMenuService;
import com.ape.apesystem.service.ApeRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 菜单controller
 * @date 2023/8/30 9:25
 */
@Controller
@ResponseBody
@RequestMapping("menu")
public class ApeMenuController {

    @Autowired
    private ApeMenuService apeMenuService;
    @Autowired
    private ApeRoleMenuService apeRoleMenuService;

    /** 获取菜单列表 */
    @Log(name = "获取菜单列表", type = BusinessType.OTHER)
    @PostMapping("getMenuList")
    public Result getMenuList(@RequestBody ApeMenu apeMenu) {
        //构造查询条件
        QueryWrapper<ApeMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(apeMenu.getMenuName()),ApeMenu::getMenuName,apeMenu.getMenuName())
                .eq(apeMenu.getStatus() != null,ApeMenu::getStatus,apeMenu.getStatus()).orderByAsc(ApeMenu::getOrderNum);
        //查询
        List<ApeMenu> apeMenus = apeMenuService.list(queryWrapper);
        //筛选出第一级
        List<ApeMenu> first = apeMenus.stream().filter(item -> "0".equals(item.getParentId())).collect(Collectors.toList());
        if (first.size() <= 0) {
            return Result.success(apeMenus);
        } else {
            for (ApeMenu menu : first) {
                filterMenu(menu,apeMenus);
            }
            return Result.success(first);
        }
    }

    /** 递归查询下级菜单 */
    public void filterMenu(ApeMenu apeMenu,List<ApeMenu> apeMenus) {
        List<ApeMenu> menus = new ArrayList<>();
        for (ApeMenu menu : apeMenus) {
            if (apeMenu.getId().equals(menu.getParentId())) {
                menus.add(menu);
                filterMenu(menu,apeMenus);
            }
        }
        apeMenu.setChildren(menus);
    }

    @Log(name = "根据id获取菜单", type = BusinessType.OTHER)
    @GetMapping("getById")
    public Result getById(@RequestParam("id")String id) {
        ApeMenu apeMenu = apeMenuService.getById(id);
        return Result.success(apeMenu);
    }

    /** 保存菜单 */
    @Log(name = "保存菜单保存菜单", type = BusinessType.INSERT)
    @PostMapping("saveMenu")
    public Result saveMenu(@RequestBody ApeMenu apeMenu) {
        if (apeMenu.getMenuType() != 0 ) {
            QueryWrapper<ApeMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(ApeMenu::getPerms,apeMenu.getPerms());
            int count = apeMenuService.count(queryWrapper);
            if (count > 0) {
                return Result.fail("权限字符已存在！");
            }
        }
        boolean save = apeMenuService.save(apeMenu);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑菜单 */
    @Log(name = "编辑菜单", type = BusinessType.UPDATE)
    @PostMapping("editMenu")
    public Result editMenu(@RequestBody ApeMenu apeMenu) {
        if (StringUtils.isNotBlank(apeMenu.getIdArrary())) {
            apeMenu.setParentId("0");
        }
        ApeMenu menu = apeMenuService.getById(apeMenu.getId());
        if (apeMenu.getMenuType() != 0 ) {
            if (!menu.getPerms().equals(apeMenu.getPerms())) {
                QueryWrapper<ApeMenu> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(ApeMenu::getPerms,apeMenu.getPerms());
                int count = apeMenuService.count(queryWrapper);
                if (count > 0) {
                    return Result.fail("权限字符已存在！");
                }
            }
        }
        boolean update = apeMenuService.updateById(apeMenu);
        if (update) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除菜单 */
    @Log(name = "删除菜单", type = BusinessType.DELETE)
    @GetMapping("removeMenu")
    public Result removeMenu(@RequestParam("id")String id) {
        //先查有没有分配给用户
        QueryWrapper<ApeRoleMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ApeRoleMenu::getMenuId,id);
        int num = apeRoleMenuService.count(wrapper);
        if (num > 0) {
            return Result.fail("菜单已分配给角色，请先解除角色菜单！");
        }
        QueryWrapper<ApeMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeMenu::getParentId,id);
        int count = apeMenuService.count(queryWrapper);
        if (count > 0) {
            return Result.fail("存在下级菜单,请先删除下级菜单！");
        }
        boolean remove = apeMenuService.removeById(id);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 根据用户获取菜单权限 */
    @Log(name = "根据用户获取菜单权限", type = BusinessType.OTHER)
    @GetMapping("getMenuByUser")
    public Result getMenuByUser() {
        ApeUser apeUser = ShiroUtils.getUserInfo();
        List<ApeMenu> menus = apeMenuService.getMenuByUser(apeUser.getId());
        return Result.success(menus);
    }

}
