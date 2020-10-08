package spring.shuyuan.judd.base.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Sting
 * create 2018/11/26

 **/
public class DateformatUtils {

    private static DateFormat dateFormat;

    /**
     * 格式化日期
     *
     * @param date
     */
    public static String formatDate(Date date, String pattern) {
        dateFormat = new SimpleDateFormat(pattern);
        String formatDate = dateFormat.format(date);
        return formatDate;
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @Exception 发生异常
     */
    public static int dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        if(c.get(Calendar.DAY_OF_WEEK) == 1){
            dayForWeek = 7;
        }else{
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    public static void main(String[] args) {
        System.out.println(formatDate(new Date(),"yyyyMMdd"));
        try {
            int weekday =dayForWeek(formatDate(new Date(),"yyyy-MM-dd"));
            System.out.println(weekday);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
