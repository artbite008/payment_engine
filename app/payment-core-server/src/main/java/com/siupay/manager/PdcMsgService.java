package com.siupay.manager;

import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import com.siupay.core.dal.mybatis.repositry.PayinOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * payment data center 收集数据
 * @author joexia
 * @description
 * @date 2022/4/18 16:32
 */
@Service
@Slf4j
public class PdcMsgService {


    @Autowired
    private PayinOrderRepository payinOrderRepository;

    public void collect(String orderId){
        try {
        }catch (Exception e){
            log.info("PdcMsgService collect msg error:{}",e.getMessage());
        }
    }

    public void collect(PayinOrderEntity payinOrderEntity){
        try {
            log.info("PdcMsgService collect msg begin orderId:{}",payinOrderEntity.getPayinOrderId());
        }catch (Exception e){
            log.info("PdcMsgService collect msg error:{}",e.getMessage());
        }
    }
}
