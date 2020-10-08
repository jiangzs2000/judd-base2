package spring.shuyuan.judd.base.utils;

import com.alibaba.fastjson.JSONObject;
import spring.shuyuan.judd.base.model.DimDate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DimDateUtil {


    private final static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 日期+1
     */
    public static String getNowDate(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        String dayBefore = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        return dayBefore;
    }
    /**
     * 当前时间是星期几
     *
     */
    public static int getWeekDay(String specifiedDay) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week_of_year = c.get(Calendar.DAY_OF_WEEK);
        week_of_year = week_of_year - 1;
        if (week_of_year == 0) {
            week_of_year = 7;
        }
        return week_of_year;
    }
    /**
     * 当年的第几周
     */
    public static int getWeekofYear(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        int week = c.get(Calendar.WEEK_OF_YEAR);
        return week;
    }
    /**
     * 属于本年第几周
     */
    public static int getYearWeekIndex(String specifiedDay) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
    /**
     * 获取yyyymmdd
     */
    public static String yyyymmdd(String start_day,String patten) throws ParseException{
        Date date= new SimpleDateFormat("yyyyMMdd").parse(start_day);
        return new SimpleDateFormat(patten).format(date);
    }
    /**
     * 获取月多少天
     *
     */
    public static int getMDaycnt(String start_day) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(start_day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_MONTH);
        return i;
    }

    /**
     * 获取月旬 三旬: 上旬1-10日 中旬11-20日 下旬21-31日
     *
     */
    public static int getTenDay(String start_day) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(start_day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_MONTH);
        if (i < 11)
            return 1;
        else if (i < 21)
            return 2;
        else
            return 3;
    }
    /**
     * 获取月旬 三旬: 上旬1-10日 中旬11-20日 下旬21-31日
     *
     */
    public static String getTenDays(String start_day) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(start_day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_MONTH);
        if (i < 11)
            return "上旬";
        else if (i < 21)
            return "中旬";
        else
            return "下旬";
    }
    /**
     * 获取月旬 三旬: 每旬多少天
     *
     */
    public static int getTenDayscnt(String start_day) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(start_day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_MONTH);
        if (i < 11){
            return 10;
        } else if (i < 21) {
            return 10;
        }else{
            return  Integer.valueOf(getMonthEndTime(start_day).substring(6,8))-20;
        }
    }
    /**
     * 当前时间季度
     *
     */
    public static int getQuarter(String start_day) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(start_day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int dt = 0;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                dt=1;
            else if (currentMonth >= 4 && currentMonth <= 6)
                dt=2;
            else if (currentMonth >= 7 && currentMonth <= 9)
                dt=3;
            else if (currentMonth >= 10 && currentMonth <= 12)
                dt=4;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }
    /**
     * 当前时间季度天数
     *
     */
    public static int getQuarterCntday(String start_day) {
        int cnt_d=0;
        int q= getQuarter(start_day);
        if (q==1) {
            start_day = start_day.substring(0,4);
            start_day = getMonthEndTime(start_day+"0201");
            cnt_d = getMDaycnt(start_day);
            cnt_d=31+cnt_d+31;
        }else if (q == 2) {
            cnt_d=30+31+30;
        }else if (q == 3) {
            cnt_d=31+31+30;
        }else if (q == 4) {
            cnt_d=31+30+31;
        }
        return cnt_d;
    }
    /**
     * 当前时间年多少天
     *
     */
    public static int getYearCntday(String start_day) {
        int cnt_d=0;
        start_day = start_day.substring(0,4);
        start_day = getMonthEndTime(start_day+"0201");
        cnt_d = getMDaycnt(start_day);
        cnt_d=31+cnt_d+31+30+31+30+31+31+30+31+30+31;

        return cnt_d;
    }
    /**
     * 当前前/后半年天数
     *
     */
    public static int getHyearCntday(String start_day) {
        int cnt_d=0;
        int q= getHalfYear(start_day);
        if (q==1) {
            start_day = start_day.substring(0,4);
            start_day = getMonthEndTime(start_day+"0201");
            cnt_d = getMDaycnt(start_day);
            cnt_d=31+cnt_d+31+30+31+30;
        }else if (q == 2) {
            cnt_d=31+31+30+31+30+31;
        }
        return cnt_d;
    }
    /**
     * 当前时间前/后半年
     *
     */
    public static int getHalfYear(String start_day) {
        int dt = getQuarter(start_day);
        if (dt<=2) {
            dt=1;
        }else if (dt>2) {
            dt=2;
        }
        return dt;
    }
    public static String getHalfYears(String start_day) {
        int dt = getQuarter(start_day);
        if (dt<=2) {
            return "上半年";
        }
        return "下半年";
    }
    /**
     * 获得本月的开始时间
     *
     */
    public static String getMonthStartTime(String start_day) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(start_day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String dt = "";
        try {
            c.set(Calendar.DATE, 1);
            dt = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }
    /**
     * 本月的结束时间
     *
     */
    public static String getMonthEndTime(String start_day) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(start_day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String dt = "";
        try {
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            dt = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获得本周的第一天，周一
     *
     */
    public static String getWeekStartTime(String start_day,String pattern) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(start_day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
            c.add(Calendar.DATE, -weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00.000"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(pattern).format(c.getTime());
    }

    /**
     * 获得本周的最后一天，周日
     *
     */
    public static String getWeekEndTime(String start_day,String pattern) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(start_day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DATE, 8 - weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59.999"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(pattern).format(c.getTime());
    }
    /**
     * 获取所属旬开始时间
     *
     */
    public static String getTenDayStartTime(String start_day) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(start_day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int ten = getTenDay(start_day);
        if (ten == 1) {
            return getMonthStartTime(start_day);
        } else if (ten == 2) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMM11");
            return df.format(date);
        } else {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMM21");
            return df.format(date);
        }

    }

    /**
     * 获取所属旬结束时间
     *
     */
    public static String getTenDayEndTime(String start_day) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(start_day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int ten = getTenDay(start_day);
        if (ten == 1) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMM10");
            return df.format(date);
        } else if (ten == 2) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMM20");
            return df.format(date);
        } else {
            return getMonthEndTime(start_day);
        }
    }
    /**
     * 当前季度的开始时间
     *
     */
    public static String getQuarterStartTime(String start_day) throws ParseException {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(start_day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        String dt = "";
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 6);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            dt =  new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前季度的结束时间
     *
     */
    public static String getQuarterEndTime(String start_day) throws ParseException {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(start_day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        String dt = "";
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            dt = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 是否节假日
     *
     */
    public static int getJjr(String start_day,String j) throws Exception  {
        String f[] = { "0101" , "0405" , "0501" , "1001" };
        String sub=start_day.substring(4,8);
        if (!"1".equals(j)) {
            for (int k = 0 ; k < f.length ; k++ ) {
                if(f[k].equals(sub)){
                    return 1;
                }
            }
            return 0;
        }else{
            return 1;
        }
    }
    /**
     * 节假日名称
     *http://timor.tech/api/holiday?date=20190505
     */
    public static String getJjrname(String start_day) throws Exception  {
        String f[] = { "0101" , "0405" , "0501" , "1001" };
        String f1[] = { "元旦节" , "清明节" , "劳动节" , "国庆节" };
        String sub=start_day.substring(4,8);
        for (int k = 0 ; k < f.length ; k++ ) {
            if(f[k].equals(sub)){
                return f1[k];
            }
        }
        return "-";
    }

    /**
     * 循环计算
     */
    public static List<DimDate> anyDate(String start_day, String end_day) throws Exception {

        List<DimDate> res = new ArrayList<>();
        while (true) {
            start_day = getNowDate(start_day);
            if (start_day.equals(end_day)) {
                break;
            }else{
                String year_s=start_day.substring(0, 4);
                String month_s=start_day.substring(4, 6);
                String day_s=start_day.substring(6, 8);
                String day_long_desc = yyyymmdd(start_day,"yyyy年MM月dd日");// 日完整名称 yyyy年MM月dd日
                String day_medium_desc =  yyyymmdd(start_day,"dd日");// 日中等长度名 dd日
                String day_short_desc =  yyyymmdd(start_day,"yyyy-MM-dd");// 日短名 yyyy-MM-dd
                int  ws=getWeekofYear(start_day);
                String n_year=year_s;
                if (ws==1 && "12".equals(month_s)){
                    n_year=(Integer.parseInt(year_s)+1) + "";
                }
                String week_code = n_year + "W" + ws;// 周代码  2019W02
                String week_long_desc = n_year + "年第" + ws + "周";          //周完整名称String  2019年第02周
                String week_medium_desc  = "第" + ws + "周";                  //周中等长度名String  第02周
                String week_short_desc = n_year + "-W" + ws ;               //周短名String
                String week_name =  yyyymmdd(start_day,"EEEE");
                //旬代码String
                String ten_day_code =year_s + month_s + "X" + getTenDay(start_day);
                //旬完整名称String
                String ten_day_long_desc = year_s +"年"+ month_s + "月" + getTenDays(start_day);
                //旬中等长度名String
                String ten_day_medium_desc = getTenDays(start_day);
                //旬短名String
                String ten_day_short_desc = year_s +"-"+ month_s + "-X" + getTenDay(start_day);
                //月代码String
                String month_code = year_s + month_s;
                //月完整名称String
                String month_long_desc = year_s + "年" + month_s + "月";
                //月中等长度名String
                String month_medium_desc = month_s + "月";
                //月短名String
                String month_short_desc = year_s + "-" + month_s;
                //季代码String
                String qu=getQuarter(start_day)+"";
                String quarter_code = year_s +"Q"+qu;
                //季完整名称String
                String quarter_long_desc = year_s + "年第" + qu + "季度";
                //季中等长度名String
                String quarter_medium_desc = "第" + qu + "季度";
                //季短名String
                String quarter_short_desc = year_s + "-Q" + qu;
                //半年代码String
                String half_year_code =year_s + "H" + getHalfYear(start_day);
                //半年完整名称String
                String half_long_desc = year_s + "年" +getHalfYears(start_day);
                //半年中等长度名String
                String half_medium_desc = getHalfYears(start_day);
                //半年短名String
                String half_short_desc = year_s + "-H" + getHalfYear(start_day);
                //年代码String
                //年完整名称String
                String year_long_desc = year_s + "年";
                //年中等长度名String
                String year_medium_desc = year_s + "年";
                //年短名String
                //全部时间代码String
                String all_time_code = "ALL";
                //全部时间名称String
                String all_time_desc = "ALL_TIME";
                //日时间跨天String
                String day_timespan = "1";
                //结束日期String
                //周跨天数String
                String week_timespan = "7";
                //周结束日期String
                String week_end_date = getWeekEndTime(start_day,"yyyy-MM-dd");;
                //旬跨天数String
                String ten_day_timespan = getTenDayscnt(start_day)+"";
                //旬结束日期String
                String ten_day_end_date = getTenDayEndTime(start_day);
                //月跨天数String
                String month_timespan = getMonthEndTime(start_day).substring(6,8)+"";
                //月结束日期String
                String month_end_date = getMonthEndTime(start_day);
                //季跨天数String
                String quarter_timespan = getQuarterCntday(start_day)+"";
                //季结束日期String
                String quarter_end_date = getQuarterEndTime(start_day);
                //半年跨天数String
                String half_year_timespan = getHyearCntday(start_day)+"";
                //半年结束日期String
                String half_year_end_date = year_s + "0630";
                //年跨天数String
                String year_timespan = getYearCntday(start_day)+"";
                //年结束日期String
                String year_end_date = year_s + "1231";
                //周开始日期String
                String week_start_date = getWeekStartTime(start_day,"yyyyMMdd");
                //月开始时间String
                String month_start_date = getMonthStartTime(start_day);
                //季度开始时间String
                String quarter_start_date = getQuarterStartTime(start_day);
                //是否工作日:1.是 0.否
                String workday_flag = getWeekDay(start_day) <=5 ? "1" : "0" ;
                //是否周末:1.是 0.否
                String weekend_flag = getWeekDay(start_day) >5 ? "1" : "0";
                Date d=new Date();
                String y=new SimpleDateFormat("yyyy").format(d);//当前年
                int ys=Integer.parseInt(y)+1;//明年

                String load_time = "2019-06-26 12:00:00";  //加载时间
                String last_week_day = getWeekEndTime(start_day,"yyyy-MM-dd"); //周的最后一天String
                String last_month_day = yyyymmdd(getMonthEndTime(start_day),"yyyy-MM-dd"); //月的最后一天String
                DimDate dd = new DimDate();
                dd.setDay_code(start_day);
                dd.setDay_long_desc(day_long_desc);
                dd.setDay_medium_desc(day_medium_desc);
                dd.setDay_short_desc(day_short_desc);
                dd.setWeek_code(week_code);
                dd.setWeek_long_desc(week_long_desc);
                dd.setWeek_medium_desc(week_medium_desc);
                dd.setWeek_short_desc(week_short_desc);
                dd.setWeek_name(week_name);
                dd.setTen_day_code(ten_day_code);
                dd.setTen_day_long_desc(ten_day_long_desc);
                dd.setTen_day_medium_desc(ten_day_medium_desc);
                dd.setTen_day_short_desc(ten_day_short_desc);
                dd.setMonth_code(month_code);
                dd.setMonth_long_desc(month_long_desc);
                dd.setMonth_medium_desc(month_medium_desc);
                dd.setMonth_short_desc(month_short_desc);
                dd.setQuarter_code(quarter_code);
                dd.setQuarter_long_desc(quarter_long_desc);
                dd.setQuarter_medium_desc(quarter_medium_desc);
                dd.setQuarter_short_desc(quarter_short_desc);
                dd.setHalf_year_code(half_year_code);
                dd.setHalf_long_desc(half_long_desc);
                dd.setHalf_medium_desc(half_medium_desc);
                dd.setHalf_short_desc(half_short_desc);
                dd.setYear_code(year_s);
                dd.setYear_long_desc(year_long_desc);
                dd.setYear_medium_desc(year_medium_desc);
                dd.setAll_time_code(all_time_code);
                dd.setAll_time_desc(all_time_desc);
                dd.setDay_timespan(day_timespan);
                dd.setWeek_timespan(week_timespan);
                dd.setWeek_end_date(week_end_date);
                dd.setTen_day_timespan(ten_day_timespan);
                dd.setTen_day_end_date(ten_day_end_date);
                dd.setMonth_timespan(month_timespan);
                dd.setMonth_end_date(month_end_date);
                dd.setQuarter_timespan(quarter_timespan);
                dd.setQuarter_end_date(quarter_end_date);
                dd.setHalf_year_timespan(half_year_timespan);
                dd.setHalf_year_end_date(half_year_end_date);
                dd.setYear_timespan(year_timespan);
                dd.setYear_end_date(year_end_date);
                dd.setWeek_start_date(week_start_date);
                dd.setMonth_start_date(month_start_date);
                dd.setQuarter_start_date(quarter_start_date);
                dd.setWorkday_flag(workday_flag);
                dd.setWeekend_flag(weekend_flag);
                dd.setLoad_time(load_time);
                dd.setLast_week_day(last_week_day);
                dd.setLast_month_day(last_month_day);
                res.add(dd);
            }
        }
        return res;
    }


    public static void main(String[] args) throws Exception {
        String start_day = "20220212";/**开始日期*/
        String end_day = "20230101";/**结束日期*/
        ;//开始时间-结束时间-续跑ID
        anyDate(start_day,end_day).forEach(e -> {
            System.out.println(JSONObject.toJSONString(e));
        });
        ///System.out.println(getWorkDays("20130101"));
    }

}
