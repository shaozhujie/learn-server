package com.ape.apeadmin.controller.gen;

import com.alibaba.fastjson2.JSONObject;
import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.utils.HumpUtils;
import com.ape.apecommon.utils.StringUtils;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apegenerator.*;
import com.ape.apesystem.domain.ApeGenTable;
import com.ape.apesystem.domain.ApeGenTableColumn;
import com.ape.apesystem.domain.ApeUser;
import com.ape.apesystem.dto.GenTableColumnEditDto;
import com.ape.apesystem.service.ApeGenTableColumnService;
import com.ape.apesystem.service.ApeGenTableService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 代码生成controller
 * @date 2023/10/10 9:25
 */
@Controller
@ResponseBody
@RequestMapping("genTable")
public class ApeGenTableController{

    @Autowired
    private ApeGenTableService apeGenTableService;
    @Autowired
    private ApeGenTableColumnService apeGenTableColumnService;
    @Autowired
    private GenIndexCode genIndexCode;
    @Autowired
    private GenAddCode genAddCode;
    @Autowired
    private GenUpdateCode genUpdateCode;

    /**
     * 查询
     */
    @Log(name = "查询代码生成列表", type = BusinessType.OTHER)
    @PostMapping("getGenPage")
    public Result getGenPage(@RequestBody ApeGenTable apeGenTable) {
        Page<ApeGenTable> page = new Page<>(apeGenTable.getPageNumber(), apeGenTable.getPageSize());
        QueryWrapper<ApeGenTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(apeGenTable.getTableName()), ApeGenTable::getTableName, apeGenTable.getTableName())
                .like(StringUtils.isNotBlank(apeGenTable.getTableComment()), ApeGenTable::getTableComment, apeGenTable.getTableComment())
                .ge(apeGenTable.getStartTime() != null, ApeGenTable::getCreateTime,apeGenTable.getStartTime())
                .le(apeGenTable.getEndTime() != null,ApeGenTable::getCreateTime,apeGenTable.getEndTime())
                .orderByDesc(ApeGenTable::getCreateTime);
        Page<ApeGenTable> genTablePage = apeGenTableService.page(page, queryWrapper);
        return Result.success(genTablePage);
    }

    /** 根据id获取代码生成 */
    @Log(name = "根据id获取代码生成", type = BusinessType.OTHER)
    @GetMapping("getGenById")
    public Result getGenById(@RequestParam("id")String id) {
        JSONObject gen = new JSONObject();
        ApeGenTable apeGenTable = apeGenTableService.getById(id);
        QueryWrapper<ApeGenTableColumn> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeGenTableColumn::getTableId,id);
        List<ApeGenTableColumn> list = apeGenTableColumnService.list(queryWrapper);
        gen.put("table",apeGenTable);
        gen.put("column",list);
        return Result.success(gen);
    }

    /** 保存代码生成 */
    @Log(name = "保存代码生成", type = BusinessType.INSERT)
    @GetMapping("saveGen")
    @Transactional(rollbackFor = Exception.class)
    public Result saveGen(@RequestParam("tables")String tables) {
        String[] split = tables.split(",");
        ApeUser userInfo = ShiroUtils.getUserInfo();
        for (String table : split) {
            //先保存表
            Map<String, Object> tablesInfo = apeGenTableService.getTablesInfo(table);
            ApeGenTable apeGenTable = new ApeGenTable();
            String idStr = IdWorker.getIdStr();
            apeGenTable.setId(idStr);
            apeGenTable.setTableName(table);
            apeGenTable.setTableComment(tablesInfo.get("tableComment").toString());
            apeGenTable.setGenType(0);
            apeGenTable.setTplCategory("crud");
            apeGenTable.setClassName(HumpUtils.toCamel(table,"_"));
            apeGenTable.setFunctionAuthor(userInfo.getUserName());
            apeGenTable.setPackageName("com.ape.apeadmin");
            apeGenTable.setModuleName("system");
            apeGenTable.setFunctionName(tablesInfo.get("tableComment").toString());
            apeGenTable.setBusinessName(tablesInfo.get("tableComment").toString());
            apeGenTableService.save(apeGenTable);
            //在获取字段保存字段
            List<Map<String,Object>> list = apeGenTableColumnService.getTableField(table);
            for (Map<String,Object> map : list) {
                ApeGenTableColumn apeGenTableColumn = new ApeGenTableColumn();
                apeGenTableColumn.setTableId(idStr);
                apeGenTableColumn.setColumnName(map.get("name").toString());
                apeGenTableColumn.setColumnComment(map.get("comment").toString());
                apeGenTableColumn.setColumnType(map.get("column").toString());
                if ("PRI".equals(map.get("key").toString())) {
                    apeGenTableColumn.setIsPk(1);
                    apeGenTableColumn.setIsQuery(0);
                    apeGenTableColumn.setIsList(0);
                    apeGenTableColumn.setIsEdit(0);
                } else {
                    apeGenTableColumn.setIsPk(0);
                }
                if ("NO".equals(map.get("isNull").toString())) {
                    apeGenTableColumn.setIsRequired(1);
                }
                String type = map.get("type").toString();
                if ("varchar".equals(type) || "text".equals(type) || "longtext".equals(type)) {
                    apeGenTableColumn.setJavaType("String");
                }
                if ("int".equals(type) || "smallint".equals(type)) {
                    apeGenTableColumn.setJavaType("Integer");
                }
                if ("bigint".equals(type)) {
                    apeGenTableColumn.setJavaType("Long");
                }
                if ("decimal".equals(type)) {
                    apeGenTableColumn.setJavaType("BigDecimal");
                }
                if ("float".equals(type)) {
                    apeGenTableColumn.setJavaType("Float");
                }
                if ("double".equals(type)) {
                    apeGenTableColumn.setJavaType("Double");
                }
                if ("tinyint".equals(type)) {
                    apeGenTableColumn.setJavaType("Boolean");
                }
                if ("datetime".equals(type)) {
                    apeGenTableColumn.setJavaType("Date");
                    apeGenTableColumn.setHtmlType("日期控件");
                }
                apeGenTableColumn.setJavaField(HumpUtils.toSmallCamel(map.get("name").toString(),"_"));
                apeGenTableColumnService.save(apeGenTableColumn);
            }
        }
        return Result.success();
    }

    /** 编辑 */
    @Log(name = "编辑代码生成", type = BusinessType.UPDATE)
    @PostMapping("editGen")
    @Transactional(rollbackFor = Exception.class)
    public Result editDict(@RequestBody GenTableColumnEditDto genTableColumnEditDto) {
        //更新表
        apeGenTableService.updateById(genTableColumnEditDto.getApeGenTable());
        //更新字段
        apeGenTableColumnService.updateBatchById(genTableColumnEditDto.getColumns());
        return Result.success();
    }

    /** 删除 */
    @GetMapping("removeGen")
    @Transactional(rollbackFor = Exception.class)
    @Log(name = "删除代码生成", type = BusinessType.DELETE)
    public Result removeGen(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeGenTableService.removeById(id);
                QueryWrapper<ApeGenTableColumn> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(ApeGenTableColumn::getTableId,id);
                apeGenTableColumnService.remove(queryWrapper);
            }
            return Result.success();
        } else {
            return Result.fail("代码生成id不能为空！");
        }
    }

    /** 同步生成表和字段 */
    @GetMapping("syncTableAndColumns")
    @Log(name = "同步生成表和字段", type = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public Result syncTableAndColumns(@RequestParam("id")String id) {
        ApeGenTable apeGenTable = apeGenTableService.getById(id);
        //先保存表
        Map<String, Object> tablesInfo = apeGenTableService.getTablesInfo(apeGenTable.getTableName());
        apeGenTable.setTableComment(tablesInfo.get("tableComment").toString());
        apeGenTable.setFunctionName(tablesInfo.get("tableComment").toString());
        apeGenTable.setBusinessName(tablesInfo.get("tableComment").toString());
        apeGenTableService.updateById(apeGenTable);
        //在获取字段保存字段
        List<Map<String,Object>> list = apeGenTableColumnService.getTableField(apeGenTable.getTableName());
        //删除表字段
        QueryWrapper<ApeGenTableColumn> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeGenTableColumn::getTableId,apeGenTable.getId());
        apeGenTableColumnService.remove(queryWrapper);
        //重新保存表字段
        for (Map<String,Object> map : list) {
            ApeGenTableColumn apeGenTableColumn = new ApeGenTableColumn();
            apeGenTableColumn.setTableId(apeGenTable.getId());
            apeGenTableColumn.setColumnType(map.get("column").toString());
            apeGenTableColumn.setColumnName(map.get("name").toString());
            apeGenTableColumn.setColumnComment(map.get("comment").toString());
            if ("PRI".equals(map.get("key").toString())) {
                apeGenTableColumn.setIsPk(1);
                apeGenTableColumn.setIsQuery(0);
                apeGenTableColumn.setIsList(0);
                apeGenTableColumn.setIsEdit(0);
            } else {
                apeGenTableColumn.setIsPk(0);
            }
            if ("NO".equals(map.get("isNull").toString())) {
                apeGenTableColumn.setIsRequired(1);
            }
            String type = map.get("type").toString();
            if ("varchar".equals(type) || "text".equals(type) || "longtext".equals(type)) {
                apeGenTableColumn.setJavaType("String");
            }
            if ("int".equals(type) || "smallint".equals(type)) {
                apeGenTableColumn.setJavaType("Integer");
            }
            if ("bigint".equals(type)) {
                apeGenTableColumn.setJavaType("Long");
            }
            if ("decimal".equals(type)) {
                apeGenTableColumn.setJavaType("BigDecimal");
            }
            if ("float".equals(type)) {
                apeGenTableColumn.setJavaType("Float");
            }
            if ("double".equals(type)) {
                apeGenTableColumn.setJavaType("Double");
            }
            if ("tinyint".equals(type)) {
                apeGenTableColumn.setJavaType("Boolean");
            }
            if ("datetime".equals(type)) {
                apeGenTableColumn.setJavaType("Date");
                apeGenTableColumn.setHtmlType("日期控件");
            }
            apeGenTableColumn.setJavaField(HumpUtils.toSmallCamel(map.get("name").toString(), "_"));
            apeGenTableColumnService.save(apeGenTableColumn);
        }
        return Result.success();
    }

    /** 预览 */
    @GetMapping("preview")
    @Log(name = "预览", type = BusinessType.OTHER)
    @Transactional(rollbackFor = Exception.class)
    public Result preview(@RequestParam("id")String id) {
        //获取表
        ApeGenTable apeGenTable = apeGenTableService.getById(id);
        QueryWrapper<ApeGenTableColumn> queryWrapper = new QueryWrapper<>();
        //获取字段
        queryWrapper.lambda().eq(ApeGenTableColumn::getTableId,id);
        List<ApeGenTableColumn> apeGenTableColumns = apeGenTableColumnService.list(queryWrapper);
        JSONObject jsonObject = new JSONObject();
        //生成domain
        String domain = GenDomainCode.genDomain(apeGenTable, apeGenTableColumns);
        jsonObject.put("domain",domain);
        jsonObject.put("domainName",apeGenTable.getClassName());
        //生成mapper接口
        String mapper = GenMapperCode.genMapper(apeGenTable, apeGenTableColumns);
        jsonObject.put("mapper",mapper);
        jsonObject.put("mapperName",apeGenTable.getClassName() + "Mapper");
        //生成service接口
        String service = GenServiceCode.genService(apeGenTable, apeGenTableColumns);
        jsonObject.put("service",service);
        jsonObject.put("serviceName",apeGenTable.getClassName() + "Service");
        //生成service实现类
        String serviceImpl = GenServiceCode.genServiceImpl(apeGenTable, apeGenTableColumns);
        jsonObject.put("serviceImpl",serviceImpl);
        jsonObject.put("serviceImplName",apeGenTable.getClassName() + "ServiceImpl");
        //生成controller
        String controller = GenControllerCode.genController(apeGenTable, apeGenTableColumns);
        jsonObject.put("controller",controller);
        jsonObject.put("controllerName",apeGenTable.getClassName() + "Controller");
        //生产mapper.xml
        String mapperXml = GenMapperCode.genMapperXml(apeGenTable, apeGenTableColumns);
        jsonObject.put("mapperXml",mapperXml);
        jsonObject.put("mapperXmlName",apeGenTable.getClassName() + "Mapper.xml");
        //生成api
        String api = GenApiCode.genApi(apeGenTable, apeGenTableColumns);
        jsonObject.put("api",api);
        //生成index
        String index = genIndexCode.genIndex(apeGenTable, apeGenTableColumns);
        jsonObject.put("index",index);
        //生成add
        String add = genAddCode.genAdd(apeGenTable, apeGenTableColumns);
        jsonObject.put("add",add);
        //生成update
        String update = genUpdateCode.genUpdate(apeGenTable, apeGenTableColumns);
        jsonObject.put("update",update);
        return Result.success(jsonObject);
    }

    /** 获取数据库表 */
    @PostMapping("getTables")
    @Log(name = "获取数据库表", type = BusinessType.OTHER)
    public Result getTables(@RequestBody ApeGenTable apeGenTable) {
        Page<Map<String,Object>> page = apeGenTableService.getTables(apeGenTable);
        return Result.success(page);
    }

}
