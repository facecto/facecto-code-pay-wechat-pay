package com.facecto.code.pay.wechat.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
@Data
@Accessors(chain = true)
public class AmountRefund implements Serializable {
    /**
     * 退款金额
     * int
     * 必
     */
    private int refund;
    /**
     * 退款币种
     * string[1,16]
     * 是：CNY
     */
    private String currency;

    /**
     * 原订单金额
     * int
     * 必
     */
    private int total;

    // todo 要增加 from

}
