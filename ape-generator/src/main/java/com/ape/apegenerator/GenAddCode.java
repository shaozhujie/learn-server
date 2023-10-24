package com.ape.apegenerator;

import com.ape.apecommon.utils.HumpUtils;
import com.ape.apecommon.utils.StringUtils;
import com.ape.apesystem.domain.ApeDictData;
import com.ape.apesystem.domain.ApeGenTable;
import com.ape.apesystem.domain.ApeGenTableColumn;
import com.ape.apesystem.service.ApeDictDataService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: add代码生成类
 * @date 2023/10/16 9:08
 */
@Component
public class GenAddCode {

    @Autowired
    private ApeDictDataService apeDictDataService;

    public String genAdd(ApeGenTable apeGenTable, List<ApeGenTableColumn> columnList) {
        StringBuilder builder = new StringBuilder();
        String name = apeGenTable.getTableName().substring(apeGenTable.getTableName().lastIndexOf("_"));
        String toCamel = HumpUtils.toCamel(apeGenTable.getTableName(), "_");
        String toSmallCamel = HumpUtils.toSmallCamel(apeGenTable.getTableName(), "_");
        builder.append("<template>\n" + "<div>\n" + "  <el-dialog title=\"新增").append(apeGenTable.getTableComment()).append("\" width=\"40%\" :destroy-on-close=\"true\" :visible.sync=\"addVisible\" :before-close=\"handleClose\">\n").append("    <el-form :model=\"form\" :rules=\"rules\" ref=\"ruleForm\">\n").append("      <el-row :gutter=\"10\">\n");
        for (ApeGenTableColumn apeGenTableColumn : columnList) {
            if (apeGenTableColumn.getIsInsert() == 1 && (!"id".equals(apeGenTableColumn.getJavaField()) && !"createBy".equals(apeGenTableColumn.getJavaField()) && !"createTime".equals(apeGenTableColumn.getJavaField()) && !"updateBy".equals(apeGenTableColumn.getJavaField()) && !"updateTime".equals(apeGenTableColumn.getJavaField()))) {
                if ("文本框".equals(apeGenTableColumn.getHtmlType())) {
                    builder.append("        <el-col :xs=\"24\" :sm=\"24\" :md=\"12\" :lg=\"12\" :xl=\"12\">\n" + "            <span class=\"search-title\">\n" + "                ").append(apeGenTableColumn.getColumnComment()).append(":\n").append("                </span>\n").append("            <el-form-item prop=\"").append(apeGenTableColumn.getJavaField()).append("\" style=\"margin-bottom:0\">\n").append("                <el-input v-model=\"form.").append(apeGenTableColumn.getJavaField()).append("\" size=\"mini\" placeholder=\"请输入").append(apeGenTableColumn.getColumnComment()).append("\" autocomplete=\"off\"></el-input>\n").append("            </el-form-item>\n").append("        </el-col>\n");
                } else if ("文本域".equals(apeGenTableColumn.getHtmlType())) {
                    builder.append("        <el-col :xs=\"24\" :sm=\"24\" :md=\"24\" :lg=\"24\" :xl=\"24\">\n" + "            <span class=\"search-title\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(apeGenTableColumn.getColumnComment()).append(":</span>\n").append("            <el-input type=\"textarea\" v-model=\"form.").append(apeGenTableColumn.getJavaField()).append("\"></el-input>\n").append("        </el-col>\n");
                } else if ("下拉框".equals(apeGenTableColumn.getHtmlType())){
                    builder.append("        <el-col :xs=\"24\" :sm=\"24\" :md=\"12\" :lg=\"12\" :xl=\"12\">\n" + "            <span class=\"search-title\">\n" + "                ").append(apeGenTableColumn.getColumnComment()).append(":\n").append("                </span>\n").append("            <el-form-item prop=\"").append(apeGenTableColumn.getJavaField()).append("\" style=\"margin-bottom:0\">\n").append("                <el-select size=\"mini\" clearable v-model=\"form.").append(apeGenTableColumn.getJavaField()).append("\" placeholder=\"请选择").append(apeGenTableColumn.getColumnComment()).append("\">\n");
                    if (StringUtils.isNotBlank(apeGenTableColumn.getDictType())) {
                        QueryWrapper<ApeDictData> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(ApeDictData::getDictCode,apeGenTableColumn.getDictType());
                        List<ApeDictData> dictData = apeDictDataService.list(queryWrapper);
                        for (ApeDictData dict : dictData) {
                            builder.append("                  <el-option\n" + "                        label=\"").append(dict.getDictLabel()).append("\"\n").append("                        value=\"").append(dict.getDictValue()).append("\">\n").append("                      </el-option>\n");
                        }
                    }
                    builder.append("                </el-select>\n" +
                            "            </el-form-item>\n" +
                            "        </el-col>\n");
                } else if ("单选框".equals(apeGenTableColumn.getHtmlType())){
                    builder.append("        <el-col :xs=\"24\" :sm=\"24\" :md=\"12\" :lg=\"12\" :xl=\"12\">\n" + "            <span class=\"search-title\">").append(apeGenTableColumn.getColumnComment()).append(":</span>\n").append("             <el-form-item prop=\"").append(apeGenTableColumn.getJavaField()).append("\" style=\"margin-bottom:0\">\n").append("                <el-radio-group style=\"margin-top:1px;height:40px;display:flex;align-items: center\" v-model=\"form.").append(apeGenTableColumn.getJavaField()).append("\">\n");
                    if (StringUtils.isNotBlank(apeGenTableColumn.getDictType())) {
                        QueryWrapper<ApeDictData> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(ApeDictData::getDictCode,apeGenTableColumn.getDictType());
                        List<ApeDictData> dictData = apeDictDataService.list(queryWrapper);
                        for (ApeDictData dict : dictData) {
                            builder.append("                  <el-radio label=\"").append(dict.getDictValue()).append("\">").append(dict.getDictLabel()).append("</el-radio>\n");
                        }
                    }
                    builder.append("                </el-radio-group>\n" +
                            "            </el-form-item>\n" +
                            "        </el-col>\n");
                } else if ("日期控件".equals(apeGenTableColumn.getHtmlType())){
                    builder.append("        <el-col :xs=\"24\" :sm=\"24\" :md=\"12\" :lg=\"12\" :xl=\"12\">\n" + "            <span class=\"search-title\">").append(apeGenTableColumn.getColumnComment()).append(":</span>\n").append("             <el-form-item prop=\"").append(apeGenTableColumn.getJavaField()).append("\" style=\"margin-bottom:0\">\n").append("                <el-date-picker\n").append("                  v-model=\"form.").append(apeGenTableColumn.getJavaField()).append("\"\n").append("                  type=\"date\"\n").append("                  placeholder=\"").append(apeGenTableColumn.getColumnComment()).append("\">\n").append("                </el-date-picker>\n").append("            </el-form-item>\n").append("        </el-col>\n");
                }
            }
        }
        builder.append("      </el-row>\n" + "    </el-form>\n" + "    <div slot=\"footer\" class=\"dialog-footer\">\n" + "      <el-button size=\"mini\" type=\"primary\" @click=\"submit\">确 定</el-button>\n" + "      <el-button size=\"mini\" @click=\"handleClose\">取 消</el-button>\n" + "    </div>\n" + "  </el-dialog>\n" + "</div>\n" + "</template>\n" + "\n" + "<script>\n" + "  import {save").append(apeGenTable.getClassName()).append("} from '../../../api/api'\n").append("  export default {\n").append("    data() {\n").append("      return{\n").append("        formLabelWidth: '80px',\n").append("        form: {\n");
        for (ApeGenTableColumn apeGenTableColumn : columnList) {
            if (apeGenTableColumn.getIsInsert() == 1 && (!"id".equals(apeGenTableColumn.getJavaField()) && !"createBy".equals(apeGenTableColumn.getJavaField()) && !"createTime".equals(apeGenTableColumn.getJavaField()) && !"updateBy".equals(apeGenTableColumn.getJavaField()) && !"updateTime".equals(apeGenTableColumn.getJavaField()))) {
                builder.append("          ").append(apeGenTableColumn.getJavaField()).append(": \"\",\n");
            }
        }
        builder.append("        },\n" +
                "        rules: {\n");
        for (ApeGenTableColumn apeGenTableColumn : columnList) {
            if ( apeGenTableColumn.getIsRequired() == 1 && apeGenTableColumn.getIsInsert() == 1 && !"id".equals(apeGenTableColumn.getJavaField()) && !"createBy".equals(apeGenTableColumn.getJavaField()) && !"createTime".equals(apeGenTableColumn.getJavaField()) && !"updateBy".equals(apeGenTableColumn.getJavaField()) && !"updateTime".equals(apeGenTableColumn.getJavaField())) {
                builder.append("          ").append(apeGenTableColumn.getJavaField()).append(": [\n").append("            { required: true, message: '请输入").append(apeGenTableColumn.getColumnComment()).append("', trigger: 'blur' },\n").append("          ],\n");
            }
        }
        builder.append("        },\n" + "      }\n" + "    },\n" + "    props: ['addVisible'],\n" + "    methods: {\n" + "      submit() {\n" + "        this.$refs[\"ruleForm\"].validate((valid) => {\n" + "          if (valid) {\n" + "            save").append(apeGenTable.getClassName()).append("(this.form).then(res => {\n").append("              if(res.code == 1000) {\n").append("                this.$notify.success({\n").append("                  title: '成功',\n").append("                  message: \"保存成功\"\n").append("                });\n").append("                this.handleClose();\n").append("              } else {\n").append("                this.$notify.error({\n").append("                  title: '错误',\n").append("                  message: res.message\n").append("                });\n").append("              }\n").append("            })\n").append("          } else {\n").append("            return false;\n").append("          }\n").append("        });\n").append("      },\n").append("      handleClose() {\n").append("        this.form = {\n");
        for (ApeGenTableColumn apeGenTableColumn : columnList) {
            if (apeGenTableColumn.getIsInsert() == 1 && (!"id".equals(apeGenTableColumn.getJavaField()) && !"createBy".equals(apeGenTableColumn.getJavaField()) && !"createTime".equals(apeGenTableColumn.getJavaField()) && !"updateBy".equals(apeGenTableColumn.getJavaField()) && !"updateTime".equals(apeGenTableColumn.getJavaField()))) {
                builder.append("          ").append(apeGenTableColumn.getJavaField()).append(": \"\",\n");
            }
        }
        builder.append("        },\n" +
                "        this.$emit(\"addFalse\")\n" +
                "      },\n" +
                "     \n" +
                "    },\n" +
                "    created() {\n" +
                "     \n" +
                "    },\n" +
                "    mounted() {\n" +
                "      \n" +
                "    }\n" +
                " }\n" +
                "</script>\n" +
                "\n" +
                "<style lang=scss scoped>\n" +
                "  .el-col {\n" +
                "      display: flex;\n" +
                "      flex-direction: row;\n" +
                "      align-items: center;\n" +
                "      margin-top: 12px\n" +
                "  }\n" +
                "  .search-title {\n" +
                "      font-family: '黑体';\n" +
                "      float: right;\n" +
                "      white-space: nowrap;\n" +
                "      font-size: 14px;\n" +
                "      width: 84px;\n" +
                "      text-align: right;\n" +
                "  }\n" +
                "  .el-tree {\n" +
                "      border: 1px solid #BDC1C9;\n" +
                "  }\n" +
                "</style>");
        return builder.toString();
    }

}
