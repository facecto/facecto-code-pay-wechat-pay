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
public class RefundNoticeResource implements Serializable {
    /**
     * 加密算法类型
     * string[1,32]
     * 必
     */
    @JsonProperty("algorithm")
    private String algorithm;
    /**
     * 加密前的对象类型
     * string[1,32]
     * 必
     */
    @JsonProperty("original_type")
    private String originalType;
    /**
     * 数据密文
     * string[1,1048576]
     * 必
     */
    @JsonProperty("ciphertext")
    private String ciphertext;
    /**
     * 附加数据
     * string[1,16]
     * 否
     */
    @JsonProperty("associated_data")
    private String associatedData;
    /**
     * 随机串
     * string[1,16]
     * 必
     */
    @JsonProperty("nonce")
    private String nonce;
}

