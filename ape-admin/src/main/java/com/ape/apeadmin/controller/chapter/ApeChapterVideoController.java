package com.ape.apeadmin.controller.chapter;

import com.ape.apecommon.annotation.Log;
import com.ape.apecommon.domain.Result;
import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.ResultCode;
import com.ape.apeframework.utils.ShiroUtils;
import com.ape.apesystem.domain.ApeChapterVideo;
import com.ape.apesystem.domain.ApeUser;
import com.ape.apesystem.service.ApeChapterVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 章节视频是否观看controller
 * @date 2023/11/23 10:39
 */
@Controller
@ResponseBody
@RequestMapping("chapterVideo")
public class ApeChapterVideoController {

    @Autowired
    private ApeChapterVideoService apeChapterVideoService;

    /** 分页获取章节视频是否观看 */
    @Log(name = "分页获取章节视频是否观看", type = BusinessType.OTHER)
    @PostMapping("getApeChapterVideoPage")
    public Result getApeChapterVideoPage(@RequestBody ApeChapterVideo apeChapterVideo) {
        Page<ApeChapterVideo> page = new Page<>(apeChapterVideo.getPageNumber(),apeChapterVideo.getPageSize());
        QueryWrapper<ApeChapterVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(apeChapterVideo.getChapterId()),ApeChapterVideo::getChapterId,apeChapterVideo.getChapterId())
                .eq(StringUtils.isNotBlank(apeChapterVideo.getUserId()),ApeChapterVideo::getUserId,apeChapterVideo.getUserId());
        Page<ApeChapterVideo> apeChapterVideoPage = apeChapterVideoService.page(page, queryWrapper);
        return Result.success(apeChapterVideoPage);
    }

    /** 根据id获取章节视频是否观看 */
    @Log(name = "根据id获取章节视频是否观看", type = BusinessType.OTHER)
    @GetMapping("getApeChapterVideoById")
    public Result getApeChapterVideoById(@RequestParam("id")String id) {
        ApeChapterVideo apeChapterVideo = apeChapterVideoService.getById(id);
        return Result.success(apeChapterVideo);
    }

    /** 保存章节视频是否观看 */
    @Log(name = "保存章节视频是否观看", type = BusinessType.INSERT)
    @PostMapping("saveApeChapterVideo")
    public Result saveApeChapterVideo(@RequestBody ApeChapterVideo apeChapterVideo) {
        ApeUser userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<ApeChapterVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeChapterVideo::getChapterId,apeChapterVideo.getChapterId())
                        .eq(ApeChapterVideo::getUserId,userInfo.getId());
        int count = apeChapterVideoService.count(queryWrapper);
        if (count <= 0) {
            apeChapterVideo.setUserId(userInfo.getId());
            boolean save = apeChapterVideoService.save(apeChapterVideo);
            if (save) {

            } else {
                return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
            }
        }
        return Result.success();
    }

    /** 编辑章节视频是否观看 */
    @Log(name = "编辑章节视频是否观看", type = BusinessType.UPDATE)
    @PostMapping("editApeChapterVideo")
    public Result editApeChapterVideo(@RequestBody ApeChapterVideo apeChapterVideo) {
        boolean save = apeChapterVideoService.updateById(apeChapterVideo);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除章节视频是否观看 */
    @GetMapping("removeApeChapterVideo")
    @Log(name = "删除章节视频是否观看", type = BusinessType.DELETE)
    public Result removeApeChapterVideo(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                apeChapterVideoService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("章节视频是否观看id不能为空！");
        }
    }

}