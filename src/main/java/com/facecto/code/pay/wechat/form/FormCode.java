package com.facecto.code.pay.wechat.form;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
@Data
public class FormCode implements Serializable {
    private String code;
}
