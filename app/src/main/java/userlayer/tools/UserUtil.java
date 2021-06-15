package userlayer.tools;

import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import logiclayer.controller.SettingsManager;
import main.MainActivity;

//With Java 8, this would be an interface, but we're using Java 7.
public abstract class UserUtil
{
    public static String dateToString(int year, int month, int day)
    {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(year, month, day);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        String dayString = Integer.toString(day);
        //In Android, January is month 0
        String monthString = Integer.toString(month + 1);
        String dayofWeekString = "";
        if (day < 10)
        {
            dayString = "0" + dayString;
        }
        if (month < 9)
        {
            monthString = "0" + monthString;
        }
        switch (weekday)
        {
            case 0:
                dayofWeekString = "Sun";
                break;
            case 1:
                dayofWeekString = "Mon";
                break;
            case 2:
                dayofWeekString = "Tue";
                break;
            case 3:
                dayofWeekString = "Wed";
                break;
            case 4:
                dayofWeekString = "Thu";
                break;
            case 5:
                dayofWeekString = "Fri";
                break;
            case 6:
                dayofWeekString = "Sat";
                break;
        }
        return SettingsManager.formatDate(dayofWeekString, dayString, monthString, Integer.toString(year));
    }

    public static String dateToString(Date date)
    {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return dateToString(year, month, day);
    }


    public static String getDecimalSeparator()
    {
        DecimalFormat decFormat = (DecimalFormat) DecimalFormat.getInstance(Locale.getDefault());
        DecimalFormatSymbols symbols = decFormat.getDecimalFormatSymbols();
        return Character.toString(symbols.getDecimalSeparator());
    }

    public static void displayText(String text)
    {
        Toast toast = Toast.makeText(MainActivity.getContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

}
