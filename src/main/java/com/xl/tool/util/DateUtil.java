
package com.xl.tool.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    public static final long HOUR_MILLS = 3600000L;
    public static final long MINUTE_MILLS = 60000L;
    public static final long DAY_MILLS = 86400000L;
    public static final long MONTH_MILLS = 2592000000L;
    public static final long WEEK_MILLS = 604800000L;
    public static final SimpleDateFormat YYYY_MM_DD_HH_MM_SS_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sf = new SimpleDateFormat();

    public DateUtil() {
    }

    public static int getBetweenDay(Date date1, Date date2) {
        GregorianCalendar d1 = new GregorianCalendar();
        d1.setTime(date1);
        GregorianCalendar d2 = new GregorianCalendar();
        d2.setTime(date2);
        int days = d2.get(6) - d1.get(6);
        int y2 = d2.get(1);
        if(d1.get(1) != y2) {
            do {
                days += d1.getActualMaximum(6);
                d1.add(1, 1);
            } while(d1.get(1) != y2);
        }

        return days;
    }

    public static int poor2day(Date strat, Date end) throws ParseException {
        if(strat.getTime() > end.getTime()) {
            return 0;
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String strStart = format.format(strat);
            String strEnd = format.format(end);
            Date tempStart = format.parse(strStart);
            Date tempEnd = format.parse(strEnd);
            long day = (tempEnd.getTime() - tempStart.getTime()) / 86400000L;
            return (int)day;
        }
    }

    public static Date formateStringToDate(String src, String format) {
        sf.applyPattern(format);

        try {
            return sf.parse(src);
        } catch (Exception var3) {
            return null;
        }
    }

    public static String praseDateToSring(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = simpleDateFormat.format(date);
        return str;
    }

    public static String formateDateToString(Date date, String format) {
        sf.applyPattern(format);

        try {
            return sf.format(date);
        } catch (Exception var3) {
            return null;
        }
    }

    public static int getLastDayOfThisMonth() {
        Calendar ca = Calendar.getInstance();
        ca.set(5, ca.getActualMaximum(5));
        return ca.get(5);
    }

    public static String getOneDayFirstAndLast(Date date, int type) {
        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            if(type == 1) {
                return e.format(calendar.getTime());
            } else {
                calendar.add(5, 1);
                calendar.add(13, -1);
                return e.format(calendar.getTime());
            }
        } catch (Exception var4) {
            return null;
        }
    }

    public static Date addOneMonth(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(2, 1);
        return calendar.getTime();
    }
}