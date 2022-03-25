package com.facecto.code.pay.business.entity;

import com.facecto.code.pay.wechat.entity.Payer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
@Data
@Accessors(chain = true)
public class BusinessPreOrder extends BusinessProduct {
    /**
     * 商户订单号
     */
    private String outTradeNo;
    /**
     * 客户 openId
     */
    private Payer payer;
}
