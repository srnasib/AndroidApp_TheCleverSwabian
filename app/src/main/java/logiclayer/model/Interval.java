package logiclayer.model;

import java.util.Calendar;

public class Interval
{
    private int multiplicator;
    private int type = Calendar.MONTH;
    private String name;

    public Interval(int type, int multiplicator, String name)
    {
        //type of Calendar.TYPE
        this.type = type;
        this.multiplicator = multiplicator;
        this.name = name;
    }

    public int getType()
    {
        return type;
    }
    public int getMultiplicator()
    {
        return multiplicator;
    }

    public String getName()
    {
        return name;
    }

}
