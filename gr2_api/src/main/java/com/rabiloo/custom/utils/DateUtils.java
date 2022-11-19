package com.rabiloo.custom.utils;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class DateUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    public static final Map<String, Integer> MAP_DYNASTY_JP_AND_YEAR = ImmutableMap.of(
            "M", 1868,
            "T", 1912,
            "S", 1926,
            "H", 1989,
            "R", 2019);

    public static String plusOneDayToDateString(String dateString) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

            Date dt = simpleDateFormat.parse(dateString);
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, 1);
            return simpleDateFormat.format(c.getTime());
        } catch (ParseException e) {
            LOGGER.error("Error parse date format yyy/MM/dd", e);
            return null;
        }
    }

    public static String getCurrentDateStr(String pattern) {
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        return format.format(ldt);
    }

    public static Date getFirstDayOfCurrentWeek() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return c.getTime();
    }

    ;

    public static Date getLastDayOfCurrentWeek() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.DATE, 6);
        return c.getTime();
    }

    public static int getCurrentDay() {
        return LocalDate.now().getDayOfYear();
    }

    public static int getCurrentMonth() {
        return LocalDate.now().getMonthValue();
    }

    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }

    public static Date getFirstDayOfMonth(int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static Date getLastDayOfMonth(int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    public static String getDayOfCustomYearFromToday(int numberOfYears, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, numberOfYears);
        return simpleDateFormat.format(cal.getTime());
    }

    public static Date startOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date endOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String getFormatedLastDayOfMonth(int month, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(getLastDayOfMonth(month));
    }

    public static String getFormatedFirstDayOfMonth(int month, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(getFirstDayOfMonth(month));
    }

    public static Calendar toCalendar(Date date) {
        if (date == null)
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date fromQRDate(String dateFromRecord) throws ParseException {
        int length = dateFromRecord.length();
        if (length != 7 && length != 8) {
            LOGGER.info("invalid date: {}, length: {}", dateFromRecord, length);
            throw new ParseException("invalid dateFormat length: " + dateFromRecord, 0);
        }
        if (length == 7) {
            dateFromRecord = fromDynastyDateFormatToCommonFormat(dateFromRecord);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.parse(dateFromRecord);
    }

    public static String toQRDate(Date date) {
        if (date == null) {
            return Constants.EMPTY_STRING;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }

    private static String fromDynastyDateFormatToCommonFormat(String dateStr) {
        String dynastySymbol = dateStr.substring(0, 1);
        Integer yearStartDynasty = MAP_DYNASTY_JP_AND_YEAR.get(dynastySymbol);
        if (yearStartDynasty == null) {
            LOGGER.info("invalid dynasty symbol: {}", dynastySymbol);
            return "";
        }

        String numberOfYearAfterStartOfDynastyStr = dateStr.substring(1, 3);
        try {
            int numberOfYearAfterStartOfDynasty = Integer.parseInt(numberOfYearAfterStartOfDynastyStr);
            int yearOfDate = yearStartDynasty + numberOfYearAfterStartOfDynasty - 1;
            String monthAndDate = dateStr.substring(3, 7);
            return yearOfDate + monthAndDate;
        } catch (NumberFormatException e) {
            LOGGER.info("numberOfYearAfterStartOfDynasty is not a number: {}", numberOfYearAfterStartOfDynastyStr);
            return "";
        }
    }

    public static String toDynastyDateFormat(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dynastySymbol = "";
        int numberOfYearAfterDynasty = 0;
        for (Map.Entry<String, Integer> entry : MAP_DYNASTY_JP_AND_YEAR.entrySet()) {
            if (year < entry.getValue()) {
                break;
            }
            dynastySymbol = entry.getKey();
            numberOfYearAfterDynasty = year - entry.getValue() + 1;
        }
        String monthStr = month < 10 ? "0" + month : String.valueOf(month);
        String dayStr = day < 10 ? "0" + day : String.valueOf(day);
        String numberOfYearAfterStartOfDynastyStr = numberOfYearAfterDynasty < 10 ? "0" + numberOfYearAfterDynasty : String.valueOf(numberOfYearAfterDynasty);
        return dynastySymbol + numberOfYearAfterStartOfDynastyStr + monthStr + dayStr;
    }

    public static String formatDateToStringWithoutHour(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(date);
    }

    public static String getDateFormatted(Date date, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static Integer getAgeFromBirthday(Date birthday) {
        if (birthday == null) {
            return null;
        }
        Period period = Period.between(birthday.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate(), LocalDate.now());
        return period.getYears();
    }

}
