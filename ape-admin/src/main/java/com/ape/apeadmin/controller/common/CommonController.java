package com.ape.apeadmin.controller.common;

import com.ape.apecommon.domain.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: TODO
 * @date 2023/8/28 11:35
 */
@Controller
@ResponseBody
@RequestMapping("common")
public class CommonController {

    /**
    * @description: 错误转发地址
    * @param: code
    	msg
    * @return:
    * @author shaozhujie
    * @date: 2023/9/14 15:05
    */
    @GetMapping("/error/{code}/{msg}")
    public Result error (@PathVariable("code")Integer code, @PathVariable("msg") String msg){
        return Result.alert(code,msg);
    }

    /**
     * @description: 上传图片
     * @param: file
     * @return:
     * @author shaozhujie
     * @date: 2023/10/13 10:44
     */
    @PostMapping("uploadImg")
    public Result uploadImg(@RequestParam("file") MultipartFile img) {
        if(img.isEmpty()){
            return Result.fail("上传的图片不能为空!");
        }
        String coverType = img.getOriginalFilename().substring(img.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        if ("jpeg".equals(coverType)  || "gif".equals(coverType) || "png".equals(coverType) || "bmp".equals(coverType)  || "jpg".equals(coverType)) {
            //文件名=当前时间到毫秒+原来的文件名
            int index = img.getOriginalFilename().lastIndexOf(".");
            String substring = img.getOriginalFilename().substring(index);
            String fileName = System.currentTimeMillis() + substring;
            //文件路径
            String filePath = System.getProperty("user.dir")+System.getProperty("file.separator")+"img";
            //如果文件路径不存在，新增该路径
            File file1 = new File(filePath);
            if(!file1.exists()){
                boolean mkdir = file1.mkdir();
            }
            //实际的文件地址
            File dest = new File(filePath + System.getProperty("file.separator") + fileName);
            //存储到数据库里的相对文件地址
            String storeImgPath = "/img/"+fileName;
            try {
                img.transferTo(dest);
                return Result.success(storeImgPath);
            } catch (IOException e) {
                return Result.fail("上传失败");
            }
        } else {
            return Result.fail("请选择正确的图片格式");
        }
    }

    /**
     * @description: 上传视频
     * @param: file
     * @return:
     * @author shaozhujie
     * @date: 2023/10/13 10:44
     */
    @PostMapping("uploadVideo")
    public Result uploadVideo(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()){
            return Result.fail("上传的视频不能为空!");
        }
        //文件名=当前时间到毫秒+原来的文件名
        int index = file.getOriginalFilename().lastIndexOf(".");
        String substring = file.getOriginalFilename().substring(index);
        String fileName = System.currentTimeMillis() + substring;
        //文件路径
        String filePath = System.getProperty("user.dir")+System.getProperty("file.separator")+"video";
        //如果文件路径不存在，新增该路径
        File file1 = new File(filePath);
        if(!file1.exists()){
            boolean mkdir = file1.mkdir();
        }
        //实际的文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        //存储到数据库里的相对文件地址
        String storeVideoPath = "/video/"+fileName;
        try {
            file.transferTo(dest);
            Result success = Result.success(storeVideoPath);
            success.setData(fileName);
            return success;
        } catch (IOException e) {
            return Result.fail("上传失败");
        }
    }

    /**
    * @description: 上传文件
    * @param: file
    * @return:
    * @author shaozhujie
    * @date: 2023/10/13 10:44
    */
    @PostMapping("uploadFile")
    public Result uploadFile(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()){
            return Result.fail("上传的文件不能为空!");
        }
        //文件名=当前时间到毫秒+原来的文件名
        int index = file.getOriginalFilename().lastIndexOf(".");
        String substring = file.getOriginalFilename().substring(index);
        String fileName = System.currentTimeMillis() + substring;
        //文件路径
        String filePath = System.getProperty("user.dir")+System.getProperty("file.separator")+"file";
        //如果文件路径不存在，新增该路径
        File file1 = new File(filePath);
        if(!file1.exists()){
            boolean mkdir = file1.mkdir();
        }
        //实际的文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        //存储到数据库里的相对文件地址
        String storeFilePath = "/file/"+fileName;
        try {
            file.transferTo(dest);
            Result success = Result.success(storeFilePath);
            success.setData(fileName);
            return success;
        } catch (IOException e) {
            return Result.fail("上传失败");
        }
    }

}
