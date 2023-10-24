package com.ape.apegenerator;

import com.ape.apesystem.domain.ApeGenTable;
import com.ape.apesystem.domain.ApeGenTableColumn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: TODO
 * @date 2023/10/13 16:35
 */
public class GenServiceCode {

    public static String genService(ApeGenTable apeGenTable, List<ApeGenTableColumn> columnList) {
        StringBuilder builder = new StringBuilder();
        builder.append("package com.ape.apesystem.service;\n" + "\n" + "import com.ape.apesystem.domain.").append(apeGenTable.getClassName()).append(";\n").append("import com.baomidou.mybatisplus.extension.service.IService;\n\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        String format = sdf.format(new Date());
        builder.append("/**\n" + " * @author ").append(apeGenTable.getFunctionAuthor()).append("\n").append(" * @version 1.0\n").append(" * @description: ").append(apeGenTable.getBusinessName()).append("service\n").append(" * @date ").append(format).append("\n").append(" */\n");
        builder.append("public interface ").append(apeGenTable.getClassName()).append("Service extends IService<").append(apeGenTable.getClassName()).append("> {\n").append("}");
        return builder.toString();
    }

    public static String genServiceImpl(ApeGenTable apeGenTable, List<ApeGenTableColumn> columnList) {
        StringBuilder builder = new StringBuilder();
        builder.append("package com.ape.apesystem.service.impl;\n" + "\n" + "import com.ape.apesystem.domain.").append(apeGenTable.getClassName()).append(";\n").append("import com.ape.apesystem.mapper.").append(apeGenTable.getClassName()).append("Mapper;\n").append("import com.ape.apesystem.service.").append(apeGenTable.getClassName()).append("Service;\n").append("import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\n").append("import org.springframework.stereotype.Service;\n\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        String format = sdf.format(new Date());
        builder.append("/**\n" + " * @author ").append(apeGenTable.getFunctionAuthor()).append("\n").append(" * @version 1.0\n").append(" * @description: ").append(apeGenTable.getBusinessName()).append("service实现类\n").append(" * @date ").append(format).append("\n").append(" */\n");
        builder.append("@Service\n" +
                "public class "+ apeGenTable.getClassName() +"ServiceImpl extends ServiceImpl<"+ apeGenTable.getClassName() +"Mapper, "+ apeGenTable.getClassName() +"> implements "+ apeGenTable.getClassName() +"Service {\n" +
                "}");
        return builder.toString();
    }
}
