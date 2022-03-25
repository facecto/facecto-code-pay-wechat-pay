//package com.facecto.code.pay.wechat.config;
//
//
//import com.facecto.code.pay.wechat.util.WechatUtils;
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.stereotype.Component;
//
//import java.security.cert.X509Certificate;
//
///**
// * @author Jon So, https://facecto.com, https://github.com/facecto
// * @version v1.0.0
// */
//@Component
//@PropertySource("classpath:application.yml")
//@Data
//public class WechatConfig implements ApplicationRunner {
//    @Value("${pay.wechat.appid}")
//    private String wechatAppid;
//
//    @Value("${pay.wechat.mchid}")
//    private String wechatMchid;
//
//    @Value("${pay.wechat.cert}")
//    private String certUrl;
//
//    @Value("${pay.wechat.cert-key}")
//    private String certKey;
//
//    @Value("${pay.wechat.v3key}")
//    private String v3key;
//
//    @Value("${pay.wechat.app-secret}")
//    private String wechatSecret;
//
//    @Value("${pay.wechat.order-notify}")
//    private String orderNotifyUrl;
//
//    @Value("${pay.wechat.refund-notify}")
//    private String refundNotifyUrl;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        WechatConstant.wechatAppid = wechatAppid;
//        WechatConstant.wechatMchid = wechatMchid;
//        WechatConstant.wechatSecret = wechatSecret;
//        WechatConstant.certUrl = certUrl;
//        WechatConstant.certKey = certKey;
//        WechatConstant.orderNotifyUrl = orderNotifyUrl;
//        WechatConstant.refundNotifyUrl = refundNotifyUrl;
//        WechatConstant.v3key = v3key;
//        WechatConstant.privateKey = WechatUtils.getPrivateKey();
//        WechatConstant.serialNo = WechatUtils.getSerialNumber();
//        WechatConstant.certificateMap = WechatUtils.refreshCertificate();
//        X509Certificate certificate = WechatUtils.getCertificate();
//    }
//
//}