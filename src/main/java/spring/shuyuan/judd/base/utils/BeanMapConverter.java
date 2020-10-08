package spring.shuyuan.judd.base.utils;


import lombok.Data;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class BeanMapConverter {
    private static final char UNDERLINE = '_';


    public static Map<String, String> bean2MapWithCamel2underscore(Object bean) {
        BeanMap map = new BeanMap(bean);
        Map<String, String> res = new HashMap<>();
        for (Object key : map.keySet()) {
            if (!key.toString().equals("class") && map.get(key) != null) { //BeanMap在传入参数bean所有字段都为空的情况下，会在map中增加("class",bean.class)的记录
                res.put(camelToUnderline((String) key), map.get(key).toString());
            }
        }
        return res;
    }

    public static <T> T map2BeanWithUnderscore2Camel(Map<String, String> properties, Class<T> beanClass) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Map<String, Object> map = new HashMap<>();
        for (String key : properties.keySet()) {
            map.put(underlineToCamel(key), properties.get(key));
        }
        T bean = beanClass.newInstance();
        BeanUtils.populate(bean, map);
        //BeanUtils.populate 有bug，当字段为pAgreementId时，反射失败
        try {
            if (map.get("pAgreementId") != null) {
                Field pAgreementId = bean.getClass().getDeclaredField("pAgreementId");
                pAgreementId.setAccessible(true);
                pAgreementId.set(bean, map.get("pAgreementId"));
            }
        }catch(Exception e){

        }
        return bean;
    }

    //驼峰转下划线
    private static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
            }
            sb.append(Character.toLowerCase(c));  //统一都转小写
        }
        return sb.toString();
    }

    //下划线转驼峰
    private static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        Boolean flag = false; // "_" 后转大写标志,默认字符前面没有"_"
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                flag = true;
                continue;   //标志设置为true,跳过
            } else {
                if (flag == true) {
                    //表示当前字符前面是"_" ,当前字符转大写
                    sb.append(Character.toUpperCase(param.charAt(i)));
                    flag = false;  //重置标识
                } else {
                    sb.append(Character.toLowerCase(param.charAt(i)));
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException {
        System.out.println(underlineToCamel("p_agreement_id"));
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("mer_no", "C7p7jD3Am7");
                put("version", "1.0");
                put("order_no", "988781cbf60947399c1d7b9e202ed166");
                put("mer_date", "20200921");
                put("merchant_no", "CA5UPc1Jdp");
                put("merchant_type", "3");
                put("p_agreement_id", "mannully-insert-0003");
                put("amount", "10000");
            }
        };
        System.out.println(map2BeanWithUnderscore2Camel(map,WithdrawOrder.class));
    }

    /*
        public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InstantiationException {
            Person p = new Person();
            p.nameInEn = "a";
            p.ageInMonth = 10;
            p.heightInInch = null;
            p.setSex("m");
            p.setHumanKind("black");

            System.out.println(bean2MapWithCamel2underscore(p));
            Map<String, String> m = new HashMap<>();
            m.put("test", "test");
            m.put("height_in_inch", "100");
            m.putAll(bean2MapWithCamel2underscore(p));

            System.out.println(String.format("m:%s", m));

            Human h = map2BeanWithUnderscore2Camel(m, Person.class);
            System.out.println(h);

            System.out.println("******************************************");
            System.out.println(bean2MapWithCamel2underscore(h));
        }

        @Data
        public static class Human {
            private String sex;
            private String humanKind;
        }

        @Data
        public static class Person extends Human {
            private String nameInEn;
            private Integer ageInMonth;
            private Long heightInInch;
        }
    */
    @Data
    public static class WithdrawOrder {

        /**
         * 支付协议号, 客户非首次支付、子商户支付时必填
         */
        public String pAgreementId;
        private String orderNo;
        private String date;
        private String notifyUrl;
        private String merNo;
        private String merOrderNo;
        private String merDate;
        /**
         * 要提现的商户
         */
        private String merchantNo;
        /**
         * 按子商户类型来传入，取值范围： 1：个人商户
         * 2：个体工商户
         * 3：企业商户
         */
        private Integer merchantType;
        private String routingChannel;

        private Long amount;

        private String remark;
        /**
         * saas平台提现手续费，分（平台自己计算的结果）
         */
        private Long platFeeAmt;
        /**
         * 渠道提现手续费，分（平台自己计算的结果）
         */
        private Long channelFeeAmt;

        private String tradeNo;

        private String merCheckDate;
        /**
         * 渠道返回的提现手续费金额, 分
         */
        private Long channelRetFeeAmt;
        /**
         * I：初始，P：处理中，S:成功，F：失败
         */
        private String state;

        private Long createUser;

        private Timestamp createTime;

        private Timestamp updateTime;
        /**
         * 记录数据状态 1正常 0无效
         */
        private Integer status;
    }
}
