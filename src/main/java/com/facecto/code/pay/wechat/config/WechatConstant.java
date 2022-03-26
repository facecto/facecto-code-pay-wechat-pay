package com.facecto.code.pay.wechat.config;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
public class WechatConstant {
    public final static String URL_JSAPI_ORDER = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";
    public final static String URL_JSAPI_ORDER_QUERY = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/{out_trade_no}";
    public final static String URL_JSAPI_REFUND = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds";
    public final static String URL_JSAPI_REFUND_QUERY = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds/{out_refund_no}";
    public final static String URL_CERTIFICATES = "https://api.mch.weixin.qq.com/v3/certificates";
    public final static String URL_CODE2SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid={APPID}&secret={SECRET}&js_code={JSCODE}&grant_type=authorization_code";
    public final static String CURRENCY = "CNY";
    public final static String ORDER_NOTICE_SUCCESS = "TRANSACTION.SUCCESS";
    public final static String ORDER_NOTICE_SUCCESS_CN = "支付成功";
    public final static String REFUND_NOTICE_SUCCESS = "REFUND.SUCCESS";
    public final static String REFUND_NOTICE_SUCCESS_CN = "退款成功";
    public final static String SUCCESS = "SUCCESS";
    public final static String FAIL = "FAIL";
    public final static String SUCCESS_CN = "成功";
    public final static String FAIL_CN = "失败";
    public final static String SIGN_TYPE = "RSA";
    public final static String KEY_ALIAS = "tenpay certificate";
    public final static String REFUND_ACCOUNT = "AVAILABLE";
    public final static String CONTENT_TYPE_NAME = "Content-Type";
    public final static String CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";
    public final static String ACCEPT = "Accept";
    public final static String ACCEPT_JSON = "application/json";
    public final static String ACCEPT_HTML_XML = "text/html, application/xhtml+xml, image/jxr, */*";
    public final static String AUTHORIZATION = "Authorization";
    public final static String POST = "POST";
    public final static String GET = "GET";
    public final static String RSA = "SHA256withRSA";
    public final static String AES_NAME = "AES";
    public final static String AES_SETTING = "AES/GCM/NoPadding";
    public final static int SECOND = 5000000;
    public final static int STATUS_CODE_OK = 200;
    public final static int GCM_LENGTH = 128;
    /**
     * 小程序id
     */
    public static String wechatAppid;

    /**
     * 直连商户号
     */
    public static String wechatMchid;

    /**
     * AppSecret
     */
    public static String wechatSecret;

    /**
     * 证书地址
     */
    public static String certUrl;

    /**
     * 证书秘钥地址
     */
    public static String certKey;

    /**
     * v3Key
     */
    public static String v3key;

    /**
     * 私钥
     */
    public static PrivateKey privateKey;

    /**
     * 证书系列号
     */
    public static String serialNo;
    /**
     * 证书
     */
    public static Map<String, X509Certificate> certificateMap = new ConcurrentHashMap<>();

    /**
     * 下单通知
     */
    public static String orderNotifyUrl;

    /**
     * 退单通知
     */
    public static String refundNotifyUrl;
}

