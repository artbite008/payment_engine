package com.siupay.enums;

import lombok.Getter;

@Getter
public enum PayinOrderPayEventsEnum {

    //支付事件
    PENDING("pending", "订单3D预授权进行中"),
    PROCESSING("processing","订单处理中,2D"),



    SUCCEEDED("succeeded", "成功"),
    FAILED("failed", "订单已失败"),
    EXPIRED("expired", "订单已过期"),




    DEPOSIT_SUCCESS("deposit_success","上分成功"),
    //DEPOSIT_PROCESSING("processing","上分处理中"),
    DEPOSIT_FAIL("deposit_fail","上分失败"),

    //快捷买币事件
    CONVERT_SUCCESS("convert_success","快捷买币成功"),
    CONVERT_FAIL("convert_fail","快捷买币失败"),
    //PCC订单撮合事件
    PCC_ORDER_CRYPTO_PROCESSING("pcc_order_crypto_processing","PCC订单撮合中"),
    PCC_ORDER_CRYPTO_SUCCESS("pcc_order_cryptoing","PCC订单撮合成功"),
    PCC_ORDER_CRYPTO_FAIL("pcc_order_cryptoing","PCC订单撮合失败"),
    ORDER_CANCEL("order_cancel","订单取消预授权"),

    //上分或快捷买币全部完成
    //ALL_SUCCESS("all_success","全部成功"),
    ;

    private String code;
    private String desc;

    private PayinOrderPayEventsEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public PayinOrderPayEventsEnum getByCode(){
        return null;
    }
}
