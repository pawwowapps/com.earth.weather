package com.example.gav.mapweatherapplication.utils;

import android.content.Context;

import com.example.gav.mapweatherapplication.R;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static String getDayOfWeek(Context context, long unixTime) {
        StringBuilder result = new StringBuilder();
        Date date = new Date(unixTime * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                result.append(context.getString(R.string.sunday));
                break;
            case Calendar.MONDAY:
                result.append(context.getString(R.string.monday));
                break;
            case Calendar.TUESDAY:
                result.append(context.getString(R.string.tuesday));
                break;
            case Calendar.WEDNESDAY:
                result.append(context.getString(R.string.wednesday));
                break;
            case Calendar.THURSDAY:
                result.append(context.getString(R.string.thursday));
                break;
            case Calendar.FRIDAY:
                result.append(context.getString(R.string.friday));
                break;
            case Calendar.SATURDAY:
                result.append(context.getString(R.string.saturday));
                break;
        }
        return result.toString();
    }

    public static String getMonthToString(Context context, long unixTime) {
        StringBuilder result = new StringBuilder();
        Date date = new Date(unixTime * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        switch (calendar.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                result.append(context.getString(R.string.january));
                break;
            case Calendar.FEBRUARY:
                result.append(context.getString(R.string.february));
                break;
            case Calendar.MARCH:
                result.append(context.getString(R.string.march));
                break;
            case Calendar.APRIL:
                result.append(context.getString(R.string.april));
                break;
            case Calendar.MAY:
                result.append(context.getString(R.string.may));
                break;
            case Calendar.JUNE:
                result.append(context.getString(R.string.june));
                break;
            case Calendar.JULY:
                result.append(context.getString(R.string.july));
                break;
            case Calendar.AUGUST:
                result.append(context.getString(R.string.august));
                break;
            case Calendar.SEPTEMBER:
                result.append(context.getString(R.string.september));
                break;
            case Calendar.OCTOBER:
                result.append(context.getString(R.string.october));
                break;
            case Calendar.NOVEMBER:
                result.append(context.getString(R.string.november));
                break;
            case Calendar.DECEMBER:
                result.append(context.getString(R.string.december));
                break;
        }
        return result.toString();
    }
}
