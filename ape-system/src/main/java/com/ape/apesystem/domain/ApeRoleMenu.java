package com.ape.apesystem.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
/**
 * @author shaozhujie
 * @version 1.0
 * @description: 角色菜单关系
 * @date 2023/8/31 10:15
 */
@Data
@EqualsAndHashCode()
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ape_role_menu")
public class ApeRoleMenu {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 角色主键
     */
    private String roleId;

    /**
     * 菜单主键
     */
    private String menuId;

}
