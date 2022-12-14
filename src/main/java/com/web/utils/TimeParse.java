package com.web.utils;

import javax.persistence.Convert;
import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeParse {
    public static void main(String[] args){
//        stampToDate("1617672852000");
    }

    /*
     * 将时间戳转换为时间
     */
    public static   String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    public String days1Before(Date now){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date =calendar.getTime();
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
        String begin_date  = formate.format(date);
        return begin_date;
    }
    public String days7Before(Date now){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date date =calendar.getTime();
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
        String begin_date  = formate.format(date);
        return begin_date;
    }


    public  Date parseTimeString2Date(String timeString) {
        if ((timeString == null) || (timeString.equals(""))) {
            return null;
        }
        Date date = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = new Date(dateFormat.parse(timeString).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public  String convertDate2String(Date date, String pattern) {
        if (date == null)
            return null;
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
    public  String  getFullTime(String timeString) {
        String timeStr = convertDate2String(parseTimeString2Date(timeString), "yyyy-MM-dd HH:mm:ss");
        return timeStr.substring(0, 16);
    }
    public  int getYear(String timeString) {
        String timeStr = convertDate2String(parseTimeString2Date(timeString), "yyyy-MM-dd HH:mm:ss");
        return Integer.parseInt(timeStr.substring(0, 4));
    }

    public  int getMonth(String timeString) {
        String timeStr = convertDate2String(parseTimeString2Date(timeString), "yyyy-MM-dd HH:mm:ss");
        return Integer.parseInt(timeStr.substring(5, 7));
    }

    public  int getDay(String timeString) {
        String timeStr = convertDate2String(parseTimeString2Date(timeString), "yyyy-MM-dd HH:mm:ss");
        return Integer.parseInt(timeStr.substring(8, 10));
    }

    public   int getHour(String timeString) {
        String timeStr = convertDate2String(parseTimeString2Date(timeString), "yyyy-MM-dd HH:mm:ss");
        return Integer.parseInt(timeStr.substring(11, 13));
    }

    public  int getMinute(String timeString) {
        String timeStr = convertDate2String(parseTimeString2Date(timeString), "yyyy-MM-dd HH:mm:ss");
        return Integer.parseInt(timeStr.substring(14, 16));
    }

    public   int getSecond(String timeString) {
        String timeStr = convertDate2String(parseTimeString2Date(timeString), "yyyy-MM-dd HH:mm:ss");
        return Integer.parseInt(timeStr.substring(17, 19));
    }
    public String getTimestamp() {
        String unixTimestamp = Long.toString(System.currentTimeMillis()/1000L);
        return unixTimestamp;
    }
}
