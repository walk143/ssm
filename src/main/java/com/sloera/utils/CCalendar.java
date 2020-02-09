package com.sloera.utils;

import java.util.Calendar;
import java.util.Date;

public class CCalendar {
    public static String second = "yyyy-MM-dd HH:mm:ss";
    public static String day = "yyyy-MM-dd";

    /*
     * @Description 获取两个日期的工作日，取小、取整
     * @param startDate:开始日期
     * @param endDate:结束日期
     * @Return 日期天数差。
     * @Author SloeraN
     * @Date 2019/12/22 19:31
     */
    public static int getDayByTwoDate(Date startDate, Date endDate) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(startDate);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(endDate);
        long total = c2.getTimeInMillis() - c1.getTimeInMillis();
        return (int) (total / (1000 * 60 * 60 * 24));
    }

    public static int getAge(Date startDate) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(startDate);
        Calendar c2 = Calendar.getInstance();
        if (c2.before(c1)) {
            throw new IllegalArgumentException("输入的日期晚于当前日期");
        }
        int yearNow = c2.get(Calendar.YEAR);//当前年份
        //int monthNow = c2.get(Calendar.MONTH);//当前月份
        //int dayOfMonthNow = c2.get(Calendar.DAY_OF_MONTH);//当前天
        int yearBirth = c1.get(Calendar.YEAR);//输入年份
        //int monthBirth = c1.get(Calendar.MONTH);//输入月份
        //int dayOfMonthBirth = c1.get(Calendar.DAY_OF_MONTH);//输入天
        int age = yearNow - yearBirth;
        c1.set(Calendar.YEAR,yearNow);
        if(c1.after(c2)){
            age--;//输入日期当前年份时，晚于当前日期
        }
        //if (monthNow <= monthBirth) {
        //    if (monthNow == monthBirth) {
        //        if (dayOfMonthNow <= dayOfMonthBirth) {
        //            if (dayOfMonthNow == dayOfMonthBirth) {
        //
        //            } else {
        //                age--;//天
        //            }
        //        }
        //    } else {
        //        age--;//月
        //    }
        //}
        return age;
    }
}
