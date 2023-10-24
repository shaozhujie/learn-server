package com.ape.apeadmin.controller.account;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apesystem.domain.ApeAccount;
import com.ape.apesystem.service.ApeAccountService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 公告controller
 * @date 2023/9/21 8:48
 */
@Controller
@ResponseBody
@RequestMapping("account")
public class ApeAccountController {

    @Autowired
    private ApeAccountService apeAccountService;

    /** 分页查询公告 */
    @Log(name = "分页查询公告", type = BusinessType.OTHER)
    @PostMapping("getAccountPage")
    public Result getAccountPage(@RequestBody ApeAccount apeAccount) {
        Page<ApeAccount> page = new Page<>(apeAccount.getPageNumber(),apeAccount.getPageSize());
        QueryWrapper<ApeAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(apeAccount.getTitle()),ApeAccount::getTitle,apeAccount.getTitle())
                .like(StringUtils.isNotBlank(apeAccount.getUpdateBy()),ApeAccount::getUpdateBy,apeAccount.getUpdateBy())
                .eq(apeAccount.getType() != null,ApeAccount::getType,apeAccount.getType());
        Page<ApeAccount> accountPage = apeAccountService.page(page, queryWrapper);
        return Result.success(accountPage);
    }

    /** 根据id查询公告 */
    @GetMapping("getById")
    @Log(name = "根据id查询公告", type = BusinessType.OTHER)
    public Result getById(@RequestParam("id") String id) {
        ApeAccount apeAccount = apeAccountService.getById(id);
        return Result.success(apeAccount);
    }

    /** 保存 */
    @PostMapping("saveAccount")
    @Log(name = "保存公告", type = BusinessType.INSERT)
    public Result saveAccount(@RequestBody ApeAccount apeAccount) {
        boolean save = apeAccountService.save(apeAccount);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑 */
    @PostMapping("editAccount")
    @Log(name = "编辑公告", type = BusinessType.UPDATE)
    public Result editDept(@RequestBody ApeAccount apeAccount){
        boolean edit = apeAccountService.updateById(apeAccount);
        if (edit) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除 */
    @GetMapping("removeAccount")
    @Log(name = "删除公告", type = BusinessType.DELETE)
    public Result removeDept(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeAccountService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("公告id不能为空！");
        }
    }

}
