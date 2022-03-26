package com.facecto.code.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facecto.code.base.util.CodeDateTimeUtils;
import com.facecto.code.base.util.CodeStringUtils;
import com.facecto.code.pay.business.entity.OutOrderForm;
import com.facecto.code.pay.business.entity.OutRefundForm;
import com.facecto.code.pay.wechat.config.WechatConstant;
import com.facecto.code.pay.wechat.entity.*;
import com.facecto.code.pay.wechat.util.WechatHttp;
import com.facecto.code.pay.wechat.util.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
@Component
@Slf4j
public class WechatPay {

    public static WechatPay wechatPay;

    @PostConstruct
    public void init() {
        wechatPay = this;
    }

    /**
     * 用户登录：用code换用户OpenId
     *
     * @param code
     * @return openId
     */
    public String getOpenId(String code) {
        JSONObject object = WechatHttp.httpGet(getOpenIdUrl(code), "", JSONObject.class);
        return object.getString("openid");
    }

    /**
     * 付款流程：生成小程序调起支付API所需参数
     *
     * @param outOrderForm 商户订单
     * @return 参数组
     * @throws Exception
     */
    public PaymentParam handlePaymentParam(OutOrderForm outOrderForm) throws Exception {
        OrderCreateJsapi orderCreateJsapi = orderCreate(outOrderForm);
        String s = JSON.toJSONString(orderCreateJsapi);
        OrderCreateJsapiReturn r = WechatHttp.httpPost(WechatConstant.URL_JSAPI_ORDER, s, OrderCreateJsapiReturn.class);
        PaymentParam param = new PaymentParam()
                .setAppid(WechatConstant.wechatAppid)
                .setPackages("prepay_id=" + r.getPrepayId())
                .setTimeStamp(CodeDateTimeUtils.getCurrentTimeStamp())
                .setNonceStr(CodeStringUtils.getNonceStr())
                .setSignType(WechatConstant.SIGN_TYPE);
        String jsapiSign = WechatHttp.getJsapiSign(param.getTimeStamp(), param.getNonceStr(), param.getPackages());
        param.setPaySign(jsapiSign);
        return param;
    }


    /**
     * 付款流程：处理支付成功通知
     *
     * @param notice 微信发起的通知
     * @return 通知体
     */
    public OrderNotice handleOrderNotice(OrderNotice notice) {
        OrderNoticeResourceOrigin origin = null;
        if (notice != null) {
            if (notice.getEventType().equals(WechatConstant.ORDER_NOTICE_SUCCESS)) {
                String s = WechatUtils.decryptToString(notice.getResource().getAssociatedData(),
                        notice.getResource().getNonce(), notice.getResource().getCiphertext());
                origin = JSONObject.parseObject(s, OrderNoticeResourceOrigin.class);
                notice.setOrigin(origin);
            }
        }
        return notice;
    }


    /**
     * 退款流程：发起退款并获得退款结果
     *
     * @param outRefundForm 商户退款单
     * @return RefundCreateReturn
     */
    public RefundCreateReturn handleRefund(OutRefundForm outRefundForm) {
        RefundCreate refundCreate = refundCreate(outRefundForm);
        String s = JSON.toJSONString(refundCreate);
        RefundCreateReturn r = WechatHttp.httpPost(WechatConstant.URL_JSAPI_REFUND, s, RefundCreateReturn.class);
        if (r.getStatusCode() != WechatConstant.STATUS_CODE_OK) {
            r.setOutTradeNo(outRefundForm.getOutTradeNo());
            r.getAmount().setRefund(outRefundForm.getRefund());
            r.getAmount().setTotal(outRefundForm.getTotal());
            r.setOutRefundNo(refundCreate.getOutRefundNo());
        } else {
            r.setStatusCode(0);
            r.setMessage(WechatConstant.SUCCESS);
        }
        return r;
    }


    /**
     * 退款流程 ：处理退款通知
     *
     * @param notice
     * @return RefundNotice
     */
    public RefundNotice handleRefundNotice(RefundNotice notice) {
        RefundNoticeResourceOrigin origin = null;
        if (notice != null) {
            if (notice.getEventType().equals(WechatConstant.REFUND_NOTICE_SUCCESS)) {
                String s = WechatUtils.decryptToString(notice.getResource().getAssociatedData(),
                        notice.getResource().getNonce(), notice.getResource().getCiphertext());
                origin = JSONObject.parseObject(s, RefundNoticeResourceOrigin.class);
                notice.setOrigin(origin);
            }
        }
        return notice;
    }

    /**
     * 封装获取opernId的url
     *
     * @param code
     * @return url
     */
    private String getOpenIdUrl(String code) {
        String url = WechatConstant.URL_CODE2SESSION.replace("{APPID}", WechatConstant.wechatAppid)
                .replace("{SECRET}", WechatConstant.wechatSecret)
                .replace("{JSCODE}", code);
        return url;
    }

    /**
     * 封装原始下单信息（JSAPI下单所需信息）
     *
     * @param outOrderForm 商户订单
     * @return 预订单
     */
    private OrderCreateJsapi orderCreate(OutOrderForm outOrderForm) {
        AmountOrder amount = new AmountOrder()
                .setCurrency(WechatConstant.CURRENCY)
                .setTotal(outOrderForm.getTotal());

        OrderCreateJsapi order = new OrderCreateJsapi()
                .setAppid(WechatConstant.wechatAppid)
                .setMchid(WechatConstant.wechatMchid)
                .setDescription(outOrderForm.getGoodInfo())
                .setOutTradeNo(outOrderForm.getOutTradeNo())
                .setTimeExpire(null)
                .setAttach(outOrderForm.getAttach())
                .setNotifyUrl(WechatConstant.orderNotifyUrl)
                .setAmount(amount)
                .setPayer(outOrderForm.getPayer());
        return order;
    }

    /**
     * 生成退款信息
     *
     * @param outRefundForm 退款信息
     * @return RefundCreate
     */
    private RefundCreate refundCreate(OutRefundForm outRefundForm) {
        AmountRefund amount = new AmountRefund()
                .setRefund(outRefundForm.getRefund())
                .setTotal(outRefundForm.getTotal())
                .setCurrency(WechatConstant.CURRENCY);
        RefundCreate refundCreate = new RefundCreate()
                .setOutRefundNo(outRefundForm.getOutRefundNo())
                .setAmount(amount)
                .setNotifyUrl(WechatConstant.refundNotifyUrl)
                .setOutTradeNo(outRefundForm.getOutTradeNo())
                .setReason(outRefundForm.getReason())
                .setFundsAccount(WechatConstant.REFUND_ACCOUNT);
        return refundCreate;
    }
}
