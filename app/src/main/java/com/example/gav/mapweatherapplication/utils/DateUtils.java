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
}
