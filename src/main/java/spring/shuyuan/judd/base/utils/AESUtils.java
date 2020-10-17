package spring.shuyuan.judd.base.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密解密工具类
 *
 * @author Sting
 */
@Slf4j
public class AESUtils {

    private static final String defaultCharset = "UTF-8";
    private static final String KEY_AES = "AES";
    private static final String KEY = "7386072b1f94fdd7acaae83cd0f0f1c1";

    /**
     * 加密
     *
     * @param data 需要加密的内容
     * @param key  加密密码
     */
    public static String encrypt(String data, String key) {
        if (StringUtils.isBlank(key)) {
            key = KEY;
        }
        return doAES(data, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     *
     * @param data 待解密内容
     * @param key  解密密钥
     */
    public static String decrypt(String data, String key) {
        if (StringUtils.isBlank(key)) {
            key = KEY;
        }
        String s = doAES(data, key, Cipher.DECRYPT_MODE);
//        return JSONObject.parseObject(s).toJSONString();
        return s;
    }

    /**
     * 加解密
     *
     * @param data 待处理数据
     * @param key  密钥
     * @param mode 加解密mode
     */
    private static String doAES(String data, String key, int mode) {
        try {
            if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
                return null;
            }
            //判断是加密还是解密
            boolean encrypt = mode == Cipher.ENCRYPT_MODE;
            byte[] content;
            //true 加密内容 false 解密内容
            if (encrypt) {
                content = data.getBytes(defaultCharset);
            } else {
                content = parseHexStr2Byte(data);
            }

            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("ASCII"), KEY_AES);
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(mode, skeySpec);// 初始化
            byte[] result = cipher.doFinal(content);
            if (encrypt) {
                //将二进制转换成16进制
                return parseByte2HexStr(result);
            } else {
                return new String(result, defaultCharset);
            }
        } catch (Exception e) {
            log.error("AES 密文处理异常", e);
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     */
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

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     */
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

    public static void main(String[] args) throws Exception {
//        String content = "18320188000058905";
//        System.out.println(content.length());
//        System.out.println("加密前：" + content);
//        System.out.println("加密密钥和解密密钥：" + KEY);
//        String encrypt = encrypt(content, KEY);
//        System.out.println("加密后：" + encrypt);
        String decrypt = decrypt("D76AAA5DE90AC4C964DC8B33BC06BAD7C90B932ED3C8FAE9608ACB7BB01B67B8", KEY);
        System.out.println("解密后：" + decrypt);
        System.out.println( encrypt("!sIk%H!gDq1NbhlE",null));

        String encryptedPwd = encrypt("test123", null);
        System.out.println(encryptedPwd);
        decrypt = decrypt(encryptedPwd, null);
        System.out.println(decrypt);

    }
}
