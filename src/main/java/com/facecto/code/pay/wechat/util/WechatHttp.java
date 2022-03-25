package com.facecto.code.pay.wechat.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facecto.code.base.CodeException;
import com.facecto.code.pay.wechat.config.WechatConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Base64Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
@Slf4j
public class WechatHttp {

    /**
     * 发起HTTP请求，并返回实体
     * @param url 目标URL
     * @param json 参数（GET参数）
     * @param t 目标实体.class
     * @param <T> 目标实体
     * @return 目标实体
     */
    public static <T> T httpGet(String url, String json, Class<T> t) {
        try {
            HttpResponse response = getGetResponse(url,json);
            int statusCode = response.getStatusLine().getStatusCode();
            String result =null;
            if (statusCode == WechatConstant.STATUS_CODE_OK) {
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
            return getTbyResult(t,statusCode, result, t.newInstance());
        } catch (Exception e){
            throw new CodeException("执行http请求失败！");
        }
    }

    /**
     * 发起Post请求
     * @param url 目标URL
     * @param json 参数（POST参数）
     * @param t 目标实体.class
     * @param <T> 目标实体
     * @return 目标实体
     */
    public static <T> T httpPost(String url, String json, Class<T> t){
        try {
            HttpResponse response = getPostResponse(url,json);
            int statusCode = response.getStatusLine().getStatusCode();
            String result =null;
            if (statusCode == WechatConstant.STATUS_CODE_OK) {
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
            return getTbyResult(t, statusCode, result, t.newInstance());
        } catch (Exception e){
            throw new CodeException("执行http请求失败！");
        }
    }

    /**
     * 将JSON文本转换成目标实体类
     * @param t 目标实体
     * @param statusCode http状态码
     * @param jsonString JsonString
     * @param instance 实例
     * @param <T> 目标类
     * @return 目标类
     */
    private static <T> T getTbyResult(Class<T> t, int statusCode, String jsonString, T instance) {
        JSONObject jsonObject = JSON.parseObject(jsonString);
        jsonObject.put("statusCode",statusCode);
        if (instance instanceof JSONObject) {
            return (T) jsonObject;
        }
        T resultBean = (T) JSONObject.parseObject(jsonObject.toString(), t);
        return resultBean;
    }

    /**
     * 获得POST方法返回body
     * @param url
     * @param json
     * @return response
     */
    private static HttpResponse getPostResponse(String url, String json){
        String token = httpToken(WechatConstant.POST, url, json);
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader(WechatConstant.CONTENT_TYPE_NAME, WechatConstant.CONTENT_TYPE_VALUE);
        httppost.addHeader(WechatConstant.ACCEPT, WechatConstant.ACCEPT_JSON);
        httppost.addHeader(WechatConstant.ACCEPT, WechatConstant.ACCEPT_HTML_XML);
        httppost.addHeader(WechatConstant.AUTHORIZATION, token);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(WechatConstant.SECOND).setConnectionRequestTimeout(WechatConstant.SECOND)
                .setSocketTimeout(WechatConstant.SECOND).build();
        httppost.setConfig(requestConfig);
        if (null != json) {
            StringEntity myEntity = new StringEntity(json, "UTF-8");
            httppost.setEntity(myEntity);
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpResponse response = httpClient.execute(httppost);
            httpClient.close();
            return response;
        }catch (Exception e){
            throw new CodeException("执行http请求失败！");
        }
    }

    /**
     * get http token
     * @param method GET POST
     * @param urlString URL
     * @param body Post method with JSON, Get method with ""
     * @return token
     * @throws Exception
     */
    private static String httpToken(String method, String urlString, String body) {
        Long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = UUID.randomUUID().toString().replace("-", "");
        String signature = httpSign(method, urlString, body, timestamp, nonceStr);
        final String TOKEN_PATTERN = "WECHATPAY2-SHA256-RSA2048 mchid=\"%s\",nonce_str=\"%s\",timestamp=\"%d\",serial_no=\"%s\",signature=\"%s\"";
        return String.format(TOKEN_PATTERN,
                WechatConstant.wechatMchid,
                nonceStr, timestamp, WechatConstant.serialNo, signature);
    }

    /**
     * 获得GET方法返回body
     * @param url url
     * @param json json
     * @return body
     */
    private static HttpResponse getGetResponse(String url, String json){
        String token = httpToken(WechatConstant.GET, url, json);
        HttpGet httpget = new HttpGet(url);
        httpget.addHeader(WechatConstant.CONTENT_TYPE_NAME, WechatConstant.CONTENT_TYPE_VALUE);
        httpget.addHeader(WechatConstant.ACCEPT, WechatConstant.ACCEPT_JSON);
        httpget.addHeader(WechatConstant.AUTHORIZATION, token);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpResponse response = httpClient.execute(httpget);
            httpClient.close();
            return response;
        }catch (Exception e){
            throw new CodeException("执行http请求失败！");
        }
    }

    /**
     * Get http sign
     * @param method GET POST
     * @param urlString URL
     * @param body Post method with JSON, Get method with ""
     * @param timestamp timestamp
     * @param nonceStr Random String
     * @return sign
     * @throws Exception
     */
    public static String httpSign(String method, String urlString, String body, long timestamp, String nonceStr) {
        URL url = null;
        try{
            url = new URL(urlString);
        }catch (MalformedURLException e){
            throw new CodeException("URL错误！");
        }
        String signUrl;
        if (WechatConstant.GET.equals(method) && url.getQuery() != null) {
            signUrl = url.getPath() + "?" + url.getQuery();
        } else {
            signUrl = url.getPath();
        }
        String signatureStr = Stream.of(method, signUrl, String.valueOf(timestamp), nonceStr, body)
                .collect(Collectors.joining("\n", "", "\n"));
        return getSign(signatureStr);
    }


    /**
     * JSAPI SIGN
     * @param timestamp timestamp
     * @param nonceStr  Random String
     * @param packages body
     * @return sign
     * @throws Exception
     */
    public static String getJsapiSign(String timestamp, String nonceStr, String packages) {
        String signatureStr = Stream.of(WechatConstant.wechatAppid, timestamp, nonceStr, packages)
                .collect(Collectors.joining("\n", "", "\n"));
        return getSign(signatureStr);
    }

    /**
     * get sign with RSA
     * @param signatureStr
     * @return sign
     * @throws Exception
     */
    public static String getSign(String signatureStr) {
        try{
            Signature sign = Signature.getInstance(WechatConstant.RSA);
            sign.initSign(WechatConstant.privateKey);
            sign.update(signatureStr.getBytes(StandardCharsets.UTF_8));
            return Base64Utils.encodeToString(sign.sign());}
        catch(Exception e){
            throw new CodeException("获得RSA签名失败！");
        }
    }
}
