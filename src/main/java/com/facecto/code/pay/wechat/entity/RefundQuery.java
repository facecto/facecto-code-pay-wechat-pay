package com.facecto.code.pay.wechat.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
@Data
public class RefundQuery implements Serializable {

    /**
     * 商户退款单号
     * string[1, 64]
     */
    private String outRefundNo;
}
