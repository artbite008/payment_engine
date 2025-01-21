package com.siupay.core.dto;

import lombok.Data;

@Data
public class AdditionalDataDto {
    /**
     * 用户语言
     */
    private String lang;
    private String clientFrom;
    /**
     *  信用卡风控专用
     */
    private String bizSessionId;
    /**
     * 钱包专用
     */
    private String reference;
    /**
     * 来源IP
     */
    private String sourceIp;

    /**
     * App端传版本号，Web端传空
     */
    private String appVersion;

    /**
     * App端传手机型号比如iphone12，iphone11等，Web端传空
     */
    private String deviceVersion;

    /**
     * 新的设备指纹ID
     */
    private String fingerId;

    /**
     * 新的设备指纹主键
     */
    private String fingerPid;

    /**
     * 老的设备指纹ID
     */
    private String deviceId;
}
