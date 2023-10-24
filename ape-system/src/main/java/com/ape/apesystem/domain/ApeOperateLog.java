package com.ape.apesystem.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 操作日志
 * @date 2023/9/22 9:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ape_operate_log")
public class ApeOperateLog {

    private static final long serialVersionUID = 1L;
    /**
     *主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     *模块名称
     */
    private String name;

    /**
     *业务类型0其他1新增2修改3删除
     */
    private Integer type;

    /**
     *方法名称
     */
    private String method;

    /**
     *请求类型
     */
    private String requestType;

    /**
     *操作类型0其他1后台2移动端
     */
    private Integer operType;

    /**
     *操作用户id
     */
    private String operUserId;

    /**
     *操作用户名称
     */
    private String operUserName;

    /**
     *操作用户账号
     */
    private String operUserAccount;

    /**
     *操作时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    /**
     *请求url
     */
    private String operUrl;

    /**
     *操作ip
     */
    private String operIp;

    /**
     *操作地点
     */
    private String operLocation;

    /**
     *请求参数
     */
    private String operParam;

    /**
     *返回参数
     */
    private String resultParam;

    /**
     *操作状态0正常1异常
     */
    private Integer state;

    /**
     *错误消息
     */
    private String errorMsg;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @TableField(exist = false)
    private Integer pageNumber;

    @TableField(exist = false)
    private Integer pageSize;


}
