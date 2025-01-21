package com.siupay.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtils {
    public static String toJson(Object obj){
        try {
           return JSON.toJSONString(obj);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return "";
    }
}
