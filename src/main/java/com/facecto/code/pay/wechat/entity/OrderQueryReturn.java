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
public class OrderQueryReturn implements Serializable {
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
     * 商户订单号
     * string[6,32]
     * 必
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 微信支付订单号
     * string[1,32]
     * 否
     */
    @JSONField(name = "transaction_id")
    private String transactionId;
    /**
     * 交易类型
     * string[1,16]
     * 否
     */
    @JSONField(name = "trade_type")
    private String tradeType;
    /**
     * 交易状态
     * string[1,32]
     * 必
     */
    @JSONField(name = "trade_state")
    private String tradeState;
    /**
     * 交易状态描述
     * string[1,256]
     * 必
     */
    @JSONField(name = "trade_state_desc")
    private String tradeStateDesc;
    /**
     * 付款银行
     * string[1,32]
     * 否
     */
    @JSONField(name = "bank_type")
    private String bankType;
    /**
     * 支付完成时间
     * string[1,64]
     * 否
     */
    @JSONField(name = "success_time")
    private String successTime;

    /**
     * 附加数据
     * string[1,128]
     * 否
     */
    private String attach;

    /**
     * 支付者信息
     * 必
     */
    private Payer payer;

    /**
     * 订单金额信息，当支付成功时返回该字段。
     * 否
     */
    private AmountQueryReturn amount;

}