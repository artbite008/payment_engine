package com.siupay.enums;

public enum PayinOrderStatusEnum {

    //订单创建
    CREATED(false),
    //2D支付中
    PROCESSING(false),
    //支付完成,充值,买币等场景
    PAYIN_COMPLETED(false),
    //3D跳转
    CUSTOMER_ACTION(false),
    //上分成功
    DEPOSIT_COMPLETED(false),
    //上分失败
    DEPOSIT_FAILED(false),
    //买币成功
    CONVERT_COMPLETED(false),
    //买币失败
    CONVERT_FAILED(false),
    //PCC订单撮合中
    PCC_ORDER_CRYPTO_PROCESSING(false),
    //PCC订单撮合失败
    PCC_ORDER_CRYPTO_FAIL(false),
    //成功终态
    SUCCEEDED(true),
    //失败终态
    FAILED(true),
    //超时终态
    EXPIRED(true),
    //取消终态
    CANCELLED(true),
    ;
    private boolean finalState;

    PayinOrderStatusEnum(boolean finalState){
        this.finalState = finalState;
    }

    public boolean isFinalState(){
        return this.finalState;
    }
}
