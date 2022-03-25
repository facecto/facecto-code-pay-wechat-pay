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
public class WechatEncryptCertificate implements Serializable {
    /**
     * 算法
     */
    private String algorithm;

    /**
     * 随机字符串
     */
    private String nonce;

    /**
     * 相关数据
     */
    @JSONField(name = "associated_data")
    private String associatedData;

    /**
     * 密文
     */
    private String ciphertext;
}

