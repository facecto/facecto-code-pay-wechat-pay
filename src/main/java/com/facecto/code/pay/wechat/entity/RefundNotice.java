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
public class RefundNotice implements Serializable {
    /**
     * 通知ID
     * string[1,36]
     * 必
     */
    @JsonProperty("id")
    private String id;
    /**
     * 通知创建时间
     * string[1,32]
     * 必-
     */
    @JsonProperty( "create_time")
    private String createTime;
    /**
     * 通知类型
     * string[1,32]
     * 必-
     */
    @JsonProperty("event_type")
    private String eventType;
    /**
     * 通知简要说明
     * string[1,16]
     * 必-
     */
    @JsonProperty("summary")
    private String summary;
    /**
     * 通知数据类型
     * string[1,32]
     * 必
     */
    @JsonProperty("resource_type")
    private String resourceType;
    /**
     * 通知资源数据
     * 必
     */
    @JsonProperty("resource")
    private RefundNoticeResource resource;

    /**
     * 解密信息
     */
    @JsonProperty("origin")
    private RefundNoticeResourceOrigin origin;

}
