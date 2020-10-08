package spring.shuyuan.judd.base.utils;

import java.util.UUID;

/**
 * @author Sting
 * create 2018/11/26

 **/
public class UUidUtils {

    /**
     * 生成uuid工具类
     *
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        System.out.println(generateUuid().length());
    }
}
