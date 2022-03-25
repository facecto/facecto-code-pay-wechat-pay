package com.facecto.code.pay.wechat.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facecto.code.base.CodeException;
import com.facecto.code.pay.wechat.config.WechatConstant;
import com.facecto.code.pay.wechat.entity.WechatCertificate;
import com.facecto.code.pay.wechat.entity.WechatEncryptCertificate;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jon So, https://facecto.com, https://github.com/facecto
 * @version v1.0.0
 */
public class WechatUtils {

    /**
     * 从证书文件中读取证书读取系列号
     *
     * @return serialNo.
     * @throws Exception
     */
    public static String getSerialNumber() throws Exception {
        return getCertificate().getSerialNumber().toString(16).toUpperCase();
    }

    /**
     * 从证书文件中读取证书读取私钥
     *
     * @return privateKey
     * @throws Exception
     */
    public static PrivateKey getPrivateKey() throws Exception {
        KeyStore ks = getKeyStore();
        PrivateKey privateKey = (PrivateKey) ks.getKey(WechatConstant.KEY_ALIAS, WechatConstant.wechatMchid.toCharArray());
        return privateKey;
    }


    /**
     * 从证书文件中获取KeyStore
     * @return keystore
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private static KeyStore getKeyStore() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        BufferedInputStream bis = new BufferedInputStream(BaseUtils.getStream(WechatConstant.certUrl));
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(bis, WechatConstant.wechatMchid.toCharArray());
        return ks;
    }

    /**
     * 从证书文件中获取KeyAlias
     *
     * @return keyalias
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private static String getKeyAlias() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        BufferedInputStream bis = new BufferedInputStream(BaseUtils.getStream(WechatConstant.certUrl));
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(bis, WechatConstant.wechatMchid.toCharArray());
        String keyAlias = null;
        Enumeration<String> aliases = ks.aliases();
        if (aliases.hasMoreElements()) {
            keyAlias = aliases.nextElement();
        }
        return keyAlias;
    }

    /***
     * 从证书文件中读取证书
     * @return x509
     * @throws Exception
     */
    public static X509Certificate getCertificate() throws Exception {
        KeyStore ks = getKeyStore();
        Certificate cert = ks.getCertificate(WechatConstant.KEY_ALIAS);
        return (X509Certificate) cert;
    }

    /**
     * 从私钥文件读取私钥
     * @param filename
     * @return privateKey
     * @throws IOException
     */
    public static PrivateKey getPrivateKey(String filename) throws IOException {
        String content = new String(BaseUtils.getBytes(filename), StandardCharsets.UTF_8);
        try {
            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }


    /**
     * 获取平台证书Map
     * @return map
     * @throws ParseException
     * @throws CertificateException
     */
    public static Map<String, X509Certificate> refreshCertificate() throws ParseException, CertificateException {
        JSONObject jsonObject = WechatHttp.httpGet(WechatConstant.URL_CERTIFICATES, "", JSONObject.class);
        List<WechatCertificate> certificateList = JSON.parseArray(jsonObject.getString("data"), WechatCertificate.class);
        WechatCertificate newestCertificate = null;
        Date newestTime = null;
        for (WechatCertificate certificate : certificateList) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            if (newestTime == null) {
                newestCertificate = certificate;
                newestTime = formatter.parse(certificate.getEffectiveTime());
            } else {
                Date effectiveTime = formatter.parse(certificate.getEffectiveTime());
                if (effectiveTime.getTime() > newestTime.getTime()) {
                    newestCertificate = certificate;
                }
            }
        }
        WechatEncryptCertificate encryptCertificate = newestCertificate.getWechatEncryptCertificate();
        String publicKey = decryptToString(encryptCertificate.getAssociatedData(), encryptCertificate.getNonce(), encryptCertificate.getCiphertext());
        CertificateFactory cf = CertificateFactory.getInstance("X509");

        ByteArrayInputStream inputStream = new ByteArrayInputStream(publicKey.getBytes(StandardCharsets.UTF_8));
        X509Certificate certificate = null;
        try {
            certificate = (X509Certificate) cf.generateCertificate(inputStream);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        Map<String, X509Certificate> certificateMap = new ConcurrentHashMap<>();
        certificateMap.clear();
        String serialNumber = newestCertificate.getSerialNo();
        certificateMap.put(serialNumber, certificate);
        return certificateMap;
    }


    /**
     * 使用v3key进行AES解密
     * @param associatedData
     * @param nonce
     * @param ciphertext
     * @return descrypt string
     * @throws GeneralSecurityException
     */
    public static String decryptToString(String associatedData, String nonce, String ciphertext) {
        try {
            byte[] associatedDataByte = associatedData.getBytes(StandardCharsets.UTF_8);
            byte[] nonceByte =nonce.getBytes(StandardCharsets.UTF_8);
            byte[] apiV3Key = WechatConstant.v3key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec key = new SecretKeySpec(apiV3Key, WechatConstant.AES_NAME);
            GCMParameterSpec spec = new GCMParameterSpec(WechatConstant.GCM_LENGTH, nonceByte);
            Cipher cipher = Cipher.getInstance(WechatConstant.AES_SETTING);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedDataByte);
            return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        } catch (Exception e){
            throw new CodeException("AES解密失败！");
        }
    }
}