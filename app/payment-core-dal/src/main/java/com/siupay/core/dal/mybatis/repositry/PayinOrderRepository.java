package com.siupay.core.dal.mybatis.repositry;

import com.baomidou.mybatisplus.extension.service.IService;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface PayinOrderRepository extends IService<PayinOrderEntity> {


	Long getMerchantCount(String mid,String startDate,String endDate);
}
