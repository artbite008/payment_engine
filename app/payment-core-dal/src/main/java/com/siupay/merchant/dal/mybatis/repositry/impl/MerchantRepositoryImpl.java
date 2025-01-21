package com.siupay.merchant.dal.mybatis.repositry.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siupay.merchant.dal.mybatis.entity.MerchantEntity;
import com.siupay.merchant.dal.mybatis.mapper.MerchantMapper;
import com.siupay.merchant.dal.mybatis.repositry.MerchantRepository;
import org.springframework.stereotype.Service;

/**
 * @Author: ce.liu
 * @Date: 2022/8/20 22:32
 */
@Service
public class MerchantRepositoryImpl extends ServiceImpl<MerchantMapper, MerchantEntity> implements MerchantRepository {
}
