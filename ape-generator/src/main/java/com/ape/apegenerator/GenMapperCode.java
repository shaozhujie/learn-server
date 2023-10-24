package com.ape.apegenerator;

import com.ape.apesystem.domain.ApeGenTable;
import com.ape.apesystem.domain.ApeGenTableColumn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: mapper接口代码生成类
 * @date 2023/10/13 15:36
 */
public class GenMapperCode {

    public static String genMapper(ApeGenTable apeGenTable, List<ApeGenTableColumn> columnList) {
        StringBuilder builder = new StringBuilder();
        builder.append("package com.ape.apesystem.mapper;\n" + "\n" + "import com.ape.apesystem.domain.").append(apeGenTable.getClassName()).append(";\n").append("import com.baomidou.mybatisplus.core.mapper.BaseMapper;\n\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        String format = sdf.format(new Date());
        builder.append("/**\n" + " * @author ").append(apeGenTable.getFunctionAuthor()).append("\n").append(" * @version 1.0\n").append(" * @description: ").append(apeGenTable.getBusinessName()).append("mapper\n").append(" * @date ").append(format).append("\n").append(" */\n");
        builder.append("public interface ").append(apeGenTable.getClassName()).append("Mapper extends BaseMapper<").append(apeGenTable.getClassName()).append("> {\n").append("}");
        return builder.toString();
    }

    public static  String genMapperXml(ApeGenTable apeGenTable, List<ApeGenTableColumn> columnList) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + "<!DOCTYPE mapper\n" + "        PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n" + "        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" + "<mapper namespace=\"com.ape.apesystem.mapper." + apeGenTable.getClassName() + "Mapper\">\n" + "\n" + "</mapper>";
    }

}
