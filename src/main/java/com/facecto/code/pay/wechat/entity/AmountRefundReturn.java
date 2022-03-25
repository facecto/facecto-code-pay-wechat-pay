package com.facecto.code.pay.wechat.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
@Data
@Accessors(chain = true)
public class AmountRefundReturn {
    /**
     * 订单金额
     * 必
     */
    private int total;
    /**
     * 退款金额
     * 必
     */
    private int refund;
    /**
     * 用户支付金额
     * 必
     */
    @JSONField(name = "payer_total")
    private int payerTotal;
    /**
     * 用户退款金额
     * 必
     */
    @JSONField(name = "payer_refund")
    private int payerRefund;
    /**
     * 应结退款金额
     * 必
     */
    @JSONField(name = "settlement_refund")
    private int settlementRefund;
    /**
     * 应结订单金额
     * 必
     */
    @JSONField(name = "settlement_total")
    private int settlementTotal;
    /**
     * 优惠退款金额
     * 必
     */
    @JSONField(name = "discount_refund")
    private int discountRefund;
    /**
     * 退款币种
     * 必
     */
    private String currency;

    // todo from
}
