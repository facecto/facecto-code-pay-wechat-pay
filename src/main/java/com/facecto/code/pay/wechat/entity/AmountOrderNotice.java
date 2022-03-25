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
public class AmountOrderNotice implements Serializable {
    /**
     * 订单总金额，单位为分。
     * int
     * 必
     */
    private int total;
    /**
     * CNY：人民币，境内商户号仅支持人民币。
     * string[1,16]
     * 必
     */
    private String currency;

    /**
     * 用户支付金额
     * int
     * 必
     */
    @JsonProperty("payer_total")
    private int payerTotal;

    /**
     * 用户支付币种
     * string[1,16]
     * 必
     */
    @JsonProperty("payer_currency")
    private String payerCurrency;
}
