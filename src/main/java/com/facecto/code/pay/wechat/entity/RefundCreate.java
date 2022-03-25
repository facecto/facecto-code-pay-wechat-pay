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
public class RefundCreate implements Serializable {
    /**
     * 商户订单号
     * string[6, 32]
     * 必
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    /**
     * 商户退款单号
     * string[1, 64]
     * 必
     */
    @JSONField(name = "out_refund_no")
    private String outRefundNo;
    /**
     * 退款原因
     * string[1, 80]
     * 否
     */
    private String reason;
    /**
     * 退款结果回调url
     * string[8, 256]
     * 否
     */
    @JSONField(name = "notify_url")
    private String notifyUrl;
    /**
     * 退款资金来源
     * string[1,32]
     * 否
     */
    @JSONField(name = "funds_account")
    private String fundsAccount;
    /**
     * 订单金额信息
     */
    @JSONField(name = "amount")
    private AmountRefund amount;
}

