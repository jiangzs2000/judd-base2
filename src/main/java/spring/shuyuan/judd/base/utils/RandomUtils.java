package spring.shuyuan.judd.base.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author Sting
 * create 2018/11/26

 **/
public class RandomUtils {
    private static char[] str = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static Random random = new Random();
    private static DateFormat format = new SimpleDateFormat("yyyyMMdd");

    /**
     * 生成order_id规则（时间戳yyyyMMddHHmmss+8位随机字符）
     */
    public static String getOrderIdByTime() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyMMddHHmmssSSS");
        StringBuffer result = new StringBuffer(sdf.format(new Date()));
        Random random=new Random();
        for(int i=0;i<3;i++){
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

    /**
     * 生成order_id规则（时间戳MMddHHmmss+8位随机字符）分账订单
     */
    public static String getRoutingOrderIdByTime() {
        SimpleDateFormat sdf=new SimpleDateFormat("MMddHHmmssSSS");
        StringBuffer result = new StringBuffer(sdf.format(new Date())).append("-");
        Random random=new Random();
        for(int i=0;i<5;i++){
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

    /**
     * 生成mer_trace规则（时间戳yyyyMMddHHmmss+6位随机字符）
     */
    public static String getMerTraceIdByTime() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyMMddHHmmssSSS");
        StringBuffer result = new StringBuffer(sdf.format(new Date()));
        Random random=new Random();
        for(int i=0;i<8;i++){
            result.append(random.nextInt(10));
        }
        return  result.toString();
    }


    /**
     * 生成order_id规则（时间戳yyyyMMdd+8位随机字符）
     *
     * @param length
     */
    public static String generateOrderId(int length) {
        return generateRandomString(length).insert(0, format.format(new Date())).toString();
    }

    /**
     * 生成trade_voucher_no规则（时间戳yyyyMMdd+8位随机字符）
     * 交易凭证号
     * @param length
     */
    public static String generatetradeVoucherNo(int length) {
        return generateRandomString(length).insert(0, format.format(new Date())).toString();
    }

    /**
     * 生成定长的字符串
     *
     * @param length
     */
    public static StringBuffer generateRandomString(int length) {
        //清空stringbuffer内容
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(str[random.nextInt(str.length)]);
        }
        return stringBuffer;
    }

    public static void main(String[] args) {
        String ss =getRoutingOrderIdByTime();
        System.out.println(ss);
        ss = generateRandomString(8).toString();
        System.out.println(ss);
        ss = generatetradeVoucherNo(8);
        System.out.println(ss);
    }



}
