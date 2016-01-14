package com.xl.tool.util;

/**
 * Created by albert on 2016-1-14.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Dlen on 2015/8/19.
 * 日期工具类
 */
public class DateUtil {

    /**
     * 获取两个日期之间的天数
     *
     * @param date1 开始日期
     * @param date2 结束日期
     * @return
     */
    private static SimpleDateFormat sf = new SimpleDateFormat();

    /**
     * 计算两个日期相差的天数
     *
     * @param date1 开始时间
     * @param date2 结束时间
     * @return
     */
    public static int getBetweenDay(Date date1, Date date2) {
        Calendar d1 = new GregorianCalendar();
        d1.setTime(date1);
        Calendar d2 = new GregorianCalendar();
        d2.setTime(date2);
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
//          d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 计算两个date类型相隔的天数
     *
     * @param strat
     * @param end
     * @return
     */
    public static int poor2day(Date strat, Date end) throws ParseException {
        if (strat.getTime() > end.getTime()) {
            return 0;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String strStart = format.format(strat);
        String strEnd = format.format(end);
        Date tempStart = format.parse(strStart);
        Date tempEnd = format.parse(strEnd);

        long day = (tempEnd.getTime() - tempStart.getTime()) / (24 * 60 * 60 * 1000);
        return (int) day;
    }


    public static Date formateStringToDate(String src, String format) {
        sf.applyPattern(format);
        try {
            return sf.parse(src);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将时间转化为字符串
     * @param date
     * @return
     */
    public static String praseDateToSring(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = simpleDateFormat.format(date);
        return str;
    }


    public static String formateDateToString(Date date, String format) {
        sf.applyPattern(format);
        try {
            return sf.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getLastDayOfThisMonth() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return ca.get(Calendar.DAY_OF_MONTH);
    }


    //获得某天的开始时间或结束时间
    public static String getOneDayFirstAndLast(Date date,int type){
        try{
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            if(type == 1){//一天的开始时间
                return sf.format(calendar.getTime());
            }else{//一天的结束时间
                calendar.add(Calendar.DAY_OF_MONTH,1);
                calendar.add(Calendar.SECOND,-1);
                return sf.format(calendar.getTime());
            }
        }catch (Exception e){
            return null;
        }
    }


    public static Date addOneMonth(Date now){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MONTH,1);
        return calendar.getTime();
    }

}