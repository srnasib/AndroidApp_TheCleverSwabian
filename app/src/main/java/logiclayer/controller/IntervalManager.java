package logiclayer.controller;

import java.util.ArrayList;
import java.util.Calendar;

import logiclayer.model.Interval;

public class IntervalManager
{
    public static final ArrayList<Interval> INTERVAL_LIST = initialiseIntervalList();
    //default: 1 month
    public static final int DEFAULT_INTERVAL = 5;

    //allows only one object at once
    private static volatile IntervalManager instance;

    private IntervalManager()
    {
    }

    static IntervalManager getInstance()
    {
        if (instance == null)
        {
            synchronized (EntryManager.class)
            {
                if (instance == null)
                {
                    instance = new IntervalManager();
                }
            }
        }
        return instance;
    }






    private static ArrayList<Interval> initialiseIntervalList()
    {
        ArrayList<Interval> intervalList = new ArrayList<Interval>();
        intervalList.add(new Interval(Calendar.YEAR,2,"2 years"));
        intervalList.add(new Interval(Calendar.YEAR,1,"1 year"));
        intervalList.add(new Interval(Calendar.MONTH,6,"6 months"));
        intervalList.add(new Interval(Calendar.MONTH,3,"3 months"));
        intervalList.add(new Interval(Calendar.MONTH,2,"2 months"));
        intervalList.add(new Interval(Calendar.MONTH,1,"1 month"));
        intervalList.add(new Interval(Calendar.DATE,2*7,"2 weeks"));
        intervalList.add(new Interval(Calendar.DATE,1*7,"1 week"));
        intervalList.add(new Interval(Calendar.DATE,2,"2 days"));
        intervalList.add(new Interval(Calendar.DATE,1,"1 day"));

        return intervalList;
    }


    public String[] getIntervalNames()
    {
        String[] intervalNames = new String[INTERVAL_LIST.size()];

        for (int i=0;i<INTERVAL_LIST.size();i++)
        {
            intervalNames[i] = INTERVAL_LIST.get(i).getName();
        }
        return intervalNames;
    }

    public int getIntervalPosition(Interval interval)
    {
        for (int i = 0; i < INTERVAL_LIST.size(); i++)
        {
            if (INTERVAL_LIST.get(i).equals(interval))
            {
                return i;
            }
        }
        //not successful
        return -1;
    }

    public Interval getDefaultInterval()
    {
        return INTERVAL_LIST.get(DEFAULT_INTERVAL);
    }

}
