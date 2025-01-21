package com.siupay.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siupay.channel.core.bo.PayInOrderHistorySyncEventBo;
import com.siupay.channel.core.dto.record.PayinResultEventBo;
import com.siupay.common.api.dto.PaymentAmount;
import com.siupay.common.api.enums.PaymentSystem;
import com.siupay.common.api.exception.ErrorCode;
import com.siupay.common.api.exception.PaymentError;
import com.siupay.common.api.exception.PaymentException;
import com.siupay.common.lib.event.IEvent;
import com.siupay.common.lib.idgenerator.IDGeneratorService;
import com.siupay.common.lib.json.JsonUtils;
import com.siupay.common.lib.utils.BigDecimalUtils;
import com.siupay.constant.Constant;
import com.siupay.constant.RedissonKeyConstants;
import com.siupay.flow.async.PayinAsyncContext;
import com.siupay.flow.async.PayinAsyncFlow;
import com.siupay.flow.confirm.PayinOrderConfirmContext;
import com.siupay.flow.confirm.PayinOrderConfirmFlow;
import com.siupay.flow.order.PayinCreateOrderContext;
import com.siupay.flow.order.PayinCreateOrderFlow;
import com.siupay.core.common.dto.Paging;
import com.siupay.core.dal.mybatis.entity.AdditionalData;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import com.siupay.core.dal.mybatis.entity.PaymentConfirm;
import com.siupay.core.dal.mybatis.repositry.PayinOrderRepository;
import com.siupay.enums.PayinOrderPayEventsEnum;
import com.siupay.enums.PayinOrderStatusEnum;
import com.siupay.enums.RedisKeys;
import com.siupay.service.PaymentService;
import com.siupay.utils.BaseFacade;
import com.siupay.core.dto.AdminPayinFilterDto;
import com.siupay.core.dto.AdminQueryHistoryRequest;
import com.siupay.core.dto.CreatePayinOrderRequest;
import com.siupay.core.dto.CreatePayinOrderResponse;
import com.siupay.core.dto.PayinOrderAdminDto;
import com.siupay.core.dto.PayinOrderConfirmRequest;
import com.siupay.core.dto.PayinOrderConfirmResponse;
import com.siupay.core.dto.PayinOrderDto;
import com.siupay.core.dto.PayinOrderQueryHistoryRequest;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentServiceImpl extends BaseFacade implements PaymentService {

    @Autowired
    private PayinCreateOrderFlow payinCreateOrderFlow;

    @Autowired
    private PayinOrderConfirmFlow payinOrderConfirmFlow;

    @Autowired
    private PayinAsyncFlow asyncFlow;

    @Autowired
    private PayinOrderRepository payinOrderRepository;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IDGeneratorService idService;

    @Override
    public Either<PaymentError, CreatePayinOrderResponse> createPayinOrder(CreatePayinOrderRequest request) {

        PayinCreateOrderContext payinCreateOrderContext = new PayinCreateOrderContext();
        payinCreateOrderContext.setCreatePayinOrderRequest(request);
        payinCreateOrderContext.setCreatePayinOrderResponse(new CreatePayinOrderResponse());
        try {
            payinCreateOrderFlow.execute(payinCreateOrderContext);
        }catch (PaymentException paymentException){
            PaymentError paymentError = paymentException.toPaymentError();
            paymentError.setSystemId(PaymentSystem.PAYMENT_CORE.getSystemId());
            log.error("create payin order error {}", JsonUtils.toJson(paymentError));
            return Either.left(paymentError);
        }catch (Exception e){
            log.error("create payin order error ",e);
            PaymentError paymentError = new PaymentException(ErrorCode.UNKNOW, PaymentSystem.PAYMENT_CORE).toPaymentError();
            return Either.left(paymentError);
        }
        return Either.right(payinCreateOrderContext.getCreatePayinOrderResponse());
    }


    @Override
    public Either<PaymentError, PayinOrderConfirmResponse> confirmPayinOrder(PayinOrderConfirmRequest request) {
        PayinOrderConfirmContext context = new PayinOrderConfirmContext();
        context.setRequest(request);
        context.setResponse(new PayinOrderConfirmResponse());

        RLock lock = redissonClient.getLock(RedissonKeyConstants.LOCK_PAYIN_ORDER_CONFIRM.concat(request.getOrderId()));
        try {
            if (lock.tryLock(15, TimeUnit.SECONDS)) {
                payinOrderConfirmFlow.execute(context);
            } else {
                throw new PaymentException(ErrorCode.UNKNOW, "订单处理锁获取失败", PaymentSystem.PAYMENT_CORE);
            }
        } catch (PaymentException paymentException) {
            PaymentError paymentError = paymentException.toPaymentError();
            paymentError.setSystemId(PaymentSystem.PAYMENT_CORE.getSystemId());
            log.error("confirm payin order error {}", JsonUtils.toJson(paymentError));
            return Either.left(paymentError);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            PaymentError paymentError =
                    new PaymentException(ErrorCode.UNKNOW, PaymentSystem.PAYMENT_CORE).toPaymentError();
            return Either.left(paymentError);
        } catch (Exception e) {
            log.error("confirm payin order error ", e);
            PaymentError paymentError =
                    new PaymentException(ErrorCode.UNKNOW, PaymentSystem.PAYMENT_CORE).toPaymentError();
            return Either.left(paymentError);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return Either.right(context.getResponse());
    }

    @Override
    public void payinAsyncProcess(IEvent<PayinResultEventBo> msg) throws Exception {
        PayinResultEventBo content = msg.getContent();
        RLock lock = redissonClient.getLock(RedissonKeyConstants.LOCK_PAYIN_ORDER_ASYNC_PROCESS.concat(content.getRequestId()));
        try {
            PayinAsyncContext context = new PayinAsyncContext();
            context.setPayinCreateResponse(content);
            if (lock.tryLock(15, TimeUnit.SECONDS)) {
                asyncFlow.execute(context);
            } else {
                throw new PaymentException(ErrorCode.UNKNOW, "订单处理锁获取失败", PaymentSystem.PAYMENT_CORE);
            }
        }catch (PaymentException e){
            PaymentError paymentError = e.toPaymentError();
            paymentError.setSystemId(PaymentSystem.PAYMENT_CORE.getSystemId());
            log.error("confirm payin order payment error {}",JsonUtils.toJson(paymentError));
            throw e;
        }catch (Exception e){
            log.error("confirm payin order error ",e);
            throw e;
        }finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public Either<PaymentError, PayinOrderDto> payinOrderQuery(String orderId){
        PayinOrderEntity payinOrder = payinOrderRepository.getById(orderId);
        if (Objects.isNull(payinOrder)){
            throw new PaymentException(ErrorCode.RECORD_NOT_EXIST);
        }
        PayinOrderDto payinOrderDto = new PayinOrderDto();
        payinOrderEntityConvert(payinOrder,payinOrderDto);
        return Either.right(payinOrderDto);
    }


    @Override
    public Either<PaymentError, Paging<PayinOrderDto>> getPageList(PayinOrderQueryHistoryRequest request) {
        Page<PayinOrderEntity> page = new Page<>(request.getCurrentPage(),request.getPageSize());
        LambdaQueryWrapper<PayinOrderEntity> wrapper = getPayinOrderEntityWrapper(request);
        payinOrderRepository.page(page,wrapper);
        Paging<PayinOrderDto> paging = new Paging<>();
        paging.setPageIndex(request.getCurrentPage().longValue());
        paging.setPageSize(request.getPageSize().longValue());
        paging.setTotalPage(page.getPages());
        paging.setTotalNum(page.getTotal());
        List<PayinOrderDto> list = page.getRecords().stream().map(payinOrder -> {
            PayinOrderDto payinOrderDto = new PayinOrderDto();
            payinOrderEntityConvert(payinOrder,payinOrderDto);
            return payinOrderDto;
        }).collect(Collectors.toList());
        paging.setRecords(list);
        return Either.right(paging);
    }

    @Override
    public Either<PaymentError, Paging<PayinOrderAdminDto>> getAdminPageList(AdminQueryHistoryRequest request) {
        log.info("getAdminPageList request {}", request);
        Page<PayinOrderEntity> page = new Page<>(request.getCurrentPage(), request.getPageSize());
        LambdaQueryWrapper<PayinOrderEntity> wrapper = getAdminPayinOrderEntityWrapper(request);
        page = payinOrderRepository.page(page, wrapper);
        Paging<PayinOrderAdminDto> paging = new Paging<>();
        paging.setPageIndex(request.getCurrentPage().longValue());
        paging.setPageSize(request.getPageSize().longValue());
        paging.setTotalPage(page.getPages());
        paging.setTotalNum(page.getTotal());
        List<PayinOrderAdminDto> list = new ArrayList<>();
        if (page.getRecords() != null && !page.getRecords().isEmpty()) {
            list = page.getRecords().stream().map(entity -> {
                        PayinOrderAdminDto payinOrderDto = new PayinOrderAdminDto();
                        PaymentAmount payinAmount = new PaymentAmount();
                        if (Objects.nonNull(entity.getPayinCurrency())) {
                            payinAmount.setAmount(BigDecimalUtils.currencyParse(entity.getPayinAmount(), entity.getPayinCurrency()));
                            payinAmount.setCurrency(entity.getPayinCurrency());
                        }
                        PaymentAmount feeAmount = new PaymentAmount();
                        if (Objects.nonNull(entity.getFeeCurrency())){
                            feeAmount.setAmount(BigDecimalUtils.currencyParse(entity.getFeeAmount(),entity.getFeeCurrency()));
                            feeAmount.setCurrency(entity.getFeeCurrency());
                        }
                        PaymentAmount depositFiatAmount = new PaymentAmount();
                        if (Objects.nonNull(entity.getDepositFiatCurrency())) {
                            depositFiatAmount.setAmount(BigDecimalUtils.currencyParse(entity.getDepositFiatAmount(), entity.getDepositFiatCurrency()));
                            depositFiatAmount.setCurrency(entity.getDepositFiatCurrency());
                        }
                        PaymentAmount depositCryptoAmount = new PaymentAmount();
                        if (Objects.nonNull(entity.getDepositCryptoCurrency())){
                            depositCryptoAmount.setAmount(entity.getDepositCryptoAmount());
                            depositCryptoAmount.setCurrency(entity.getDepositCryptoCurrency());
                        }
                        BeanUtils.copyProperties(entity, payinOrderDto);
                        payinOrderDto.setPayinAmount(payinAmount);
                        payinOrderDto.setFeeAmount(feeAmount);
                        payinOrderDto.setDepositFiatAmount(depositFiatAmount);
                        payinOrderDto.setDepositCryptoAmount(depositCryptoAmount);
                        return payinOrderDto;
                    })
                    .collect(Collectors.toList());
        }
        paging.setRecords(list);
        log.info("getAdminPageList request {} with size : {}", request, list.size());
        return Either.right(paging);
    }

    @Override
    public void payinOrderHistorySyncProcess(IEvent<PayInOrderHistorySyncEventBo> event) {
        PayInOrderHistorySyncEventBo bo = event.getContent();
        if (StringUtils.isBlank(bo.getDepositId())) {
            log.warn("同步的订单信息没有depositId, 渠道id:{}, channelCoreId:{}, 渠道单号：{}", bo.getChannelId(), bo.getPayInTrxnId()
                    , bo.getChannelTrackId());
            return;
        }

        String lockKey = RedisKeys.LOCK_SYNC_CHANNEL_ORDER_INFO_KEY.buildKey(bo.getDepositId());
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(RedisKeys.LOCK_SYNC_CHANNEL_ORDER_INFO_KEY.getTimeout(), TimeUnit.SECONDS)) {
                doSyncPayinOrder(bo);
            } else {
                throw new RuntimeException("同步数据锁失败");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("处理depositId:{}异常", e.getMessage(), e);
            throw new RuntimeException("订单同步处理异常");
        } catch (Exception e) {
            log.error("处理depositId:{}异常", e.getMessage(), e);
            throw new RuntimeException("订单同步处理异常");
        } finally {
            if (Objects.nonNull(lock) && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private void doSyncPayinOrder(PayInOrderHistorySyncEventBo bo) {
        String payInOrderId = Constant.PIR_DEPOSIT + bo.getDepositId();
        PayinOrderEntity payinOrder = payinOrderRepository.getById(payInOrderId);
        String status = "";
        if (PayinOrderPayEventsEnum.SUCCEEDED.getCode().equalsIgnoreCase(bo.getStatus())) {
            status = PayinOrderStatusEnum.SUCCEEDED.name();
        } else if (PayinOrderPayEventsEnum.FAILED.getCode().equalsIgnoreCase(bo.getStatus())) {
            status = PayinOrderStatusEnum.FAILED.name();
        } else {
            log.error("[PaymentServiceImpl.payinOrderHistorySyncProcess] channelCoreId = {}, status {} is ignored!",
                    bo.getPayInTrxnId(),
                    bo.getStatus());
            return;
        }

        if (payinOrder == null) {
            payinOrder = new PayinOrderEntity();
            extracted(bo, payinOrder);
            payinOrder.setPayinOrderId(payInOrderId);
            payinOrder.setStatus(status);
            if (!payinOrderRepository.save(payinOrder)) {
                log.error("[PaymentServiceImpl.payinOrderHistorySyncProcess] order sync error ,payinOrder = {}",
                        JSON.toJSONString(payinOrder));
            }
        } else if (PayinOrderStatusEnum.CREATED.name().equalsIgnoreCase(payinOrder.getStatus())) {
            extracted(bo, payinOrder);
            payinOrder.setStatus(status);
            payinOrderRepository.updateById(payinOrder);
        } else {
            return;
        }
    }

    private void extracted(PayInOrderHistorySyncEventBo bo, PayinOrderEntity payinOrder) {
        payinOrder.setChannelCoreId(bo.getPayInTrxnId());
        payinOrder.setChannelId(bo.getChannelId());
        payinOrder.setUid(bo.getExternalUserId());
        payinOrder.setPaymentUserId(bo.getPaymentUserId());

        String depositCurrency = bo.getDepositAmount().getCurrency();
        long depositFiatAmount = BigDecimalUtils.currencyParseLocal(bo.getDepositAmount().getAmount().toString(),
                depositCurrency).longValue();
        payinOrder.setDepositFiatAmount(depositFiatAmount);
        payinOrder.setDepositFiatCurrency(depositCurrency);

        String feeCurrency = bo.getFeeAmount().getCurrency();
        long feeAmount =
                BigDecimalUtils.currencyParseLocal(bo.getFeeAmount().getAmount().toString(), feeCurrency).longValue();
        payinOrder.setFeeAmount(feeAmount);
        payinOrder.setFeeCurrency(feeCurrency);

        String payinCurrency = bo.getPayinAmount().getCurrency();
        long payinAmount = BigDecimalUtils.currencyParseLocal(bo.getPayinAmount().getAmount().toString(),
                feeCurrency).longValue();
        payinOrder.setPayinAmount(payinAmount);
        payinOrder.setPayinCurrency(payinCurrency);
        payinOrder.setOrderType(bo.getTradeType().name());
        payinOrder.setPaymentMethod(bo.getPaymentMethod().name());

        PaymentConfirm paymentConfirm = new PaymentConfirm();
        paymentConfirm.setPaymentInstrumentId(null);
        payinOrder.setPaymentConfirm(paymentConfirm);

        AdditionalData additionalData = new AdditionalData();
        additionalData.setClientFrom(bo.getSource());
        payinOrder.setAdditionalData(additionalData);
    }

    private LambdaQueryWrapper<PayinOrderEntity> getPayinOrderEntityWrapper(PayinOrderQueryHistoryRequest request){
        LambdaQueryWrapper<PayinOrderEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.orderByDesc(PayinOrderEntity::getCreateTime);
        lambdaQueryWrapper.eq(PayinOrderEntity::getUid, getUserId());
        if (Objects.nonNull(request.getFilter())){
            if (Objects.nonNull(request.getFilter().getOrderType())){
                lambdaQueryWrapper.eq(PayinOrderEntity::getOrderType,request.getFilter().getOrderType());
            }
            if (Objects.nonNull(request.getFilter().getFiatCurrency())){
                lambdaQueryWrapper.eq(PayinOrderEntity::getPayinCurrency,request.getFilter().getFiatCurrency());
            }
            if (Objects.nonNull(request.getFilter().getCryptoCurrency())){
                lambdaQueryWrapper.eq(PayinOrderEntity::getDepositCryptoCurrency,request.getFilter().getCryptoCurrency());
            }
            if (Objects.nonNull(request.getFilter().getOrderId())){
                lambdaQueryWrapper.eq(PayinOrderEntity::getPayinOrderId,request.getFilter().getOrderId());
            }
            if (CollectionUtils.isNotEmpty(request.getFilter().getStatus())){
                lambdaQueryWrapper.in(PayinOrderEntity::getStatus,request.getFilter().getStatus());
            }
            if (Objects.nonNull(request.getFilter().getPaymentMethod())){
                lambdaQueryWrapper.eq(PayinOrderEntity::getPaymentMethod,request.getFilter().getPaymentMethod());
            }
        }
        return lambdaQueryWrapper;
    }

    private LambdaQueryWrapper<PayinOrderEntity> getAdminPayinOrderEntityWrapper(AdminQueryHistoryRequest request) {
        LambdaQueryWrapper<PayinOrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(PayinOrderEntity::getCreateTime);
        AdminPayinFilterDto filter = request.getFilter();
        if (Objects.nonNull(filter)) {
            if (Objects.nonNull(filter.getCreateTimeStart()) && Objects.nonNull(filter.getCreateTimeEnd())) {
                queryWrapper.ge(PayinOrderEntity::getCreateTime, filter.getCreateTimeStart())
                        .le(PayinOrderEntity::getCreateTime, filter.getCreateTimeEnd());
            }
            if (Objects.nonNull(filter.getOrderType())) {
                queryWrapper.eq(PayinOrderEntity::getOrderType, filter.getOrderType());
            }
            if (Objects.nonNull(filter.getFiatCurrency())) {
                queryWrapper.eq(PayinOrderEntity::getPayinCurrency, filter.getFiatCurrency());
            }
            if (Objects.nonNull(filter.getUid())) {
                queryWrapper.eq(PayinOrderEntity::getUid, filter.getUid());
            }
            if (Objects.nonNull(filter.getCryptoCurrency())) {
                queryWrapper.eq(PayinOrderEntity::getDepositCryptoCurrency, filter.getCryptoCurrency());
            }
            if (Objects.nonNull(filter.getOrderId())) {
                queryWrapper.eq(PayinOrderEntity::getPayinOrderId, filter.getOrderId());
            }
            if (CollectionUtils.isNotEmpty(filter.getStatus())) {
                queryWrapper.in(PayinOrderEntity::getStatus, filter.getStatus());
            }
            if (Objects.nonNull(filter.getPaymentMethod())) {
                queryWrapper.eq(PayinOrderEntity::getPaymentMethod, filter.getPaymentMethod());
            }
        }
        return queryWrapper;
    }

    private void payinOrderEntityConvert(PayinOrderEntity payinOrder, PayinOrderDto payinOrderDto){
        BeanUtils.copyProperties(payinOrder,payinOrderDto);
        payinOrderDto.setOrderId(payinOrder.getPayinOrderId());
        PaymentAmount payinAmount = new PaymentAmount();
        if (Objects.nonNull(payinOrder.getPayinCurrency())) {
            payinAmount.setAmount(BigDecimalUtils.currencyParse(payinOrder.getPayinAmount(), payinOrder.getPayinCurrency()));
            payinAmount.setCurrency(payinOrder.getPayinCurrency());
        }
        PaymentAmount feeAmount = new PaymentAmount();
        if (Objects.nonNull(payinOrder.getFeeCurrency())){
            feeAmount.setAmount(BigDecimalUtils.currencyParse(payinOrder.getFeeAmount(),payinOrder.getFeeCurrency()));
            feeAmount.setCurrency(payinOrder.getFeeCurrency());
        }
        PaymentAmount depositFiatAmount = new PaymentAmount();
        if (Objects.nonNull(payinOrder.getDepositFiatCurrency())) {
            depositFiatAmount.setAmount(BigDecimalUtils.currencyParse(payinOrder.getDepositFiatAmount(), payinOrder.getDepositFiatCurrency()));
            depositFiatAmount.setCurrency(payinOrder.getDepositFiatCurrency());
        }
        PaymentAmount depositCryptoAmount = new PaymentAmount();
        if (Objects.nonNull(payinOrder.getDepositCryptoCurrency())){
            depositCryptoAmount.setAmount(payinOrder.getDepositCryptoAmount());
            depositCryptoAmount.setCurrency(payinOrder.getDepositCryptoCurrency());
        }
        payinOrderDto.setPaymentInstrumentId(payinOrder.getPaymentConfirm().getPaymentInstrumentId());
        payinOrderDto.setPayinAmount(payinAmount);
        payinOrderDto.setFeeAmount(feeAmount);
        payinOrderDto.setDepositFiatAmount(depositFiatAmount);
        payinOrderDto.setDepositCryptoAmount(depositCryptoAmount);
    }
}
