package com.facecto.code.pay.wechat.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
@Data
@Accessors(chain = true)
public class AmountRefundNotice implements Serializable {
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
    @JsonProperty("payer_total")
    private int payerTotal;
    /**
     * 用户退款金额
     * 必
     */
    @JsonProperty("payer_refund")
    private int payerRefund;
}
