package com.facecto.code.pay.wechat.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
@Data
@Accessors(chain = true)
public class WechatCertificate implements Serializable {
    /**
     * 平台证书序列号
     */
    @JSONField(name = "serial_no")
    private String serialNo;

    /**
     * 平台证书有效时间
     */
    @JSONField(name = "effective_time")
    private String effectiveTime;

    /**
     * 平台证书过期时间
     */
    @JSONField(name = "expire_time")
    private String expireTime;

    /**
     * 加密证书
     */
    @JSONField(name = "encrypt_certificate")
    private WechatEncryptCertificate wechatEncryptCertificate;
}
