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
public class Payer implements Serializable {
    /**
     * 用户在直连商户appid下的唯一标识。 下单前需获取到用户的Openid
     * string[1,128]
     */
    private String openid;
}

