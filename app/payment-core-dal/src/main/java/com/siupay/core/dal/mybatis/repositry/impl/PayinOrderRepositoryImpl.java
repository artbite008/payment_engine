package com.siupay.core.dal.mybatis.repositry.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import com.siupay.core.dal.mybatis.mapper.PayinOrderMapper;
import com.siupay.core.dal.mybatis.repositry.PayinOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayinOrderRepositoryImpl extends ServiceImpl<PayinOrderMapper, PayinOrderEntity> implements PayinOrderRepository {


	@Autowired
	private PayinOrderMapper payinOrderMapper;

	@Override
	public Long getMerchantCount(String mid,String startDate, String endDate){
		return payinOrderMapper.countPayinAmount(mid,startDate,endDate);
	}
}
