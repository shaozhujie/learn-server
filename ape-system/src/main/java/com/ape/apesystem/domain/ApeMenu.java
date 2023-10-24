package com.ape.apesystem.domain;

import com.ape.apecommon.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 菜单
 * @date 2023/8/30 9:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ape_menu")
public class ApeMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 主键数组
     */
    private String idArrary;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 父菜单
     */
    private String parentId;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 外链
     */
    private Integer target;

    /**
     * 路由地址
     */
    private String routeUrl;

    /**
     * 组件地址
     */
    private String componentUrl;

    /**
     * 参数
     */
    private String param;

    /**
     * 菜单类型
     */
    private Integer menuType;

    /**
     * 可见
     */
    private Integer visible;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 权限
     */
    private String perms;

    /**
     * 图标
     */
    private String icon;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    List<ApeMenu> children;

}
