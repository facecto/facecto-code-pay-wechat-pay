package com.facecto.code.pay.business.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
@Data
@Accessors(chain = true)
public class OutRefundForm {
    /**
     * 商户订单号
     * string[6,32]
     * 必
     */
    private String outTradeNo;
    /**
     * 商户退款单号
     * string[1, 64]
     * 必
     */
    private String outRefundNo;
    /**
     * 退款原因
     * string[1, 80]
     * 否
     */
    private String reason;
    /**
     * 退款金额 分
     * 必
     */
    private int refund;
    /**
     * 原订单金额 分
     * 必
     */
    private int total;
}
