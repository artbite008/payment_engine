package com.siupay.flow.order.handler;

import com.siupay.common.api.dto.PaymentAmount;
import com.siupay.common.lib.enums.FundDirection;
import com.siupay.common.lib.enums.PaymentMethod;
import com.siupay.common.lib.enums.TransactionType;
import com.siupay.common.lib.idgenerator.IDGeneratorService;
import com.siupay.common.lib.utils.BigDecimalUtils;
import com.siupay.core.dal.mybatis.entity.AdditionalData;
import com.siupay.core.dal.mybatis.entity.OrderData;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import com.siupay.core.dal.mybatis.entity.PaymentConfirm;
import com.siupay.core.dal.mybatis.entity.RiskData;
import com.siupay.core.dal.mybatis.repositry.PayinOrderRepository;
import com.siupay.core.dto.CreatePayinOrderRequest;
import com.siupay.core.dto.CreatePayinOrderResponse;
import com.siupay.enums.PayinOrderStatusEnum;
import com.siupay.enums.RiskResultEnum;
import com.siupay.flow.order.PayinCreateOrderContext;
import com.siupay.utils.BaseFacade;
import com.siupay.starter.chaincontext.ChainContextConstants;
import com.siupay.starter.chaincontext.ChainRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

import static com.siupay.constant.Constant.PIR_;

@Component
@Slf4j
public class PayinOrderSaveAndReturn extends BaseFacade implements Command {

    @Autowired
    private IDGeneratorService idService;

    @Autowired
    private PayinOrderRepository payinOrderRepository;

    @Override
    public boolean execute(Context context) {

        PayinCreateOrderContext payinCreateOrderContext = (PayinCreateOrderContext) context;
        PayinOrderEntity payinOrder = buildPayinOrder(payinCreateOrderContext.getCreatePayinOrderRequest(),payinCreateOrderContext);
        payinOrderRepository.save(payinOrder);
        buildResponse(payinCreateOrderContext,payinOrder);
        return false;
    }

    private PayinOrderEntity buildPayinOrder(CreatePayinOrderRequest request, PayinCreateOrderContext context){
        PayinOrderEntity payinOrder = new PayinOrderEntity();
        BeanUtils.copyProperties(request,payinOrder);
        //pir_xxxx
        String payinOrderId = PIR_ + idService.generateId(TransactionType.DEFAULT, PaymentMethod.valueOf(request.getConfirm().getPaymentMethod().toUpperCase()), FundDirection.PAYIN);
        payinOrder.setPayinOrderId(payinOrderId);
        payinOrder.setUid(getUserId());
        payinOrder.setMerchantId(getMerchantId());
        payinOrder.setChannelId(context.getChannel().getChannelId());
        long amount = BigDecimalUtils.currencyParseLocal(request.getPaymentAmount().getAmount().toString(),request.getPaymentAmount().getCurrency()).longValue();
        payinOrder.setPayinAmount(amount);
        payinOrder.setPayinCurrency(request.getPaymentAmount().getCurrency());
        PaymentConfirm paymentConfirm = new PaymentConfirm();
        BeanUtils.copyProperties(request.getConfirm(),paymentConfirm);
        payinOrder.setPaymentConfirm(paymentConfirm);
        payinOrder.setPaymentMethod(request.getConfirm().getPaymentMethod().toUpperCase());
        if (Objects.nonNull(context.getFeeAmount())) {
            long feeAmount = BigDecimalUtils.currencyParseLocal(context.getFeeAmount().toString(), context.getFeeCurrency()).longValue();
            payinOrder.setFeeAmount(feeAmount);
            payinOrder.setFeeCurrency(context.getFeeCurrency());
            BigDecimal depositFiatAmount = request.getPaymentAmount().getAmount().subtract(context.getFeeAmount());
            long depositFiatAmountLong = BigDecimalUtils.currencyParseLocal(depositFiatAmount.toString(),request.getPaymentAmount().getCurrency()).longValue();
            payinOrder.setDepositFiatAmount(depositFiatAmountLong);
            payinOrder.setDepositFiatCurrency(request.getPaymentAmount().getCurrency());
        }
        if (Objects.nonNull(context.getDepositCryptoAmount())){
            payinOrder.setDepositCryptoAmount(context.getDepositCryptoAmount());
            payinOrder.setDepositCryptoCurrency(context.getDepositCryptoCurrency());
        }
        OrderData orderData = new OrderData();
        BeanUtils.copyProperties(request.getOrder(),orderData);
        payinOrder.setOrderData(orderData);
        payinOrder.setStatus(PayinOrderStatusEnum.CREATED.name());
        RiskData riskData = new RiskData();
        //todo 这里根据风控结果来判断是否3D，如果风控关了根据卡号来
        if (Objects.nonNull(context.getRiskResult())){
            riskData.setNeed3DS(context.getRiskResult().equals(RiskResultEnum.ACCEPT_3DS));
        }else {
            riskData.setNeed3DS(false);
        }
        payinOrder.setRiskData(riskData);
        AdditionalData additionalData = new AdditionalData();
        BeanUtils.copyProperties(request.getAdditionalData(), additionalData);
        additionalData.setSourceIp(ChainRequestContext.getCurrentContext().getString(ChainContextConstants.IP));
        additionalData.setLang(ChainRequestContext.getCurrentContext().getString(ChainContextConstants.LANG));
        payinOrder.setAdditionalData(additionalData);

        //PA-2530添加价差、报价保存
        payinOrder.setSpreadAmount(context.getSpreadAmount());
        payinOrder.setSpreadCurrency(context.getSpreadCurrency());
        payinOrder.setSpreadConfig(context.getSpreadConfig());

        return payinOrder;
    }

    private void buildResponse(PayinCreateOrderContext payinCreateOrderContext, PayinOrderEntity payinOrder){
        CreatePayinOrderResponse response = payinCreateOrderContext.getCreatePayinOrderResponse();

        response.setOrderId(payinOrder.getPayinOrderId());
        response.setStatus(payinOrder.getStatus());
        PaymentAmount paymentAmount = new PaymentAmount();
        paymentAmount.setAmount(BigDecimalUtils.currencyParse(payinOrder.getPayinAmount(),payinOrder.getPayinCurrency()));
        paymentAmount.setCurrency(payinOrder.getPayinCurrency());
        response.setPaymentAmount(paymentAmount);

        payinCreateOrderContext.setCreatePayinOrderResponse(response);
    }
}
