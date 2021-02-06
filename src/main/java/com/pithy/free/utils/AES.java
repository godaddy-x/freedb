package com.pithy.free.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * AES 加密工具类
 */
public class AES {

    private static final Logger log = LoggerFactory.getLogger(AES.class);
    private static final String s = "82,49,77,102,84,86,69,81,89,48,84,111,83,108,99,110,100,83,68,119,89,66,85,105,89,83,52,65,81,120,89,105,82,104,107,50,83,82,103,65,85,67,77,111,84,83,73,82,83,81,69,79,80,85,69,50,83,105,102,119,88,86,111,86,99,85,77,102,72,119,81,78,100,81,80,104,99,68,81,74,96,85,103,86,86,108,81,74,83,46,48,73,99,83,84,47,80,82,77,68,84,106,77,49,80,47,103,120,81,81,84,46,81,82,89,82,80,86,99,46,87,106,110,118,73,65,81,106,72,83,52,82";

    private static final char[] KEY_ARR = getKey().toCharArray();
    private static final String KEY_ALGORITHM = "AES";
    private static final String KEY_MODE = "SHA1PRNG";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
            byte[] result = cipher.doFinal(byteContent);
            return parseByte2HexStr(result);
        } catch (Exception e) {
            log.error("AES加密失败", e);
            return null;
        }
    }

    /**
     * AES 解密操作
     *
     * @param content  密文字符串
     * @param password 加密密码
     * @return 明文数据
     */
    public static String decrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            byte[] result = cipher.doFinal(parseHexStr2Byte(content));
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("AES解密失败", e);
            return null;
        }
    }

    // 生成加密秘钥
    private static SecretKeySpec getSecretKey(final String password) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom secureRandom = SecureRandom.getInstance(KEY_MODE);
        secureRandom.setSeed(password.getBytes(StandardCharsets.UTF_8));
        kg.init(128, secureRandom);
        SecretKey secretKey = kg.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }

    // 将二进制转换成16进制
    public static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    // 将16进制转换为二进制
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static String genKey(int... index) {
        if (index == null || index.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i : index) {
            if (i >= 99) {
                sb.append(KEY_ARR[91]);
            } else {
                sb.append(KEY_ARR[i]);
            }
        }
        return sb.toString();
    }

    private static String getKey() {
        StringBuffer sb = new StringBuffer();
        String[] sp = s.split(",");
        for (int i = 0; i < sp.length; i++) {
            int a = Integer.parseInt(sp[i]);
            if (i % 2 == 0) {
                a = a + 1;
            } else {
                a = a + 2;
            }
            sb.append((char) a);
        }
        return sb.toString();
    }

}