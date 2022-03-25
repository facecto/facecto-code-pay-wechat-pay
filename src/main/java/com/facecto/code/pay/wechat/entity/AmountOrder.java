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
public class AmountOrder implements Serializable {
    /**
     * 订单总金额，单位为分。
     * int
     * 必
     */
    private int total;
    /**
     * CNY：人民币，境内商户号仅支持人民币。
     * string[1,16]
     * 否
     */
    private String currency;

}
