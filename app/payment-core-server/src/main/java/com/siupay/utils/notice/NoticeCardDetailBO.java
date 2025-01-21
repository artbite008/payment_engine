package com.siupay.utils.notice;


import lombok.Data;

/**
 * payment-core
 *
 * @author uther.chen
 * @description: 消息的card信息包包装
 */
@Data
public class NoticeCardDetailBO {

    /**
     * 渠道类型
     */
    private String channelType;

    /**
     * 发卡行名称
     */
    private String issuingBank;

    /**
     * 前6
     */
    private String prefix6;

    /**
     * 后四
     */
    private String postfix4;
}
