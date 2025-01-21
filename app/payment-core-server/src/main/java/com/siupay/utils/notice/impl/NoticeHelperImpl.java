package com.siupay.utils.notice.impl;

import com.google.common.collect.Maps;
import com.siupay.common.api.exception.PaymentError;
import com.siupay.common.lib.enums.TradeType;
import com.siupay.common.lib.json.JsonUtils;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import com.siupay.utils.notice.NoticeHelper;
import com.siupay.utils.notice.convertor.NoticeRequestBuilder;
import io.vavr.control.Either;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @program: Uther.chen
 * @description: 消息helper
 * @create: 2022-04-14 14:51
 **/
@Service
@Slf4j
public class NoticeHelperImpl implements NoticeHelper, ApplicationContextAware {

    @Setter
    private Map<TradeType, NoticeRequestBuilder> noticeRequestBuilderMap;


    public void sendPayinOrderNotify(PayinOrderEntity payinOrder) {
    }

    private NoticeRequestBuilder getConverter(TradeType tradeType) {
        return noticeRequestBuilderMap.get(tradeType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, NoticeRequestBuilder> beanMap = applicationContext.getBeansOfType(NoticeRequestBuilder.class);
        if (CollectionUtils.isEmpty(beanMap)) {
            log.warn("不存在 NoticeRequestBuilder 的实现类");
            return;
        }
        noticeRequestBuilderMap = Maps.newHashMapWithExpectedSize(beanMap.size());
        for (NoticeRequestBuilder noticeRequestBuilder : beanMap.values()) {
            TradeType tradeType = noticeRequestBuilder.getTradeType();
            noticeRequestBuilderMap.put(tradeType, noticeRequestBuilder);
        }
    }
}
