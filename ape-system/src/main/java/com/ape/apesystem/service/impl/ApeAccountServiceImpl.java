package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeAccount;
import com.ape.apesystem.mapper.ApeAccountMapper;
import com.ape.apesystem.service.ApeAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 公告service实现类
 * @date 2023/9/21 8:47
 */
@Service
public class ApeAccountServiceImpl extends ServiceImpl<ApeAccountMapper, ApeAccount> implements ApeAccountService {
}
