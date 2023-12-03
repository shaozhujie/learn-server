package com.ape.apesystem.domain;

import com.ape.apecommon.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 用户考试题目
 * @date 2023/11/24 10:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ape_test_student")
public class ApeTestStudent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String itemId;

    /**
     * 考试id
     */
    private String testId;

    /**
     * 题目
     */
    private String title;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 类型 0：单选 1：多选 2：填空 3：判断 4：问答题 5：计算题
     */
    private Integer type;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 得分点
     */
    private String keyword;

    /**
     * 答案
     */
    private String answer;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 得分
     */
    private Integer point;

    /**
     * 答案
     */
    private String solution;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private Integer totalScore;
    @TableField(exist = false)
    private Integer totalGetScore;
    @TableField(exist = false)
    private String testName;

    @TableField(exist = false)
    private Integer pageNumber;

    @TableField(exist = false)
    private Integer pageSize;
}