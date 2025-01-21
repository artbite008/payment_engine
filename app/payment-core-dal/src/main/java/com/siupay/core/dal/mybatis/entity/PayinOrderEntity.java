package com.siupay.core.dal.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "payin_order",autoResultMap = true)
public class PayinOrderEntity {

    @TableId(type = IdType.INPUT)
    private String payinOrderId;

    private String channelCoreId;

    @TableField("accounting_biz_id")
    private String accountingBizId;
    
    private String pccId;

    private String uid;

    private String paymentUserId;

    private String merchantId;

    private String channelId;

    private String orderType;

    private Long payinAmount;

    private String payinCurrency;

    private Long depositFiatAmount;

    private String depositFiatCurrency;

    private BigDecimal depositCryptoAmount;

    private String depositCryptoCurrency;

    private Long feeAmount;

    private String feeCurrency;

    private BigDecimal spreadAmount;

    private String spreadCurrency;

    private String paymentMethod;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private OrderData orderData;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private PaymentConfirm paymentConfirm;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private RiskData riskData;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private AdditionalData additionalData;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private SpreadConfig SpreadConfig;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private ErrorReason errorReason;

    private String status;

    private String nextAction;

    private String creator;

    private Date expireTime;

    private Date createTime;

    private Date updateTime;
}
