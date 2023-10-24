package com.ape.apesystem.domain;

import com.ape.apecommon.domain.BaseEntity;
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
 * @description: 代码生成字段表
 * @date 2023/10/9 11:28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ape_gen_table_column")
public class ApeGenTableColumn  extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 表id
     */
    private String tableId;

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 字段描述
     */
    private String columnComment;

    /**
     * 字段类型
     */
    private String columnType;

    /**
     * Java类型
     */
    private String javaType;

    /**
     * Java属性名
     */
    private String javaField;

    /**
     * 主键
     */
    private Integer isPk;

    /**
     * 必填
     */
    private Integer isRequired;

    /**
     * 插入
     */
    private Integer isInsert;

    /**
     * 编辑
     */
    private Integer isEdit;

    /**
     * 列表
     */
    private Integer isList;

    /**
     * 查询
     */
    private Integer isQuery;

    /**
     * 查询方式
     */
    private String queryType;

    /**
     * 文本类型
     */
    private String htmlType;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 排序
     */
    private String sort;

}
