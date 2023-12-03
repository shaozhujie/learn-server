package com.ape.apesystem.domain;

import com.ape.apecommon.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 用户
 * @date 2023/8/11 16:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ape_user")
public class ApeUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 部门数组
     */
    private String postId;

    /**
     * 部门数组
     */
    private String idArrary;

    /**
    * 部门id
    */
    private String deptId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 登陆账号
     */
    private String loginAccount;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String tel;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 状态
     */
    private Integer status;

    /**
     * ip
     */
    private String loginIp;

    /**
     * 登陆日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;

    /**
     * 修改密码日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pwdUpdateDate;

    /**
     * 学校
     */
    private String school;

    /**
     * 国家
     */
    private String country;

    /**
     * 专业
     */
    private String major;

    /**
     * 职称
     */
    private String agree;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 年龄
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;

    /**
     * 资质
     */
    private String flair;

    /**
     * 地址
     */
    private String address;

    /**
     * 参加工作时间
     */
    private Integer workDate;

    /**
     * 删除标志
     */
    @TableLogic
    private Integer delFlag;

    private String remark;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private Integer pageNumber;

    @TableField(exist = false)
    private Integer pageSize;

    @TableField(exist = false)
    private List<String> roleIds;

}
