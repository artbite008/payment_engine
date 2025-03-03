package com.siupay.utils.risk;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 
 * @date 2022年03月01日
 */
@UtilityClass
@Slf4j
public class EncryptUtils {

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";

    /**
     * 默认编码
     */
    private static final String CHARSET = "utf-8";

    /**
     * DES加密字符串
     */
    public static String encrypt(String cipherText, String securityKey) {
        if (StringUtils.isBlank(cipherText)) {
            return null;
        }
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(securityKey.getBytes(StandardCharsets.UTF_8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey secretKey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            byte[] bytes = cipher.doFinal(cipherText.getBytes(CHARSET));
            return new String(Base64.getEncoder().encode(bytes));
        } catch (Exception e) {
            log.error("DES加密[{}]失败", cipherText, e);
        }
        return null;
    }

    /**
     * 解密
     * 
     * @param cipherText
     * @param securityKey
     * @return
     */
    public static String decrypt(String cipherText, String securityKey) {
        if (StringUtils.isBlank(cipherText)) {
            return cipherText;
        }
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(securityKey.getBytes(StandardCharsets.UTF_8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey secretKey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText.getBytes(StandardCharsets.UTF_8))),
                    StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("DES解密[{}]失败", cipherText, e);
        }
        return null;
    }
}
