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
 * @description: index代码生成类
 * @date 2023/10/14 10:13
 */
@Component
public class GenIndexCode {

    @Autowired
    private ApeDictDataService apeDictDataService;

    public String genIndex(ApeGenTable apeGenTable, List<ApeGenTableColumn> columnList) {
        StringBuilder builder = new StringBuilder();
        String name = apeGenTable.getTableName().substring(apeGenTable.getTableName().lastIndexOf("_"));
        String toCamel = HumpUtils.toCamel(apeGenTable.getTableName(), "_");
        String toSmallCamel = HumpUtils.toSmallCamel(apeGenTable.getTableName(), "_");
        builder.append("<template>\n" + "  <div class=\"").append(name).append("\">\n").append("      <div class=\"search-table\">\n").append("        <div class=\"search\">\n").append("            <el-row :gutter=\"10\" style=\"padding:10px\">\n");
        for (ApeGenTableColumn apeGenTableColumn : columnList) {
            String s = HumpUtils.toCamel(apeGenTableColumn.getJavaField(), "_");
            String sm = HumpUtils.toSmallCamel(apeGenTableColumn.getJavaField(), "_");
            if (apeGenTableColumn.getIsQuery() == 1) {
                if ("下拉框".equals(apeGenTableColumn.getHtmlType())) {
                    builder.append("                <el-col :xs=\"24\" :sm=\"12\" :md=\"8\" :lg=\"8\" :xl=\"8\">\n" + "                    <span class=\"search-title\">").append(apeGenTableColumn.getColumnComment()).append(":</span>\n").append("                    <el-select clearable style=\"margin-top:10px\" size=\"mini\" v-model=\"search.").append(apeGenTableColumn.getJavaType()).append("\" placeholder=\"请选择\">\n");
                    if (StringUtils.isNotBlank(apeGenTableColumn.getDictType())) {
                        QueryWrapper<ApeDictData> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(ApeDictData::getDictCode,apeGenTableColumn.getDictType());
                        List<ApeDictData> dictData = apeDictDataService.list(queryWrapper);
                        for (ApeDictData dict : dictData) {
                            builder.append("<el-option\n" + "                        label=\"").append(dict.getDictLabel()).append("\"\n").append("                        value=\"").append(dict.getDictValue()).append("\">\n").append("                      </el-option>\n");
                        }
                    }
                    builder.append("</el-select>\n" +
                            "                </el-col>\n");
                } else if ("Date".equals(apeGenTableColumn.getJavaType())) {
                    if ("BETWEEN".equals(apeGenTableColumn.getQueryType())) {
                        builder.append("                <el-col :xs=\"24\" :sm=\"12\" :md=\"8\" :lg=\"8\" :xl=\"8\">\n" + "                    <span class=\"search-title\">").append(apeGenTableColumn.getColumnComment()).append(":</span>\n").append("                    <el-date-picker size=\"mini\"\n").append("                      style=\"margin-top:10px\"\n").append("                      v-model=\"").append(sm).append("\"\n").append("                      type=\"daterange\"\n").append("                      range-separator=\"至\"\n").append("                      format=\"yyyy-MM-dd\" \n").append("                      value-format=\"yyyy-MM-dd\" \n").append("                      start-placeholder=\"开始日期\"\n").append("                      end-placeholder=\"结束日期\">\n").append("                    </el-date-picker>\n").append("                </el-col>\n");
                    } else {
                        builder.append("                <el-col :xs=\"24\" :sm=\"12\" :md=\"8\" :lg=\"8\" :xl=\"8\">\n" + "                    <span class=\"search-title\">登陆时间:</span>\n" + "                    <el-date-picker size=\"mini\"\n" + "                      style=\"margin-top:10px\"\n" + "                      v-model=\"search.").append(apeGenTableColumn.getJavaField()).append("\"\n").append("                      format=\"yyyy-MM-dd\" \n").append("                      value-format=\"yyyy-MM-dd\" \n").append("                    </el-date-picker>\n").append("                </el-col>\n");
                    }
                } else {
                    builder.append("                <el-col :xs=\"24\" :sm=\"12\" :md=\"8\" :lg=\"8\" :xl=\"8\">\n" + "                    <span class=\"search-title\">").append(apeGenTableColumn.getColumnComment()).append(":</span>\n").append("                    <el-input\n").append("                        style=\"margin-top:10px\"\n").append("                        size=\"mini\"\n").append("                        placeholder=\"请输入").append(apeGenTableColumn.getColumnComment()).append("\"\n").append("                        v-model=\"search.").append(apeGenTableColumn.getJavaField()).append("\">\n").append("                    </el-input>\n").append("                </el-col>\n");
                }
            }
        }
        builder.append("                <el-col :xs=\"24\" :sm=\"12\" :md=\"8\" :lg=\"8\" :xl=\"8\">\n" +
                "                    <el-button style=\"margin-top:10px\" size=\"mini\" icon=\"el-icon-search\" type=\"primary\" @click=\"searchPage\">查询</el-button>\n" +
                "                    <el-button style=\"margin-top:10px\" size=\"mini\" icon=\"el-icon-refresh\" @click=\"refresh\">重置</el-button>\n" +
                "                </el-col>\n");
        builder.append("            </el-row>\n" +
                "        </div>\n");
        builder.append("        <div class=\"table\">\n" +
                "            <el-row style=\"padding-top:10px;margin-left:10px\">\n" +
                "                <el-button type=\"primary\" size=\"mini\" icon=\"el-icon-plus\" plain @click=\"add\">新增</el-button>\n" +
                "                <el-button type=\"success\" :disabled=\"update.length != 1 ?true:false\" size=\"mini\" icon=\"el-icon-edit\" plain @click=\"updateDataBtn\">修改</el-button>\n" +
                "                <el-button type=\"danger\" :disabled=\"update.length <= 0 ?true:false\" size=\"mini\" icon=\"el-icon-delete\" plain @click=\"deleteDataBtn\">删除</el-button>\n" +
                "                <!-- <el-button type=\"warning\" size=\"mini\" icon=\"el-icon-download\" plain>导出</el-button> -->\n" +
                "            </el-row>\n" +
                "            <el-table\n" +
                "            v-loading=\"loading\"\n" +
                "            :data=\"tableData\"\n" +
                "            :header-cell-style=\"{\n" +
                "              'color': '#4A2B90',\n" +
                "              'background-color': '#ECE9F4',\n" +
                "            }\"\n" +
                "            :row-style=\"{\n" +
                "              'color': '#888897',\n" +
                "              'font-size': '15px',\n" +
                "              'font-family':'黑体',\n" +
                "              'white-space': 'nowrap'\n" +
                "            }\"\n" +
                "            @selection-change=\"handleSelectionChange\"\n" +
                "            stripe\n" +
                "            style=\"width: 100%\">\n" +
                "            <el-table-column\n" +
                "              type=\"selection\"\n" +
                "              width=\"55\">\n" +
                "            </el-table-column>\n");
        for (ApeGenTableColumn apeGenTableColumn : columnList) {
            if (apeGenTableColumn.getIsList() == 1) {
                builder.append("            <el-table-column\n" + "              prop=\"").append(apeGenTableColumn.getJavaField()).append("\"\n").append("              label=\"").append(apeGenTableColumn.getColumnComment()).append("\"\n").append("              width=\"180\"\n").append("              >\n").append("            </el-table-column>\n");
            }
        }
        builder.append("            <el-table-column\n" +
                "              label=\"操作\"\n" +
                "              width=\"180\"\n" +
                "              >\n" +
                "              <template slot-scope=\"scope\">\n" +
                "                <el-button size=\"mini\" type=\"success\" @click=\"updateData(scope.row.id)\">修改</el-button>\n" +
                "                <el-popconfirm\n" +
                "                  style=\"margin-left:5px\"\n" +
                "                  confirm-button-text='确认'\n" +
                "                  cancel-button-text='取消'\n" +
                "                  icon=\"el-icon-info\"\n" +
                "                  icon-color=\"red\"\n" +
                "                  title=\"确认删除选中的数据？\"\n" +
                "                  @confirm=\"deleteDate(scope.row.id)\"\n" +
                "                >\n" +
                "                  <el-button size=\"mini\" slot=\"reference\" type=\"danger\">删除</el-button>\n" +
                "                </el-popconfirm>\n" +
                "              </template>\n" +
                "            </el-table-column>\n" +
                "          </el-table>\n" +
                "          <el-pagination\n" +
                "            background\n" +
                "            layout=\"total, sizes, prev, pager, next, jumper\"\n" +
                "            :page-sizes=\"[10, 20, 50, 100]\"\n" +
                "            :page-size=\"search.pageSize\"\n" +
                "            :current-page=\"search.pageNumber\"\n" +
                "            @size-change=\"handleSizeChange\"\n" +
                "            @current-change=\"handleCurrentChange\"\n" +
                "            :total=\"total\">\n" +
                "          </el-pagination>\n" +
                "          </div>\n" +
                "        </div>\n");
        builder.append("      <add @addFalse=\"addFalse\" :addVisible = \"addVisible\"></add>\n" +
                "      <update @updateFalse=\"updateFalse\" :updateId = \"updateId\" :updateVisible = \"updateVisible\"></update>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</template>\n\n");
        builder.append("<script>\n" + "  import {get").append(apeGenTable.getClassName()).append("Page,remove").append(apeGenTable.getClassName()).append("} from '../../../api/api' \n").append("  import add from '../../../components/system/").append(toSmallCamel).append("/add").append(toCamel).append("'\n").append("  import update from '../../../components/system/").append(toSmallCamel).append("/update").append(toCamel).append("'\n").append("  export default {\n").append("    data() {\n").append("      return{\n").append("        loading: true,\n").append("        update: [],\n").append("        remove: [],\n").append("        updateId: \"\",\n").append("        addVisible: false,\n").append("        updateVisible: false,\n").append("        search: {\n");
        for (ApeGenTableColumn apeGenTableColumn : columnList) {
            String s = HumpUtils.toCamel(apeGenTableColumn.getJavaField(), "_");
            String sm = HumpUtils.toSmallCamel(apeGenTableColumn.getJavaField(), "_");
            if (apeGenTableColumn.getIsQuery() == 1) {
                if ("BETWEEN".equals(apeGenTableColumn.getQueryType()) && "Date".equals(apeGenTableColumn.getJavaType())) {
                    builder.append("            ").append(sm).append("StartTime: \"\",\n").append("            ").append(sm).append("EndTime: \"\",\n");
                } else {
                    builder.append("            ").append(apeGenTableColumn.getJavaField()).append(": \"\",\n");
                }
            }
        }
        builder.append("            pageNumber: 1,\n" +
                "            pageSize:10\n        },\n");
        for (ApeGenTableColumn apeGenTableColumn : columnList) {
            String sm = HumpUtils.toSmallCamel(apeGenTableColumn.getJavaField(), "_");
            if ("Date".equals(apeGenTableColumn.getJavaType())) {
                if ("BETWEEN".equals(apeGenTableColumn.getQueryType())) {
                    builder.append("        ").append(sm).append(": [],\n");
                }
            }
        }
        builder.append("        total: 0,\n" +
                "        tableData: []\n" +
                "      }\n" +
                "    },\n");
        builder.append("    components: {\n" + "      add").append(",\n").append("      update").append("\n").append("    },\n");
        builder.append("    methods: {\n");

        builder.append("        searchPage() {\n" +
                "            this.search.pageNumber = 1\n" +
                "            this.query()\n" +
                "        },\n");
        builder.append("      query() {\n");
        for (ApeGenTableColumn apeGenTableColumn : columnList) {
            String sm = HumpUtils.toSmallCamel(apeGenTableColumn.getJavaField(), "_");
            if ("Date".equals(apeGenTableColumn.getJavaType())) {
                if ("BETWEEN".equals(apeGenTableColumn.getQueryType())) {
                    builder.append("        if (this.").append(sm).append(".length > 0) {\n").append("          this.search.").append(sm).append("StartTime = this.").append(sm).append("[0] + \" 00:00:00\"\n").append("          this.search.").append(sm).append("EndTime = this.").append(sm).append("[1] + \" 23:59:59\"\n").append("        }\n");
                }
            }
        }
        builder.append("        get").append(apeGenTable.getClassName()).append("Page(this.search).then(res => {\n").append("          if(res.code == 1000) {\n").append("            this.tableData = res.data.records\n").append("            this.total = res.data.total\n").append("            this.loading = false\n").append("          } else {\n").append("            this.$notify.error({\n").append("              title: '错误',\n").append("              message: res.message\n").append("            });\n").append("          }\n").append("        })\n").append("      },\n");
        builder.append("      refresh() {\n");
        for (ApeGenTableColumn apeGenTableColumn : columnList) {
            String sm = HumpUtils.toSmallCamel(apeGenTableColumn.getJavaField(), "_");
            if (apeGenTableColumn.getIsQuery() == 1) {
                if ("Date".equals(apeGenTableColumn.getJavaType())) {
                    if ("BETWEEN".equals(apeGenTableColumn.getQueryType())) {
                        builder.append("        this.search.").append(sm).append("StartTime").append(" = \"\"\n");
                        builder.append("        this.search.").append(sm).append("EndTime").append(" = \"\"\n");
                        builder.append("        this.").append(sm).append(" = []\n");
                    }
                } else {
                    builder.append("        this.search.").append(apeGenTableColumn.getJavaField()).append(" = \"\"\n");
                }
            }
        }
        builder.append("        this.query()\n" +
                "      },\n");
        builder.append("      handleCurrentChange(val) {\n" +
                "        this.search.pageNumber = val\n" +
                "        this.query()\n" +
                "      },\n" +
                "      handleSizeChange(val) {\n" +
                "        this.search.pageSize = val\n" +
                "        this.query()\n" +
                "      },\n" +
                "      handleSelectionChange(val) {\n" +
                "        this.update = []\n" +
                "        this.remove = []\n" +
                "        for (let i = 0;i < val.length;i++) {\n" +
                "          var item = val[i]\n" +
                "          this.update.push(item.id)\n" +
                "          this.remove.push(item.id)\n" +
                "        }\n" +
                "      },\n" +
                "      add() {\n" +
                "        this.addVisible = true\n" +
                "      },\n" +
                "      addFalse() {\n" +
                "        this.addVisible = false\n" +
                "        this.query()\n" +
                "      },\n" +
                "      updateFalse() {\n" +
                "        this.updateId = ''\n" +
                "        this.updateVisible = false\n" +
                "        this.query()\n" +
                "      },\n" +
                "      updateData(id) {\n" +
                "        this.updateId = id\n" +
                "        this.updateVisible = true\n" +
                "      },\n" +
                "      updateDataBtn() {\n" +
                "        this.updateData(this.update[0])\n" +
                "      },\n" +
                "      deleteDataBtn() {\n" +
                "        this.$confirm('确定删除选中的'+ this.remove.length +'条数据?', '提示', {\n" +
                "          confirmButtonText: '确定',\n" +
                "          cancelButtonText: '取消',\n" +
                "          type: 'warning'\n" +
                "        }).then(() => {\n" +
                "          this.deleteDate(this.remove.join(\",\"))\n" +
                "        }).catch(() => {\n" +
                "                 \n" +
                "        });\n" +
                "      },\n");
        builder.append("      deleteDate(ids) {\n" + "        remove").append(apeGenTable.getClassName()).append("({ids:ids}).then(res => {\n").append("            if(res.code == 1000) {\n").append("              this.$message({\n").append("                type: 'success',\n").append("                message: '删除成功!'\n").append("              });\n").append("              this.query()\n").append("            } else {\n").append("              this.$notify.error({\n").append("                title: '错误',\n").append("                message: res.message\n").append("              });\n").append("            }\n").append("          })\n").append("      },\n").append("    },\n").append("    mounted() {\n").append("      this.query()\n").append("    }\n").append(" }\n").append("</script>\n");
        builder.append("<style lang=scss scoped>\n" +
                "  .search-table {\n" +
                "      width: 100%;\n" +
                "  }\n" +
                "  .search {\n" +
                "      background: #ffffff;\n" +
                "      border-radius: 7px;\n" +
                "      box-shadow: 0 2px 12px 0 rgba(0,0,0,.1)\n" +
                "  }\n" +
                "  .table {\n" +
                "      background: #ffffff;\n" +
                "      border-radius: 7px;\n" +
                "      box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);\n" +
                "      margin-top: 10px\n" +
                "  }\n" +
                "  .el-col {\n" +
                "      display: flex;\n" +
                "      flex-direction: row;\n" +
                "      align-items: center;\n" +
                "  }\n" +
                "  .search-title {\n" +
                "      font-family: '黑体';\n" +
                "      float: right;\n" +
                "      white-space: nowrap;\n" +
                "      font-size: 14px;\n" +
                "      margin-top:10px;\n" +
                "      width: 63px;\n" +
                "      text-align: right;\n" +
                "  }\n" +
                "  .el-table {\n" +
                "      padding: 10px;\n" +
                "  }\n" +
                "  .el-dialog__header {\n" +
                "    border-bottom: 1px solid #F4F8F9 !important;\n" +
                "  }\n" +
                "  .el-dialog {\n" +
                "    border-radius: 10px!important;\n" +
                "  }\n" +
                "</style>");
        return builder.toString();
    }
}
