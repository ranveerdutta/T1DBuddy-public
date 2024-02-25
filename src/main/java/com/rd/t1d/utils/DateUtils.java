package com.rd.t1d.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateUtils {

    public static ZonedDateTime getCurrentZonedDateTime(){
        return ZonedDateTime.now();
    }

    public static int yearDiffFromToday(Date date){
        Date now = new Date();
        Date diff = new Date(now.getTime() - date.getTime());
        Calendar cal=Calendar.getInstance();
        cal.setTime(diff);
        return cal.get(Calendar.YEAR) - 1970;
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }


    public static Date parseDate(String format, String date){
        try{
            return new SimpleDateFormat(format).parse(date);
        }catch(Exception e){
            log.error("error while parsing date: " + date);
            return null;
        }
    }

    public static long minDiffFromToday(Date date){
        Date now = new Date();
        long diff = now.getTime() - date.getTime();
        return diff / (60 * 1000);
    }

    public static long dayDiffFromToday(long date){
        Date now = new Date();
        long diff = now.getTime() - date;
        return diff / (24 * 60 * 60 * 1000);
    }

}
