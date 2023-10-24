package com.ape.apeadmin.controller.gen;

import com.ape.apesystem.service.ApeGenTableColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 代码生成字段controller
 * @date 2023/10/10 9:29
 */
@Controller
@ResponseBody
@RequestMapping("genColumn")
public class ApeGenTableColumnController {

    @Autowired
    private ApeGenTableColumnService apeGenTableColumnService;



}
