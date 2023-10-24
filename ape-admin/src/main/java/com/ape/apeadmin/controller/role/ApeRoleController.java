package com.ape.apeadmin.controller.role;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apesystem.domain.ApeRole;
import com.ape.apesystem.domain.ApeRoleMenu;
import com.ape.apesystem.domain.ApeUserRole;
import com.ape.apesystem.service.ApeRoleMenuService;
import com.ape.apesystem.service.ApeRoleService;
import com.ape.apesystem.service.ApeUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: TODO
 * @date 2023/8/31 10:21
 */
@Controller
@ResponseBody
@RequestMapping("role")
public class ApeRoleController {

    @Autowired
    private ApeRoleService apeRoleService;
    @Autowired
    private ApeRoleMenuService apeRoleMenuService;
    @Autowired
    private ApeUserRoleService apeUserRoleService;

    /** 分页获取角色 */
    @Log(name = "分页获取角色", type = BusinessType.OTHER)
    @PostMapping("getRolePage")
    public Result getRolePage(@RequestBody ApeRole apeRole) {
        QueryWrapper<ApeRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(apeRole.getRoleName()),ApeRole::getRoleName,apeRole.getRoleName())
                .like(StringUtils.isNotBlank(apeRole.getRoleKey()),ApeRole::getRoleKey,apeRole.getRoleKey())
                .eq(apeRole.getStatus() != null,ApeRole::getStatus,apeRole.getStatus());
        Page<ApeRole> page = new Page<>(apeRole.getPageNumber(),apeRole.getPageSize());
        Page<ApeRole> rolePage = apeRoleService.page(page, queryWrapper);
        return Result.success(rolePage);
    }

    /** 获取角色列表 */
    @Log(name = "获取角色列表", type = BusinessType.OTHER)
    @PostMapping("getRoleList")
    public Result getRoleList(@RequestBody ApeRole apeRole) {
        QueryWrapper<ApeRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(apeRole.getRoleName()),ApeRole::getRoleName,apeRole.getRoleName())
                .like(StringUtils.isNotBlank(apeRole.getRoleKey()),ApeRole::getRoleKey,apeRole.getRoleKey())
                .eq(apeRole.getStatus() != null,ApeRole::getStatus,apeRole.getStatus());
        List<ApeRole> apeRoleList = apeRoleService.list(queryWrapper);
        return Result.success(apeRoleList);
    }

    /** 根据id获取角色 */
    @Log(name = "根据id获取角色", type = BusinessType.OTHER)
    @GetMapping("getRoleById")
    public Result getRoleById(@RequestParam("id")String id) {
        ApeRole apeRole = apeRoleService.getById(id);
        QueryWrapper<ApeRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeRoleMenu::getRoleId,apeRole.getId());
        List<ApeRoleMenu> apeRoleMenus = apeRoleMenuService.list(queryWrapper);
        List<String> menuIds = new ArrayList<>();
        for (ApeRoleMenu apeRoleMenu : apeRoleMenus) {
            menuIds.add(apeRoleMenu.getMenuId());
        }
        apeRole.setMenuIds(menuIds);
        return Result.success(apeRole);
    }

    /** 保存角色 */
    @Log(name = "保存角色", type = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("saveRole")
    public Result saveRole(@RequestBody ApeRole apeRole) {
        QueryWrapper<ApeRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeRole::getRoleKey,apeRole.getRoleKey());
        int count = apeRoleService.count(queryWrapper);
        if (count > 0) {
            return Result.fail("权限字符已存在！");
        }
        //先保存角色
        String uuid = IdWorker.get32UUID();
        apeRole.setId(uuid);
        apeRoleService.save(apeRole);
        //再保存角色菜单关系
        List<String> menuIds = apeRole.getMenuIds();
        List<ApeRoleMenu> apeRoleMenuList = new ArrayList<>();
        for (String menuId : menuIds) {
            ApeRoleMenu apeRoleMenu = new ApeRoleMenu();
            apeRoleMenu.setRoleId(uuid);
            apeRoleMenu.setMenuId(menuId);
            apeRoleMenuList.add(apeRoleMenu);
        }
        apeRoleMenuService.saveBatch(apeRoleMenuList);
        return Result.success();
    }

    /** 编辑角色 */
    @Log(name = "编辑角色", type = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("editRole")
    public Result editRole(@RequestBody ApeRole apeRole) {
        ApeRole role = apeRoleService.getById(apeRole.getId());
        if (!role.getRoleKey().equals(apeRole.getRoleKey())) {
            QueryWrapper<ApeRole> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ApeRole::getRoleKey,apeRole.getRoleKey());
            int count = apeRoleService.count(wrapper);
            if (count > 0) {
                return Result.fail("权限字符已存在！");
            }
        }
        //更新角色
        apeRoleService.updateById(apeRole);
        //把角色菜单关系删除
        QueryWrapper<ApeRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeRoleMenu::getRoleId,apeRole.getId());
        apeRoleMenuService.remove(queryWrapper);
        //删除之后再重新保存
        List<String> menuIds = apeRole.getMenuIds();
        List<ApeRoleMenu> apeRoleMenuList = new ArrayList<>();
        for (String menuId : menuIds) {
            ApeRoleMenu apeRoleMenu = new ApeRoleMenu();
            apeRoleMenu.setRoleId(apeRole.getId());
            apeRoleMenu.setMenuId(menuId);
            apeRoleMenuList.add(apeRoleMenu);
        }
        apeRoleMenuService.saveBatch(apeRoleMenuList);
        return Result.success();
    }

    /** 删除角色 */
    @Log(name = "删除角色", type = BusinessType.DELETE)
    @Transactional(rollbackFor = Exception.class)
    @GetMapping("removeRole")
    public Result removeRole(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                //先查有没有分配给用户
                QueryWrapper<ApeUserRole> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(ApeUserRole::getRoleId,id);
                int count = apeUserRoleService.count(wrapper);
                if (count > 0) {
                    return Result.fail("角色已分配给用户，请先解除用户角色！");
                }
                //删除角色和菜单关系
                apeRoleService.removeById(id);
                QueryWrapper<ApeRoleMenu> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(ApeRoleMenu::getRoleId,id);
                apeRoleMenuService.remove(queryWrapper);
            }
            return Result.success();
        } else {
            return Result.fail("角色id不能为空！");
        }
    }

}
