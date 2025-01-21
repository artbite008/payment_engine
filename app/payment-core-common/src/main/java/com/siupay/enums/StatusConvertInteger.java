package com.siupay.enums;

public class StatusConvertInteger {

    public static Integer payinStatusConvert(PayinOrderStatusEnum status) {
        switch (status) {
            case SUCCEEDED:
                return 1;
            case FAILED:
            case EXPIRED:
            case CANCELLED:
                return 2;
            default:
                return 0;
        }
    }

}
