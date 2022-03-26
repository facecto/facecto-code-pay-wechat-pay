package com.facecto.code.pay.business.entity;

import com.facecto.code.pay.wechat.entity.Payer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
@Data
@Accessors(chain = true)
public class OutOrderForm implements Serializable {
    /**
     * 商户订单号
     * string[6,32]
     */
    private String outTradeNo;
    /**
     * 商品信息
     * string[1,127]
     */
    private String goodInfo;
    /**
     * 附加数据，在通知中原样返回
     * string[1,128]
     */
    private String attach;
    /**
     * 支付者信息
     * 内含 openid
     */
    private Payer payer;
    /**
     * 总金额 分
     * int
     */
    private int total;
}
