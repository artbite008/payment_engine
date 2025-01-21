package com.siupay.flow.async.handler;

import com.siupay.accounting.api.MerchantTransactionApi;
import com.siupay.accounting.api.request.MerchantTransactionRequest;
import com.siupay.accounting.api.request.TransactionType;
import com.siupay.accounting.api.response.MerchantTransactionResponse;
import com.siupay.common.api.dto.PaymentAmount;
import com.siupay.common.api.exception.ErrorCode;
import com.siupay.common.api.exception.PaymentError;
import com.siupay.common.api.exception.PaymentException;
import com.siupay.common.lib.enums.TradeType;
import com.siupay.common.lib.json.JsonUtils;
import com.siupay.common.lib.utils.BigDecimalUtils;
import com.siupay.config.state.PayinStateMachineService;
import com.siupay.constant.DynamicConstants;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import com.siupay.core.dal.mybatis.repositry.PayinOrderRepository;
import com.siupay.enums.PayinOrderPayEventsEnum;
import com.siupay.flow.async.PayinAsyncContext;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class PayinAsyncToAccounting implements Command {

    public static final String BIZ_FROM_DEPOSIT = "DEPOSIT";

    @Autowired
    private PayinOrderRepository payinOrderRepository;
    @Autowired
    private PayinStateMachineService payinStateMachine;
    @Autowired
    private DynamicConstants dynamicConstants;
    @Autowired
    private MerchantTransactionApi merchantTransactionApi;

    private final String SUCCEEDED = "SUCCEEDED";
    private final String FAILED = "FAILED";

    @Override
    public boolean execute(Context context) throws Exception {
        PayinAsyncContext payinAsyncContext = (PayinAsyncContext) context;
        PayinOrderEntity payinOrder = payinOrderRepository.getById(payinAsyncContext.getPayinOrderId());
        if (isNeedAccounting(payinAsyncContext, payinOrder)) {
            if (payinOrder.getOrderType().equalsIgnoreCase(TradeType.BUY.name())) {
                fastBuyAccounting(payinAsyncContext, payinOrder);
            }
            // 不需要 上分
            if (payinOrder.getOrderType().equalsIgnoreCase(TradeType.RECHARGE.name())) {
                //todo
                receiptAccounting(payinAsyncContext, payinOrder);
//                payinStateMachine.sentEvent(payinOrder, PayinOrderPayEventsEnum.DEPOSIT_SUCCESS);
            }
            payinOrderRepository.updateById(payinOrder);
        }
        return false;
    }

    private boolean isNeedAccounting(PayinAsyncContext payinAsyncContext, PayinOrderEntity payinOrder) {
        return StringUtils.equalsIgnoreCase(payinOrder.getOrderData().getCryptoCurrency(), dynamicConstants.getFunds())
                || payinOrder.getOrderType().equalsIgnoreCase(TradeType.RECHARGE.name());
    }

    private void fastBuyAccounting(Context context, PayinOrderEntity payinOrder) {

    }

    private void receiptAccounting(Context context, PayinOrderEntity payinOrder) {

        MerchantTransactionRequest amountRequest = new MerchantTransactionRequest();
        amountRequest.setChannelId(payinOrder.getChannelId());
        amountRequest.setChannelUserId(payinOrder.getUid());
        amountRequest.setMerchantId(payinOrder.getMerchantId());
        amountRequest.setTransactionType(TransactionType.PAYIN.name());
        amountRequest.setTransactionOrderId(payinOrder.getPayinOrderId());
        if (StringUtils.isNotEmpty(payinOrder.getPayinCurrency())){
            PaymentAmount paymentAmount = new PaymentAmount();
            paymentAmount.setAmount(BigDecimalUtils.currencyParse(payinOrder.getPayinAmount(),payinOrder.getPayinCurrency()));
            paymentAmount.setCurrency(payinOrder.getPayinCurrency());
            amountRequest.setRequestAmount(paymentAmount);
        }
        log.info("request accounting payin is {}",JsonUtils.toJson(amountRequest));
        Either<PaymentError, MerchantTransactionResponse> either = merchantTransactionApi.accounting(amountRequest);
        log.info("response accounting payin is {}",JsonUtils.toJson(either));
        if (either.isLeft()){
            payinStateMachine.sentEvent(payinOrder, PayinOrderPayEventsEnum.DEPOSIT_FAIL);
            throw new PaymentException(ErrorCode.SERVER_ERROR,"accounting payin error");
        }

        MerchantTransactionRequest feeRequest = new MerchantTransactionRequest();
        feeRequest.setChannelId(payinOrder.getChannelId());
        feeRequest.setChannelUserId(payinOrder.getUid());
        feeRequest.setMerchantId(payinOrder.getMerchantId());
        feeRequest.setTransactionType(TransactionType.FEE.name());
        feeRequest.setTransactionOrderId(payinOrder.getPayinOrderId());
        if (StringUtils.isNotEmpty(payinOrder.getFeeCurrency())){
            PaymentAmount feeAmount = new PaymentAmount();
            feeAmount.setAmount(BigDecimalUtils.currencyParse(payinOrder.getFeeAmount(),payinOrder.getFeeCurrency()));
            feeAmount.setCurrency(payinOrder.getFeeCurrency());
            feeRequest.setRequestAmount(feeAmount);
        }
        log.info("request accounting fee is {}",JsonUtils.toJson(feeRequest));
        Either<PaymentError, MerchantTransactionResponse> feeEither = merchantTransactionApi.accounting(feeRequest);
        log.info("response accounting fee is {}",JsonUtils.toJson(feeEither));
        if (feeEither.isLeft()){
            payinStateMachine.sentEvent(payinOrder, PayinOrderPayEventsEnum.DEPOSIT_FAIL);
            throw new PaymentException(ErrorCode.SERVER_ERROR,"accounting fee error");
        }
        payinOrder.setAccountingBizId(either.get().getJournalId());
        payinStateMachine.sentEvent(payinOrder, PayinOrderPayEventsEnum.DEPOSIT_SUCCESS);
    }
}
