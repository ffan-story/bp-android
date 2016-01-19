package com.feifan.bp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Frank on 15/8/19.
 */
public class TimeUtil {

    private static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATE_FORMAT_DATE_MONTH = new SimpleDateFormat("yyyy-MM");
    private TimeUtil() {
    }

    /**
     * 获得今天日期
     */
    public static String getToday(){
        Calendar calendar = Calendar.getInstance();
        return DATE_FORMAT_DATE.format(calendar.getTime());
    }

    /**
     * 获得昨天日期
     */
    public static String getYesterday(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        return DATE_FORMAT_DATE.format(calendar.getTime());
    }

    /**
     * 获取上个月份
     * @return
     */
    public static String getLastMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-1);
        return DATE_FORMAT_DATE_MONTH.format(calendar.getTime());
    }

    /**
     * 获取上上个月份
     * @return
     */
    public static String getLast2Month(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-2);
        return DATE_FORMAT_DATE_MONTH.format(calendar.getTime());
    }

    /**
     * 比较两个日期大小
     * @param date1
     * @param date2
     * @return true date1大于date2
     *        false date1小于等于date2
     */
    public static boolean compare_date(String date1,String date2){

        try{

            Date dt1 = DATE_FORMAT_DATE.parse(date1);
            Date dt2 = DATE_FORMAT_DATE.parse(date2);

            if (dt1.getTime()>dt2.getTime()){
                return true;
            }else{
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获得两个日期的日期差
     * @param date1
     * @param date2
     * @return
     */

    public static int getIntervalDays(String date1,String date2){

        int days = 0;
        try{
            Date dt1 = DATE_FORMAT_DATE.parse(date1);
            Date dt2 = DATE_FORMAT_DATE.parse(date2);

            if (dt1.getTime()>dt2.getTime()){
                days = (int)((dt1.getTime() - dt2.getTime())/86400000);
            }else{
                days = (int)((dt2.getTime() - dt1.getTime())/86400000);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    // 字符串类型日期转化成date类型
    public static Date strToDate(String style, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String dateToStr(String style, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(date);
    }
}
