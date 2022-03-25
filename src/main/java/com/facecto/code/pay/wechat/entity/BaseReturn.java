package com.facecto.code.pay.wechat.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
@Data
public class BaseReturn implements Serializable {
    private String code;
    private String message;
    private int statusCode;
}
