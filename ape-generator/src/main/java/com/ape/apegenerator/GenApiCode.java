package com.ape.apegenerator;

import com.ape.apecommon.utils.HumpUtils;
import com.ape.apesystem.domain.ApeGenTable;
import com.ape.apesystem.domain.ApeGenTableColumn;

import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: api代码生成类
 * @date 2023/10/14 9:45
 */
public class GenApiCode {

    public static String genApi(ApeGenTable apeGenTable, List<ApeGenTableColumn> columnList) {
        String name = apeGenTable.getTableName().substring(apeGenTable.getTableName().lastIndexOf("_") + 1);
        String toCamel = HumpUtils.toCamel(apeGenTable.getTableName(), "_");
        return "//-------------------------------" + apeGenTable.getBusinessName() + "---------------------------------------\n" +
                "//查询" + apeGenTable.getBusinessName() + "\n" + "export const get" + apeGenTable.getClassName() + "Page = (params) => post(\"/" + name + "/get" + toCamel + "Page\",params)\n" +
                "//根据id查询" + apeGenTable.getBusinessName() + "\n" + "export const get" + apeGenTable.getClassName() + "ById = (params) => get(\"/" + name + "/get" + apeGenTable.getClassName() + "ById\",params)\n" +
                "//保存" + apeGenTable.getBusinessName() + "\n" + "export const save" + apeGenTable.getClassName() + " = (params) => post(\"/" + name + "/save" + apeGenTable.getClassName() + "\",params)\n" +
                "//更新" + apeGenTable.getBusinessName() + "\n" + "export const edit" + apeGenTable.getClassName() + " = (params) => post(\"/" + name + "/edit" + apeGenTable.getClassName() + "\",params)\n" +
                "//删除" + apeGenTable.getClassName() + "\n" + "export const remove" + apeGenTable.getClassName() + " = (params) => get(\"/" + name + "/remove" + apeGenTable.getClassName() + "\",params)";
    }

}
