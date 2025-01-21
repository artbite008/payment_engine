package com.siupay.utils.notice.convertor;

import com.siupay.common.lib.enums.TradeType;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;

public interface NoticeRequestBuilder {
    /**
     * 支持的交易类型
     *
     * @return
     */
    TradeType getTradeType();

    /**
     * 将订单信息转换为用户通知的消息对象
     *
     * @param payinOrder
     * @return
     */
}
