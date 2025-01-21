package com.siupay.job;//package com.siupay.job;
//
//import com.baomidou.mybatisplus.core.conditions.Wrapper;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.ctrip.framework.apollo.ConfigService;
//import com.dangdang.ddframe.job.api.ShardingContext;
//import com.dangdang.ddframe.job.api.simple.SimpleJob;
//import com.siupay.common.lib.enums.ChannelEnum;
//import com.siupay.constant.Constant;
//import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
//import com.siupay.core.dal.mybatis.repositry.PayinOrderRepository;
//import com.siupay.enums.PayinOrderStatusEnum;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
///**
// * 订单自动过期任务
// * @author joexia
// * @description
// * @date 2022/4/14 15:22
// */
//@Slf4j
//@Component
//public class PayinOrderAutoExpireJob implements SimpleJob {
//
//    @Autowired
//    private PayinOrderRepository payinOrderRepository;
//
//    /**
//     * 默认时间窗口30分钟
//     */
//    public static final int DEFAULT_TIME_WINDOW=30*60;
//
//    @Override
//    public void execute(ShardingContext shardingContext) {
//
//        int timeWindow =  ConfigService.getAppConfig().getIntProperty(Constant.APOLLO_KEY_PAYIN_ORDER_SCAN_TIME_WINDOW, DEFAULT_TIME_WINDOW)*1000;
//
//        PayinOrderEntity updateEntity=new PayinOrderEntity();
//        updateEntity.setStatus(PayinOrderStatusEnum.EXPIRED.name());
//        updateEntity.setUpdateTime(new Date());
//
//        Wrapper<PayinOrderEntity> queryWrapper = Wrappers.<PayinOrderEntity>lambdaQuery()
//                .eq(PayinOrderEntity::getStatus, PayinOrderStatusEnum.CREATED.name())
//                .notIn(PayinOrderEntity::getChannelId, ChannelEnum.ADVCASH.getChannelId(),
//                        ChannelEnum.SEPA_TRANSACTIVE.getChannelId(),
//                        ChannelEnum.CAPITUAL.getChannelId())
//                .le(PayinOrderEntity::getCreateTime, new Date(new Date().getTime() - timeWindow));
//
//        boolean update = payinOrderRepository.update(updateEntity, queryWrapper);
//        log.info("PayinOrderAutoExpireJob execute result :{}",update);
//
//    }
//}
