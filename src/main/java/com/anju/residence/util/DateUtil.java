package com.anju.residence.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author cygao
 * @date 2020/12/1 19:53
 **/
public class DateUtil {

  /**
   * 耗电日志上传时间间隔
   */
  public static final int UPLOAD_MINUTE_INTERVAL = 5;

  public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static Calendar getFirstSecondCalender(Calendar calendar) {
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);

    return calendar;
  }

  public static Calendar getLastSecondCalender(Calendar calendar) {
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);

    return calendar;
  }

  public static Date getFirstSecondDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    return getFirstSecondCalender(calendar).getTime();
  }

  public static Date getLastSecondDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    return getLastSecondCalender(calendar).getTime();
  }

  public static Date getTodayFirstSecondDate() {
    return getFirstSecondDate(now());
  }

  public static Date getTodayLastSecondDate() {
    return getLastSecondDate(now());
  }

  public static Date getTomorrowFirstSecondDate() {
    Calendar now = Calendar.getInstance();
    now.add(Calendar.DATE, 1);

    return getFirstSecondCalender(now).getTime();
  }

  public static Date getDateMinusUploadInterval() {
    return getDateMinusMinute(2 * UPLOAD_MINUTE_INTERVAL - 1);
  }

  public static Date getDateMinusMinute(int minute) {
    Calendar now = Calendar.getInstance();
    now.add(Calendar.MINUTE, -minute);

    return now.getTime();
  }

  public static Date getDateMinusHour(int hour) {
    Calendar now = Calendar.getInstance();
    now.add(Calendar.HOUR, -hour);

    return now.getTime();
  }

  /**
   * 获取值为系统当前时间的Date对象
   */
  public static Date now() {
    return new Date(System.currentTimeMillis());
  }
}
