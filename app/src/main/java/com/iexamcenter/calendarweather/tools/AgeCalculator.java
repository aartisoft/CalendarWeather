package com.iexamcenter.calendarweather.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AgeCalculator
{
    public static Age calculateAge(Calendar birthDate,Calendar now)
    {
        int years = 0;
        int months = 0;
        int days = 0;

        //create calendar object for birth day
      //  Calendar birthDay = Calendar.getInstance();
       // birthDay.setTimeInMillis(birthDate.getTime());

        //create calendar object for current day
      //  long currentTime = System.currentTimeMillis();
     //   Calendar now = Calendar.getInstance();
      //  now.setTimeInMillis(currentTime);

        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDate.get(Calendar.MONTH) + 1;

        //Get difference between months
        months = currMonth - birthMonth;

        //if month difference is in negative then reduce years by one
        //and calculate the number of months.
        if (months < 0)
        {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDate.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDate.get(Calendar.DATE))
        {
            years--;
            months = 11;
        }

        //Calculate the days
        if (now.get(Calendar.DATE) > birthDate.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDate.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDate.get(Calendar.DATE))
        {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDate.get(Calendar.DAY_OF_MONTH) + today;
        }
        else
        {
            days = 0;
            if (months == 12)
            {
                years++;
                months = 0;
            }
        }
        //Create new Age object
        return new Age(days, months, years);
    }


}