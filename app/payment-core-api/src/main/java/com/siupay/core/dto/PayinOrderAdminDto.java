package com.siupay.core.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.siupay.common.api.dto.PaymentAmount;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PayinOrderAdminDto {

    private String payinOrderId;

    private String channelCoreId;

    private String accountingBizId;

    private String pccId;

    private String uid;

    private String paymentUserId;

    private String merchantId;

    private String channelId;

    private String orderType;

    private PaymentAmount payinAmount;

    private String payinCurrency;

    private PaymentAmount depositFiatAmount;

    private String depositFiatCurrency;

    private PaymentAmount depositCryptoAmount;

    private String depositCryptoCurrency;

    private PaymentAmount feeAmount;

    private String feeCurrency;

    private String paymentMethod;

    private String status;

    private String nextAction;

    private String creator;

    private Date expireTime;

    private Date createTime;

    private Date updateTime;

    private String orderId;

    private String paymentInstrumentId;

    private static final ObjectMapper MAPPER;

    public String getPayinOrderId() {
        return payinOrderId;
    }

    public void setPayinOrderId(String payinOrderId) {
        this.payinOrderId = payinOrderId;
    }

    public String getChannelCoreId() {
        return channelCoreId;
    }

    public void setChannelCoreId(String channelCoreId) {
        this.channelCoreId = channelCoreId;
    }

    public String getAccountingBizId() {
        return accountingBizId;
    }

    public void setAccountingBizId(String accountingBizId) {
        this.accountingBizId = accountingBizId;
    }

    public String getPccId() {
        return pccId;
    }

    public void setPccId(String pccId) {
        this.pccId = pccId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPaymentUserId() {
        return paymentUserId;
    }

    public void setPaymentUserId(String paymentUserId) {
        this.paymentUserId = paymentUserId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public PaymentAmount getPayinAmount() {
        return payinAmount;
    }

    public void setPayinAmount(PaymentAmount payinAmount) {
        this.payinAmount = payinAmount;
    }

    public String getPayinCurrency() {
        return payinCurrency;
    }

    public void setPayinCurrency(String payinCurrency) {
        this.payinCurrency = payinCurrency;
    }

    public PaymentAmount getDepositFiatAmount() {
        return depositFiatAmount;
    }

    public void setDepositFiatAmount(PaymentAmount depositFiatAmount) {
        this.depositFiatAmount = depositFiatAmount;
    }

    public String getDepositFiatCurrency() {
        return depositFiatCurrency;
    }

    public void setDepositFiatCurrency(String depositFiatCurrency) {
        this.depositFiatCurrency = depositFiatCurrency;
    }

    public PaymentAmount getDepositCryptoAmount() {
        return depositCryptoAmount;
    }

    public void setDepositCryptoAmount(PaymentAmount depositCryptoAmount) {
        this.depositCryptoAmount = depositCryptoAmount;
    }

    public String getDepositCryptoCurrency() {
        return depositCryptoCurrency;
    }

    public void setDepositCryptoCurrency(String depositCryptoCurrency) {
        this.depositCryptoCurrency = depositCryptoCurrency;
    }

    public PaymentAmount getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(PaymentAmount feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getFeeCurrency() {
        return feeCurrency;
    }

    public void setFeeCurrency(String feeCurrency) {
        this.feeCurrency = feeCurrency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentInstrumentId() {
        return paymentInstrumentId;
    }

    public void setPaymentInstrumentId(String paymentInstrumentId) {
        this.paymentInstrumentId = paymentInstrumentId;
    }

    static {
        final DateFormat iso8601 = new ISO8601DateFormat();
        iso8601.setTimeZone(TimeZone.getTimeZone("UTC"));
        MAPPER = new ObjectMapper().registerModule(new Jdk8Module()).setDateFormat(iso8601);
    }

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception ioe) {
            return ioe.getLocalizedMessage();
        }
    }
}
