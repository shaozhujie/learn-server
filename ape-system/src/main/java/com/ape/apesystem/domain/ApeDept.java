package com.ape.apesystem.domain;

import com.ape.apecommon.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 部门
 * @date 2023/8/28 10:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ape_dept")
public class ApeDept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 主键数组
     */
    private String idArrary;

    /**
     * 上级id
     */
    private String parentId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 顺序
     */
    private Integer orderNum;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 删除标志
     */
    @TableLogic
    private Integer delFlag;

    @TableField(exist = false)
    List<ApeDept> children;

}
