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
public class OrderQuery implements Serializable {
    /**
     * string[1,32]
     * 必
     */
    private String mchid;
    /**
     * string[6,32]
     * 必
     */
    private String outTradeNo;
}
