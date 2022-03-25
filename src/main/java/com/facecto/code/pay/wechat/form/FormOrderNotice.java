package com.facecto.code.pay.wechat.form;

import com.facecto.code.pay.wechat.entity.OrderNotice;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
@Data
@Accessors(chain = true)
public class FormOrderNotice extends OrderNotice implements Serializable {

}