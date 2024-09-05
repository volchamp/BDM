package com.kmvc.jeesite.modules.syncPortal.utils;

import java.text.*;
import java.util.*;

public class DateUtil
{
    private static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    private static final int MORNING_START_WORK = 9;
    private static final int MORNING_END_WORK = 12;
    private static final int AFTERMOON_START_WORK = 14;
    private static final String AFTERMOON_END_WORK = " 18:30:00";
    private static final long WORK_TIME_LENGTH = 27000000L;
    private static final long HOUR = 3600000L;
    private static final long SIESTA = 7200000L;
    List<String> holidayList;
    List<String> workdayList;
    int startHour;
    int startMinute;
    int endHour;
    int endMinute;

    private DateUtil() {
        this.holidayList = new ArrayList<String>();
        this.workdayList = new ArrayList<String>();
        this.startHour = 0;
        this.startMinute = 0;
        this.endHour = 0;
        this.endMinute = 0;
        final int i = 0;
        this.holidayList.add("2016-1-1");
        this.holidayList.add("2016-2-8");
        this.holidayList.add("2016-2-9");
        this.holidayList.add("2016-2-10");
        this.holidayList.add("2016-2-11");
        this.holidayList.add("2016-2-12");
        this.holidayList.add("2016-2-13");
        this.holidayList.add("2016-4-1");
        this.holidayList.add("2016-5-2");
        this.holidayList.add("2016-6-9");
        this.holidayList.add("2016-6-10");
        this.holidayList.add("2016-6-11");
        this.holidayList.add("2016-9-15");
        this.holidayList.add("2016-9-16");
        this.holidayList.add("2016-9-17");
        this.holidayList.add("2016-10-3");
        this.holidayList.add("2016-10-4");
        this.holidayList.add("2016-10-5");
        this.holidayList.add("2016-10-6");
        this.holidayList.add("2016-10-7");
        this.workdayList.add("2016-2-6");
        this.workdayList.add("2016-2-14");
        this.workdayList.add("2016-6-12");
        this.workdayList.add("2016-9-18");
        this.workdayList.add("2016-10-8");
        this.workdayList.add("2016-10-9");
    }

    public static String getFormatterValue(final Date date, final String formatter) {
        final SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        return sdf.format(date);
    }

