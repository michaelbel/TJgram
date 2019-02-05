package org.michaelbel.tjgram.utils.date;

import android.content.Context;
import android.text.TextUtils;

import org.michaelbel.tjgram.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class TimeFormatter {

    private static final long MILLISECONDS_IN_SECOND = 1000;
    private static final long MILLISECONDS_IN_MINUTE = 1000 * 60;
    private static final long MILLISECONDS_IN_HOUR = 1000 * 60 * 60;
    private static final long MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;

    //private String[] russianMonths = {"Янв", "Фев", "Мар", "Апр", "Мая", "Июня", "Июля", "Авг", "Сен", "Окт", "Ноя", "Дек"};

    public static CharSequence getTimeAgo(Context context, String startTime) {
        SimpleDateFormat startFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
        String currentTime = startFormat.format(new Date());

        try {
            Date startDate = startFormat.parse(startTime);
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);

            Date nowDate = startFormat.parse(currentTime);
            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.setTime(nowDate);

            long milliseconds = System.currentTimeMillis()/*nowDate.getTime()*/ - startDate.getTime();

            long seconds = milliseconds / MILLISECONDS_IN_SECOND;
            long minutes = milliseconds / MILLISECONDS_IN_MINUTE;
            long hours = milliseconds / MILLISECONDS_IN_HOUR;
            //long days = milliseconds / MILLISECONDS_IN_DAY;

            boolean isDayToday = nowCalendar.get(Calendar.DATE) == startCalendar.get(Calendar.DATE);
            boolean isDayYesterday = nowCalendar.get(Calendar.DATE) - startCalendar.get(Calendar.DATE) == 1;
            boolean isDayBeforeYesterday = nowCalendar.get(Calendar.DATE) - startCalendar.get(Calendar.DATE) > 1;
            boolean isMonthNow = nowCalendar.get(Calendar.MONTH) == startCalendar.get(Calendar.MONTH);
            boolean isYearNow = nowCalendar.get(Calendar.YEAR) == startCalendar.get(Calendar.YEAR);
            boolean isYearLast = nowCalendar.get(Calendar.YEAR) > startCalendar.get(Calendar.YEAR);

            String formatDate = formatDate(context, /*"d MMM"*/"dd.MM", startDate);
            String formatDateWithYear = formatDate(context, /*"d MMM yyyy"*/"dd.MM.yyyy", startDate);
            String formatTime = formatDate(context, "HH:mm", startDate);

            if (seconds >= 0 && seconds < 60) {
                return context.getString(R.string.just_now);
            } else if (minutes > 0 && minutes < 2) {
                return context.getString(R.string.minute_ago);
            } else if ( minutes >= 2 && minutes < 60) {
                return context.getResources().getQuantityString(R.plurals.minutes, (int) minutes, (int) minutes);
            } else if (hours > 0 && hours < 2) {
                return context.getString(R.string.hour_ago);
            } else if (hours >= 2 && hours < 13) {
                return context.getResources().getQuantityString(R.plurals.hours, (int) hours, (int) hours);
            } else if (hours > 12 && isDayToday && isMonthNow) {
                return context.getString(R.string.today_at, formatTime);
            } else if (hours > 12 && isDayYesterday && isMonthNow) {
                return context.getString(R.string.yesterday_at, formatTime);
            } else if (isDayBeforeYesterday && isYearNow) {
                return context.getString(R.string.default_date, formatDate, formatTime);
            } else if (isDayBeforeYesterday && isYearLast) {
                return context.getString(R.string.default_date, formatDateWithYear, formatTime);
            } else {
                return context.getString(R.string.default_date, formatDateWithYear, formatTime);
            }
        } catch (ParseException e) {
            Timber.e(e);
        }

        return context.getString(R.string.unknown_date);
    }

    private static String formatDate(Context context, String format, Date date) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, LocaleUtil.INSTANCE.getLocale(context));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        return simpleDateFormat.format(date != null ? date : new Date());
    }

    // bugs.openjdk.java.net/browse/JDK-8075548
    // Русские месяцы не конвертируются, временно заюзан US
    public static String convertSignDate(Context context, String startDate) {
        if (startDate == null || TextUtils.isEmpty(startDate)) {
            return context.getString(R.string.unknown_date);
        }

        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ssZZZZZ", Locale.US);
        SimpleDateFormat newFormat = new SimpleDateFormat(/*"d MMM yyyy"*/"dd.MM.yyyy", Locale.US);

        try {
            Date date = format.parse(startDate);
            return newFormat.format(date);
        } catch (ParseException e) {
            Timber.e(e);
        }

        return context.getString(R.string.unknown_date);
    }
}