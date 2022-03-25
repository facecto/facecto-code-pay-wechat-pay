package com.facecto.code.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facecto.code.pay.business.entity.BusinessPreOrder;
import com.facecto.code.pay.wechat.config.WechatConstant;
import com.facecto.code.pay.wechat.entity.*;
import com.facecto.code.pay.wechat.util.BaseUtils;
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
     * @param code
     * @return openId
     */
    public String getOpenId(String code) {
        JSONObject object = WechatHttp.httpGet(getOpenIdUrl(code), "", JSONObject.class);
        System.out.println(object);
        return object.getString("openid");
    }

    /**
     * 付款流程：生成小程序调起支付API所需参数
     * @param preOrder JSAPI下单信息
     * @return 小程序所需参数
     * @throws Exception
     */
    public PaymentParam handlePaymentParam(BusinessPreOrder preOrder) throws Exception {
        OrderCreateJsapi orderCreateJsapi = orderCreate(preOrder);
        String s = JSON.toJSONString(orderCreateJsapi);
        OrderCreateJsapiReturn r = WechatHttp.httpPost(WechatConstant.URL_JSAPI_ORDER, s, OrderCreateJsapiReturn.class);
        PaymentParam param = new PaymentParam()
                .setAppid(WechatConstant.wechatAppid)
                .setPackages("prepay_id=" + r.getPrepayId())
                .setTimeStamp(BaseUtils.getCurrentTimeStamp())
                .setNonceStr(BaseUtils.getNonceStr())
                .setSignType(WechatConstant.SIGN_TYPE);
        String jsapiSign = WechatHttp.getJsapiSign(param.getTimeStamp(), param.getNonceStr(), param.getPackages());
        param.setPaySign(jsapiSign);
        return param;
    }


    /**
     * 付款流程：处理支付成功通知
     * @param notice 微信发起的通知
     * @return 通知体
     */
    public OrderNotice handleOrderNotice(OrderNotice notice) {
        OrderNoticeResourceOrigin origin =null;
        if(notice!=null){
            if(notice.getEventType().equals(WechatConstant.ORDER_NOTICE_SUCCESS)){
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
     * @param outTradeNo 商户订单号
     * @param reason 退款原因
     * @param refund 退款金额
     * @param total 订单金额
     * @return RefundCreateReturn
     */
    public RefundCreateReturn handleRefund(String outTradeNo,String reason,int refund, int total){
        RefundCreate refundCreate = refundCreate(outTradeNo, reason,refund,total);
        String s = JSON.toJSONString(refundCreate);
        RefundCreateReturn r = WechatHttp.httpPost(WechatConstant.URL_JSAPI_REFUND, s, RefundCreateReturn.class);
        if(r.getStatusCode()!=WechatConstant.STATUS_CODE_OK){
            r.setOutTradeNo(outTradeNo);
            r.getAmount().setRefund(refund);
            r.getAmount().setTotal(total);
            r.setOutRefundNo(refundCreate.getOutRefundNo());
        }else {
            r.setStatusCode(0);
            r.setMessage(WechatConstant.SUCCESS);
        }
        return r;
    }


    /**
     * 退款流程 ：处理退款通知
     * @param notice
     * @return RefundNotice
     */
    public RefundNotice handleRefundNotice(RefundNotice notice) {
        RefundNoticeResourceOrigin origin =null;
        if(notice!=null){
            if(notice.getEventType().equals(WechatConstant.REFUND_NOTICE_SUCCESS)){
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
     * @param businessPreOrder
     * @return OrderCreateJsapi
     */
    private OrderCreateJsapi orderCreate(BusinessPreOrder businessPreOrder) {
        AmountOrder amount = new AmountOrder()
                .setCurrency(WechatConstant.CURRENCY)
                .setTotal(businessPreOrder.getTotalFee());

        OrderCreateJsapi order = new OrderCreateJsapi()
                .setAppid(WechatConstant.wechatAppid)
                .setMchid(WechatConstant.wechatMchid)
                .setDescription(businessPreOrder.getGoods())
                .setOutTradeNo(businessPreOrder.getOutTradeNo())
                .setTimeExpire(null)
                .setAttach(businessPreOrder.getOutTradeNo())
                .setNotifyUrl(WechatConstant.orderNotifyUrl)
                .setAmount(amount)
                .setPayer(businessPreOrder.getPayer());
        return order;
    }

    /**
     * 生成退款信息
     * @param outTradeNo
     * @param reason
     * @param refund
     * @param total
     * @return RefundCreate
     */
    private RefundCreate refundCreate(String outTradeNo,String reason,int refund, int total){
        AmountRefund amount = new AmountRefund()
                .setRefund(refund)
                .setTotal(total)
                .setCurrency(WechatConstant.CURRENCY);
        RefundCreate refundCreate = new RefundCreate()
                .setOutRefundNo(BaseUtils.getOrderNo())
                .setAmount(amount)
                .setNotifyUrl(WechatConstant.refundNotifyUrl)
                .setOutTradeNo(outTradeNo)
                .setReason(reason)
                .setFundsAccount(WechatConstant.REFUND_ACCOUNT)
                ;
        return refundCreate;
    }
}
