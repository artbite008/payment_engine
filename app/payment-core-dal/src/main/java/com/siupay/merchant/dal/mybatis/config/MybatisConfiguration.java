package com.siupay.merchant.dal.mybatis.config;

import com.siupay.common.lib.date.AlterTimeZoneInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;

/**
 * 
 * @Date: 2022/8/20 22:14
 */
@MapperScan("com.siupay.merchant.dal.mybatis.mapper")
public class MybatisConfiguration {

	@Bean
	public AlterTimeZoneInterceptor alterTimeZoneInterceptor() {
		return new AlterTimeZoneInterceptor();
	}
}