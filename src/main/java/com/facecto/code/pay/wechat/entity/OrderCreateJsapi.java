package com.facecto.code.pay.wechat.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
@Data
@Accessors(chain = true)
public class OrderCreateJsapi implements Serializable {
    /**
     * 应用ID
     * string[1,32]
     * 必
     */
    private String appid;
    /**
     * 直连商户号
     * string[1,32]
     * 必
     */
    private String mchid;
    /**
     * 商品描述
     * string[1,127]
     * 必
     */
    private String description;
    /**
     * 商户订单号
     * string[6,32]
     * 必
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    /**
     * 通知地址
     * string[1,256]
     * 必
     */
    @JSONField(name = "notify_url")
    private String notifyUrl;

    /**
     * 交易结束时间
     * string[1,64]
     * 否
     */
    @JSONField(name = "time_expire")
    private String timeExpire;

    /**
     * 附加数据
     * string[1,128]
     * 否
     */
    private String attach;
    /**
     * 订单金额信息
     */
    private AmountOrder amount;

    /**
     * 支付者信息
     */
    private Payer payer;

}
