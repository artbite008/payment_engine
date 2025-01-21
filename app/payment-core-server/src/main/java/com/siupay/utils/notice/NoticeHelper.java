package com.siupay.utils.notice;

import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;

/**
 * @program: uther.chen
 * @description: 消息helper
 * @create: 2022-04-14 14:51
 */
public interface NoticeHelper {
    /**
     * 发送用户payin order的消息
     *
     * @param payinOrder
     */
    void sendPayinOrderNotify(PayinOrderEntity payinOrder);

}
