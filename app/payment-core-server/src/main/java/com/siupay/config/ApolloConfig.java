package com.siupay.config;//package com.siupay.config;
//import com.alibaba.fastjson.JSON;
//import com.ctrip.framework.apollo.model.ConfigChange;
//import com.ctrip.framework.apollo.model.ConfigChangeEvent;
//import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.context.scope.refresh.RefreshScope;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ApolloConfig {
//    private final static Logger logger = LoggerFactory.getLogger("BIZ-INFO");
//
//    @Autowired
//    private RefreshScope refreshScope;
//
//    @ApolloConfigChangeListener
//    public void changeHandler(ConfigChangeEvent changeEvent) {
//        for (String key : changeEvent.changedKeys()) {
//            ConfigChange change = changeEvent.getChange(key);
//            logger.info("配置发生变化 {}", change.toString());
//            refresh(key);
//        }
//    }
//
//    /**
//     * 刷新配置
//     *
//     * @param key
//     */
//    public void refresh(String key) {
//        refreshScope.refresh(key);
//    }
//}
