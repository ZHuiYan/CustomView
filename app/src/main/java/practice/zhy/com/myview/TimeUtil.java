package practice.zhy.com.myview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    public static String PATTERN1 = "MM-dd HH:mm";
    public static String PATTERN2 = "yyyy-MM-dd HH:mm:ss";
    public static String PATTERN3 = "yyyy-MM-dd";
    public static String PATTERN4 = "yyyyMMdd";
    public static String PATTERN5 = "yyyyMMdd HH:mm";
    public static String PATTERN6 = "yyyy-MM-dd HH:mm";
    public static String PATTERN7 = "yyyy年MM月dd日";
    public static String PATTERN9 = "MM-dd";
    public static String PATTERN8 = "yyyyMMddHHmm";
    public static String PATTERN10 = "MM月dd日";


    //九点半的秒数
    private static final long Half_Past_Nine=9 * 3600+30 * 60;
    //九点一刻的秒数
    private static final long Half_Past_Nine_Quarter=9 * 3600 +15 * 60;
    //11点半的秒数,晚一分钟结束
    private static final long Half_Past_Eleven =11* 3600 +30 * 60+60;
    //13点的秒数
    private static final long Thirteen =13* 3600;
    //15点一刻的秒数,晚一分钟结束
    private static final long Fifteen_Quarter =15* 3600+15*60;
    //15点的秒数
    private static final long Fifteen=15 * 3600;
    //8点的秒数
    private static final long Eight=8 * 3600;

    public static boolean isHoliday=false;



    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(PATTERN4).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat(PATTERN4)
                .format(c.getTime());
        return dayAfter;
    }


    /**
     * 获取系统时间
     *
     * @param currentTimeMillis
     * @return
     */
    public static String getSystemTime(long currentTimeMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN2, Locale.getDefault());
        Date date = new Date(currentTimeMillis);
        return formatter.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN1, Locale.getDefault());
        return formatter.format(new Date());
    }
    public static String getTime(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(new Date());
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN3, Locale.getDefault());
        return formatter.format(new Date());
    }



    /**
     * 获取当前日期(带格式)
     *
     * @return
     */
    public static String getDate(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(new Date());
    }

    public static String formatStr(long time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date(time);
        return formatter.format(date);
    }

    public static String formatStr1(String time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        Date d = null;
        try {
            d = formatter.parse(time);   // 将给定的字符串中的日期提取出来
        } catch (Exception e) {            // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace();       // 打印异常信息
        }
        if (d == null){
            return "";
        }else {
            return formatter.format(d);
        }
    }
    public static String formatStr2(String time, String format){
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN2, Locale.getDefault());
        SimpleDateFormat formatter1 = new SimpleDateFormat(format, Locale.getDefault());
        Date d = null;
        try {
            d = formatter.parse(time);   // 将给定的字符串中的日期提取出来
        } catch (Exception e) {            // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace();       // 打印异常信息
        }
        if (d == null){
            return "";
        }else {
            return formatter1.format(d);
        }
    }
    public static String formatStr2(String time, String format1,String format2){
        SimpleDateFormat formatter = new SimpleDateFormat(format2, Locale.getDefault());
        SimpleDateFormat formatter1 = new SimpleDateFormat(format1, Locale.getDefault());
        Date d = null;
        try {
            d = formatter.parse(time);   // 将给定的字符串中的日期提取出来
        } catch (Exception e) {            // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace();       // 打印异常信息
        }
        if (d == null){
            return "";
        }else {
            return formatter1.format(d);
        }
    }
    //股吧显示时间，如果为本年，则只显示"MM-dd HH:mm"，否则显示 "yyyy-MM-dd HH:mm"
    public static String getStr(String time,String format){
        Calendar a=Calendar.getInstance();
        int year = a.get(Calendar.YEAR);
        String result = formatStr2(time,"yyyy");
        if (result.equals(year + "")){
            return formatStr2(time,format);
        }else {
            return formatStr2(time,PATTERN3);
        }
    }
    public static String date2String(Date date, String format){
        String time;
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        time = formatter.format(date);
        return time;
    }
    /**
     * 格式化时间
     *
     * @param
     * @return
     */
    public static Date getStringToDate(String format, String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        Date date = null;
        try {
            date = formatter.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long getStringToLong(String format, String timeStr) {
        return getStringToDate(format, timeStr).getTime();
    }

    /**
     * 是否为午间
     * @return
     */
    public static boolean isNoonTime() {
        int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (week == Calendar.SATURDAY || week == Calendar.SUNDAY) {
            //排除周日和周六
            return false;
        } else {
            Date dtnow = new Date();
            //当前的秒数
            long now = dtnow.getHours() * 3600
                    + dtnow.getMinutes() * 60
                    + dtnow.getSeconds();
            if (now > Half_Past_Eleven && now < Thirteen) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 是否为早间
     * @return
     */
    public static boolean isMorningTime() {
        int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (week == Calendar.SATURDAY || week == Calendar.SUNDAY) {
            //排除周日和周六
            return false;
        } else {
            Date dtnow = new Date();
            //当前的秒数
            long now = dtnow.getHours() * 3600
                    + dtnow.getMinutes() * 60
                    + dtnow.getSeconds();
            if (now > Eight && now < Half_Past_Nine_Quarter) {
                return true;
            } else {
                return false;
            }
        }
    }


    public static boolean isTransactionTime() {
        if (isHoliday){
            return false;
        }
        int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (week == Calendar.SATURDAY || week == Calendar.SUNDAY) {
            //排除周日和周六
            return false;
        } else {
            Date dtnow=new Date();
            //当前的秒数
            long now=dtnow.getHours() * 3600
                    + dtnow.getMinutes() * 60
                    + dtnow.getSeconds();
            if ((now >=Half_Past_Nine_Quarter  && now <= Half_Past_Eleven) || (now >= Thirteen && now <= Fifteen_Quarter)) {
                return true;
            } else {
                return false;
            }
        }

    }

    /**
     * 是否为闪烁时刻
     * @return
     */
    public static boolean isTwinkleTime() {
        if (isHoliday){
            return false;
        }
        int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (week == Calendar.SATURDAY || week == Calendar.SUNDAY) {
            //排除周日和周六
            return false;
        } else {
            Date dtnow=new Date();
            //当前的秒数
            long now=dtnow.getHours() * 3600
                    + dtnow.getMinutes() * 60
                    + dtnow.getSeconds();
            if ((now >=Half_Past_Nine  && now <= Half_Past_Eleven) || (now >= Thirteen && now <= Fifteen)) {
                return true;
            } else {
                return false;
            }
        }

    }



}
