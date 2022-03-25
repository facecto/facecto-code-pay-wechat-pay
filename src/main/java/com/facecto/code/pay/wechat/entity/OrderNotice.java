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
public class OrderNotice implements Serializable {
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
     * 必
     */
    @JsonProperty("create_time")
    private String createTime;
    /**
     * 通知类型
     * string[1,32]
     * 必
     */
    @JsonProperty("event_type")
    private String eventType;
    /**
     * 通知数据类型
     * string[1,32]
     * 必
     */
    @JsonProperty("resource_type")
    private String resourceType;
    /**
     * 通知数据类型
     * string[1,32]
     * 必
     */
    @JsonProperty("resource")
    private OrderNoticeResource resource;
    /**
     * 回调摘要
     * string[1,64]
     * 必
     */
    @JsonProperty("summary")
    private String summary;

    /**
     * 解密数据
     */
    @JsonProperty("origin")
    private OrderNoticeResourceOrigin origin;
}

