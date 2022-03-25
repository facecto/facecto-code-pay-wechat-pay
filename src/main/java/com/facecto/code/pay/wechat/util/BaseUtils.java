package com.facecto.code.pay.wechat.util;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
public class BaseUtils {
    /**
     * 从resource读取文件字节
     *
     * @param path
     * @return byte[]
     * @throws IOException
     */
    public static byte[] getBytes(String path) throws IOException {
        ClassPathResource classResource = new ClassPathResource(path);
        InputStream stream = classResource.getInputStream();
        byte[] bytes = IOUtils.toByteArray(stream);
        stream.read(bytes);
        stream.close();
        return bytes;
    }

    /**
     * 从resource读取文件流
     *
     * @param path
     * @return stream
     * @throws IOException
     */
    public static InputStream getStream(String path) throws IOException {
        return new ByteArrayInputStream(getBytes(path));
    }

    /**
     * 获取随机字符串
     *
     * @return 随机字符串
     */
    public static String getNonceStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取当前的Timestamp
     *
     * @return timestamp
     */
    public static String getCurrentTimeStamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /**
     * 获取当前的时间
     *
     * @return time
     */
    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }

    /**
     * 生成订单号
     *
     * @return orderNo
     */
    public static String getOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        return sdf.format(new Date()) + getUUID(16);
    }

    /**
     * 生成随机数
     *
     * @return uuid
     */
    private static String getUUID(int len) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, len);
    }

}
