package com.ape.apesystem.domain;

import com.ape.apecommon.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 课程报名
 * @date 2023/11/21 03:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ape_task_student")
public class ApeTaskStudent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 学生id
     */
    private String userId;

    /**
     * 学生姓名
     */
    private String userName;

    /**
     * 教师id
     */
    private String teacherId;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 课程id
     */
    private String taskId;

    /**
     * 课程名称
     */
    private String taskName;

    /**
     * 报名状态 0：通过  1：未通过
     */
    private Integer state;

    @TableField(exist = false)
    private Integer pageNumber;

    @TableField(exist = false)
    private Integer pageSize;

    @TableField(exist = false)
    private String taskDescribe;

    @TableField(exist = false)
    private Integer num;

    @TableField(exist = false)
    private String image;

    @TableField(exist = false)
    private String homework;

    @TableField(exist = false)
    private String video;

    @TableField(exist = false)
    private String chapterId;

    @TableField(exist = false)
    private String testScore;

    @TableField(exist = false)
    private String totalScore;

    @TableField(exist = false)
    private Integer videoCount;

    @TableField(exist = false)
    private Integer assignCount;

    @TableField(exist = false)
    private Integer assign;

    @TableField(exist = false)
    private Integer videoNum;

    @TableField(exist = false)
    private Integer proportion;

}