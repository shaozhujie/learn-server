package com.ape.apeadmin.controller.dict;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apecommon.utils.StringUtils;
import com.ape.apesystem.domain.ApeDictData;
import com.ape.apesystem.service.ApeDictDataService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 字典controller
 * @date 2023/10/9 14:32
 */
@Controller
@ResponseBody
@RequestMapping("dict")
public class ApeDictDataController {

    @Autowired
    private ApeDictDataService apeDictDataService;

    /**
     * 查询
     */
    @Log(name = "查询字典列表", type = BusinessType.OTHER)
    @PostMapping("getDictPage")
    public Result getDictPage(@RequestBody ApeDictData apeDictData) {
        Page<ApeDictData> page = new Page<>(apeDictData.getPageNumber(), apeDictData.getPageSize());
        QueryWrapper<ApeDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(apeDictData.getDictCode()), ApeDictData::getDictCode, apeDictData.getDictCode())
                .like(StringUtils.isNotBlank(apeDictData.getDictLabel()), ApeDictData::getDictLabel, apeDictData.getDictLabel())
                .eq(apeDictData.getStatus() != null, ApeDictData::getStatus, apeDictData.getStatus())
                .orderByDesc(ApeDictData::getDictCode).orderByDesc(ApeDictData::getCreateTime);
        Page<ApeDictData> dictDataPage = apeDictDataService.page(page, queryWrapper);
        return Result.success(dictDataPage);
    }

    /**
     * 查询
     */
    @Log(name = "查询全部字典类型", type = BusinessType.OTHER)
    @GetMapping("getDictTypeList")
    public Result getDictTypeList() {
        QueryWrapper<ApeDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().groupBy(ApeDictData::getDictCode);
        List<ApeDictData> dataList = apeDictDataService.list(queryWrapper);
        return Result.success(dataList);
    }

    /** 根据id获取字典 */
    @Log(name = "根据id获取字典", type = BusinessType.OTHER)
    @GetMapping("getDictById")
    public Result getDictById(@RequestParam("id")String id) {
        ApeDictData dictData = apeDictDataService.getById(id);
        return Result.success(dictData);
    }

    /** 保存字典 */
    @Log(name = "保存字典", type = BusinessType.INSERT)
    @PostMapping("saveDict")
    public Result saveDict(@RequestBody ApeDictData apeDictData) {
        boolean save = apeDictDataService.save(apeDictData);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑 */
    @Log(name = "编辑字典", type = BusinessType.UPDATE)
    @PostMapping("editDict")
    public Result editDict(@RequestBody ApeDictData apeDictData) {
        boolean save = apeDictDataService.updateById(apeDictData);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除字典 */
    @GetMapping("removeDict")
    @Log(name = "删除字典", type = BusinessType.DELETE)
    public Result removeDict(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeDictDataService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("字典id不能为空！");
        }
    }

    /** 根据编码获取字典数据 */
    @GetMapping("getDictByCode")
    @Log(name = "根据编码获取字典数据", type = BusinessType.OTHER)
    public Result getDictByCode(@RequestParam("code")String code) {
        QueryWrapper<ApeDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeDictData::getDictCode,code).orderByAsc(ApeDictData::getDictSort);
        List<ApeDictData> dictDataList = apeDictDataService.list(queryWrapper);
        return Result.success(dictDataList);
    }

    /** 根据编码获取字典数据和标签获取值 */
    @GetMapping("getDictByCodeAndLabel")
    @Log(name = "根据编码获取字典数据和标签获取值", type = BusinessType.OTHER)
    public Result getDictByCodeAndLabel(@RequestParam("code")String code,@RequestParam("label")String label) {
        QueryWrapper<ApeDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeDictData::getDictCode,code)
                .eq(ApeDictData::getDictLabel,label).last("limit 1");
        ApeDictData dictData = apeDictDataService.getOne(queryWrapper);
        return Result.success(dictData);
    }

}
