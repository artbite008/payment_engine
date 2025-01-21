package com.siupay.core.dal.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PayinOrderMapper extends BaseMapper<PayinOrderEntity> {

	@Select("select sum(payin_amount) from payin_order where" +
			" status = 'SUCCEEDED' and merchant_id=#{merchantId} and "+
			"create_time >= #{startDate} and create_time<=#{endDate}")
	Long countPayinAmount(@Param("merchantId")String merchantId, @Param("startDate")String startDate, @Param("endDate")String endDate);
}
