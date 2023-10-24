package com.ape.apeadmin.controller.param;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apesystem.domain.ApeParam;
import com.ape.apesystem.service.ApeParamService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 参数controller
 * @date 2023/9/20 16:44
 */
@Controller
@ResponseBody
@RequestMapping("param")
public class ApeParamController {

    @Autowired
    private ApeParamService apeParamService;

    /** 分页获取参数列表 */
    @Log(name = "分页获取参数列表", type = BusinessType.OTHER)
    @PostMapping("getParamPage")
    public Result getParamPage(@RequestBody ApeParam apeParam) {
        Page<ApeParam> page = new Page<>(apeParam.getPageNumber(),apeParam.getPageSize());
        QueryWrapper<ApeParam> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(apeParam.getParamName()),ApeParam::getParamName,apeParam.getParamName())
                .like(StringUtils.isNotBlank(apeParam.getParamKey()),ApeParam::getParamKey,apeParam.getParamKey())
                .eq(apeParam.getWithin() != null,ApeParam::getWithin,apeParam.getWithin());
        Page<ApeParam> apeParamPage = apeParamService.page(page, queryWrapper);
        return Result.success(apeParamPage);
    }

    /** 根据id获取参数 */
    @Log(name = "根据id获取参数", type = BusinessType.OTHER)
    @GetMapping("getById")
    public Result getById(@RequestParam("id")String id) {
        ApeParam apeParam = apeParamService.getById(id);
        return Result.success(apeParam);
    }

    /** 保存参数 */
    @Log(name = "保存参数", type = BusinessType.INSERT)
    @PostMapping("saveParam")
    public Result saveParam(@RequestBody ApeParam apeParam) {
        QueryWrapper<ApeParam> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeParam::getParamKey,apeParam.getParamKey());
        int count = apeParamService.count(queryWrapper);
        if (count > 0) {
            return Result.fail("参数键已存在！");
        }
        boolean save = apeParamService.save(apeParam);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 修改参数 */
    @Log(name = "修改参数", type = BusinessType.UPDATE)
    @PostMapping("editParam")
    public Result editParam(@RequestBody ApeParam apeParam) {
        ApeParam param = apeParamService.getById(apeParam.getId());
        if (!param.getParamKey().equals(apeParam.getParamKey())) {
            QueryWrapper<ApeParam> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(ApeParam::getParamKey,apeParam.getParamKey());
            int count = apeParamService.count(queryWrapper);
            if (count > 0) {
                return Result.fail("参数键已存在！");
            }
        }
        boolean update = apeParamService.updateById(apeParam);
        if (update) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除参数 */
    @Log(name = "删除参数", type = BusinessType.DELETE)
    @Transactional(rollbackFor = Exception.class)
    @GetMapping("removeParam")
    public Result removeParam(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeParamService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("参数id不能为空！");
        }
    }
}
