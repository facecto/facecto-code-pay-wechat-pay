package com.facecto.code.pay.business.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
@Data
@Accessors(chain = true)
public class BusinessProduct implements Serializable {
    /**
     * 商品信息
     */
    private String goods;
    /**
     * 单价
     */
    private int price;
    /**
     * 数量
     */
    private int num;
    /**
     * 总金额
     * 分
     */
    private int totalFee;
}