    public static Date getFormatterValue(final String source, final String formatter) {
        final SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        try {
            return sdf.parse(source);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int[] getIntTime(final String time) {
        final int[] t = new int[2];
        final String[] str = time.split(":");
        t[0] = Integer.parseInt(str[0]);
        t[1] = Integer.parseInt(str[1]);
        return t;
    }

    private long countAgeing(Date inputTime, Date closeTime) throws Exception {
        inputTime = this.handleInputTime(inputTime);
        closeTime = this.handleCloseTime(closeTime);
        return this.countRealAgeing(inputTime, closeTime);
    }

    private long countRealAgeing(final Date inputTime, final Date closeTime) throws Exception {
        final int inputWeek = this.getWeekTime(inputTime);
        Calendar inputDateCalendar = Calendar.getInstance();
        inputDateCalendar.setTime(inputTime);
        Calendar closeDateCalendar = Calendar.getInstance();
        closeDateCalendar.setTime(closeTime);
        final long day = 86400000L;
        final long mod = (closeDateCalendar.getTimeInMillis() - inputDateCalendar.getTimeInMillis()) / day;
        System.out.println("----->\u5929\u6570\uff1a" + mod);
        final long weekNum = mod / 7L;
        long cut = weekNum * 2L;
        final long yuShuDay = mod % 7L;
        if (inputWeek + yuShuDay > 7L) {
            cut += 2L;
        }
        if (inputWeek + yuShuDay == 7L) {
            ++cut;
        }
        inputDateCalendar.setTime(inputTime);
        final long inputTimeStartLong = inputDateCalendar.getTimeInMillis();
        final Date inputTempTime = inputTime;
        inputDateCalendar = this.addHour(inputTempTime, 17, 30);
        final long inputTimeEndLong = inputDateCalendar.getTimeInMillis();
        final Date endTempTime = closeTime;
        closeDateCalendar = this.addHour(endTempTime, 8, 30);
        final long closeTimeStartLong = closeDateCalendar.getTimeInMillis();
        closeDateCalendar.setTime(closeTime);
        final long closeTimeEndLong = closeDateCalendar.getTimeInMillis();
        final long hour = 360000L;
        long ageingLong = 0L;
        final Long middayNum = 7200000L;
        if (mod == 0L) {
            System.out.println(" inputTime: " + inputTime + " closeTime: " + closeTime);
            inputDateCalendar.setTime(inputTime);
            closeDateCalendar.setTime(closeTime);
            final Calendar c5 = this.addHour(endTempTime, 12, 0);
            final Date closeTempTime1 = c5.getTime();
            final Calendar c6 = this.addHour(inputTempTime, 12, 0);
            final Date closeTempTime2 = c6.getTime();
            if (inputTime.before(closeTempTime2) && closeTime.after(closeTempTime1)) {
                ageingLong = closeDateCalendar.getTimeInMillis() - inputDateCalendar.getTimeInMillis() - 7200000L;
            }
            else {
                ageingLong = closeDateCalendar.getTimeInMillis() - inputDateCalendar.getTimeInMillis();
            }
        }
        else {
            final long str = (mod - cut - 1L + this.countRestDay(inputTime, closeTime)) * 27000000L;
            ageingLong = inputTimeEndLong - inputTimeStartLong + closeTimeEndLong - closeTimeStartLong + str;
            final Calendar c6 = this.addHour(inputTempTime, 12, 0);
            final Date inputTempTime2 = c6.getTime();
            if (inputTime.before(inputTempTime2)) {
                ageingLong -= 7200000L;
            }
            final Calendar c7 = this.addHour(endTempTime, 14, 0);
            final Date inputTempTime3 = c7.getTime();
            if (closeTime.after(inputTempTime3)) {
                ageingLong -= 7200000L;
            }
        }
        System.out.println("ageingLong:" + ageingLong);
        if (ageingLong < 0L) {
            return 0L;
        }
        return ageingLong;
    }

    private int countRestDay(final Date inputTime, final Date closeTime) throws Exception {
        Date inputNow = inputTime;
        final Date closeNow = closeTime;
        inputNow = this.addDay(inputNow, 1);
        int index = 0;
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        while (inputNow.after(inputTime) && inputNow.before(closeNow)) {
            for (int k = 0; k < this.holidayList.size(); ++k) {
                int i = 99;
                i = inputNow.compareTo(sdf.parse(this.holidayList.get(k)));
                if (i == 0) {
                    --index;
                    break;
                }
            }
            for (int j = 0; j < this.workdayList.size(); ++j) {
                int i = 99;
                i = inputNow.compareTo(sdf.parse(this.workdayList.get(j)));
                if (i == 0) {
                    ++index;
                    break;
                }
            }
            inputNow = this.addDay(inputNow, 1);
        }
        return index;
    }

    private Date handleCloseTime(Date closeTime) throws Exception {
        final int closeWeek = this.getWeekTime(closeTime);
        final Date closeTempTime = closeTime;
        if (this.isWorkDay(closeTime) && this.isFestival(closeTime)) {
            if (closeWeek == 7) {
                closeTime = this.findHolidayBeforeLastDay(closeTempTime);
            }
            else if (closeWeek == 1) {
                closeTime = this.findHolidayBeforeLastDay(closeTempTime);
            }
            else if (closeWeek == 2) {
                if (this.isBeforeStartWork(closeTime)) {
                    closeTime = this.findHolidayBeforeLastDay(closeTempTime);
                }
                else if (this.isAfterEndWork(closeTime)) {
                    final Calendar c1 = this.addHour(closeTempTime, 17, 30);
                    closeTime = c1.getTime();
                }
            }
            else if (this.isBeforeStartWork(closeTime)) {
                closeTime = this.findHolidayBeforeLastDay(closeTempTime);
            }
            else if (this.isAfterEndWork(closeTime)) {
                final Calendar c2 = this.addHour(closeTempTime, 17, 30);
                closeTime = c2.getTime();
            }
        }
        else if (!this.isFestival(closeTime)) {
            closeTime = this.findHolidayBeforeLastDay(closeTime);
        }
        else if (!this.isWorkDay(closeTime)) {
            if (this.isBeforeStartWork(closeTime)) {
                closeTime = this.findHolidayBeforeLastDay(closeTime);
            }
            else if (this.isAfterEndWork(closeTime)) {
                final Calendar c3 = this.addHour(closeTempTime, 17, 30);
                closeTime = c3.getTime();
            }
        }
        final Calendar c4 = this.addHour(closeTempTime, 14, 0);
        final Date closeTempTime2 = c4.getTime();
        final Calendar c5 = this.addHour(closeTempTime, 12, 0);
        final Date closeTempTime3 = c5.getTime();
        if (closeTime.before(closeTempTime2) && closeTime.after(closeTempTime3)) {
            final Calendar c6 = this.addHour(closeTempTime, 12, 0);
            closeTime = c6.getTime();
        }
        return closeTime;
    }

    private Date handleInputTime(Date inputTime) throws Exception {
        final int inputWeek = this.getWeekTime(inputTime);
        final Date inputTempTime = inputTime;
        if (this.isFestival(inputTime) && this.isWorkDay(inputTime)) {
            if (inputWeek == 7) {
                inputTime = this.findHolidayAfterFirstWorkDay(inputTime);
            }
            else if (inputWeek == 1) {
                inputTime = this.findHolidayAfterFirstWorkDay(inputTime);
            }
            else if (inputWeek == 6) {
                if (this.isBeforeStartWork(inputTime)) {
                    final Calendar c3 = this.addHour(inputTempTime, 8, 30);
                    inputTime = c3.getTime();
                }
                else if (this.isAfterEndWork(inputTime)) {
                    inputTime = this.findHolidayAfterFirstWorkDay(inputTime);
                }
            }
            else if (this.isBeforeStartWork(inputTime)) {
                final Calendar c4 = this.addHour(inputTempTime, 8, 30);
                inputTime = c4.getTime();
            }
            else if (this.isAfterEndWork(inputTime)) {
                inputTime = this.findHolidayAfterFirstWorkDay(inputTime);
            }
        }
        else if (!this.isFestival(inputTime)) {
            inputTime = this.findHolidayAfterFirstWorkDay(inputTime);
        }
        else if (!this.isWorkDay(inputTime)) {
            if (this.isBeforeStartWork(inputTime)) {
                final Calendar c4 = this.addHour(inputTempTime, 8, 30);
                inputTime = c4.getTime();
            }
            else if (this.isAfterEndWork(inputTime)) {
                inputTime = this.findHolidayAfterFirstWorkDay(inputTime);
            }
        }
        final Calendar c5 = this.addHour(inputTempTime, 14, 0);
        final Date inputTempTime2 = c5.getTime();
        final Calendar c6 = this.addHour(inputTempTime, 12, 0);
        final Date inputTempTime3 = c6.getTime();
        if (inputTime.before(inputTempTime2) && inputTime.after(inputTempTime3)) {
            final Calendar c7 = this.addHour(inputTempTime, 14, 0);
            inputTime = c7.getTime();
        }
        return inputTime;
    }

    private Date findHolidayAfterFirstWorkDay(Date dateTime) throws Exception {
        boolean flag = true;
        final Calendar c1 = this.addHour(dateTime, 8, 30);
        c1.add(5, 1);
        dateTime = c1.getTime();
        while (flag) {
            if (!this.isFestival(dateTime)) {
                dateTime = this.addDay(dateTime, 1);
            }
            else if (!this.isWorkDay(dateTime) || (this.getWeekTime(dateTime) != 1 && this.getWeekTime(dateTime) != 7)) {
                final Date dateTempTime = dateTime;
                final Calendar c2 = this.addHour(dateTempTime, 8, 30);
                dateTime = c2.getTime();
                flag = false;
            }
            else {
                dateTime = this.addDay(dateTime, 1);
            }
        }
        System.out.println(dateTime);
        return dateTime;
    }

    private Date findHolidayBeforeLastDay(Date dateTime) throws Exception {
        boolean flag = true;
        dateTime = this.addDay(dateTime, -1);
        while (flag) {
            if (!this.isFestival(dateTime)) {
                dateTime = this.addDay(dateTime, -1);
            }
            else if (!this.isWorkDay(dateTime) || (this.getWeekTime(dateTime) != 1 && this.getWeekTime(dateTime) != 7)) {
                final Date dateTempTime = dateTime;
                final Calendar c3 = this.addHour(dateTempTime, 17, 30);
                dateTime = c3.getTime();
                flag = false;
            }
            else {
                dateTime = this.addDay(dateTime, -1);
            }
        }
        return dateTime;
    }

    private boolean isFestival(final Date dateTime) throws Exception {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String date = sdf.format(dateTime);
        final Date nowDay = sdf.parse(date);
        int isFestival = 99;
        if (this.holidayList != null && this.holidayList.size() > 0) {
            for (int i = 0; i < this.holidayList.size(); ++i) {
                isFestival = nowDay.compareTo(sdf.parse(this.holidayList.get(i)));
                if (isFestival == 0) {
                    break;
                }
            }
        }
        return isFestival != 0;
    }

    private boolean isWorkDay(final Date dateTime) throws Exception {
        int isWorkDay = 99;
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String date = sdf.format(dateTime);
        final Date nowDay = sdf.parse(date);
        if (this.workdayList != null && this.workdayList.size() > 0) {
            for (int i = 0; i < this.workdayList.size(); ++i) {
                isWorkDay = nowDay.compareTo(sdf.parse(this.workdayList.get(i)));
                if (isWorkDay == 0) {
                    break;
                }
            }
        }
        return isWorkDay != 0;
    }

    private boolean isBeforeStartWork(final Date dateTime) {
        final Date tempTime = dateTime;
        final Calendar c1 = this.addHour(tempTime, 0, 0);
        final Date startWork1 = c1.getTime();
        System.out.println(startWork1);
        final Calendar c2 = this.addHour(tempTime, 8, 30);
        final Date startWork2 = c2.getTime();
        return dateTime.after(startWork1) && dateTime.before(startWork2);
    }

    private boolean isAfterEndWork(final Date dateTime) {
        final Date tempTime = dateTime;
        final Calendar c1 = this.addHour(tempTime, 17, 30);
        final Date endWork1 = c1.getTime();
        final Calendar c2 = this.addHour(tempTime, 0, 0);
        c2.add(5, 1);
        final Date endWork2 = c2.getTime();
        return dateTime.after(endWork1) && dateTime.before(endWork2);
    }

    private int getWeekTime(final Date dateTime) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        final int week = calendar.get(7);
        System.out.println(week);
        return week;
    }

    private Calendar addHour(final Date dateTime, final int h, final int m) {
        final Calendar c = Calendar.getInstance();
        c.setTime(dateTime);
        c.set(11, h);
        c.set(12, m);
        c.set(13, 0);
        return c;
    }

    private Date addDay(final Date dateTime, final int day) {
        final Calendar c = Calendar.getInstance();
        c.setTime(dateTime);
        c.add(5, day);
        return c.getTime();
    }

    public static void main(final String[] args) {
        final String startdate = "2016-2-13 9:00:00";
        final String endDate = "2016-2-14 9:30:00";
        final DateUtil dateUtil = new DateUtil();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            final Date inputTime = sdf.parse(startdate);
            final Date closeTime = sdf.parse(endDate);
            System.out.println("+++" + inputTime);
            System.out.println("=====" + dateUtil.countAgeing(inputTime, closeTime));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
