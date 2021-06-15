package logiclayer.model;

import java.util.Calendar;
import java.util.Date;

public class RecurringEntry extends Entry
{
    private Date endDate;
    private Interval interval;
    private Date lastEntryCreation = null;

    public RecurringEntry(Date date, Date endDate, Interval interval, String title, boolean type, double amount, Category category, PaymentMethod paymentMethod, String comment, Date modificationTime)
    {
        super(date, title, type, amount, category, paymentMethod, comment, modificationTime);
        this.endDate = endDate;
        this.interval = interval;
    }

    //Clone function
    public RecurringEntry(RecurringEntry another)
    {
        super(another);
        this.endDate = another.getEndDate();
        this.interval = another.getInterval();
        this.lastEntryCreation = another.lastEntryCreation;
    }

    public RecurringEntry(Entry entry, Interval interval, Date endDate)
    {
        super(entry);
        this.interval = interval;
        this.endDate = endDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Interval getInterval()
    {
        return interval;
    }

    public void setInterval(Interval interval)
    {
        this.interval = interval;
    }

    public Date getLastEntryCreation()
    {
        return lastEntryCreation;
    }

    public void setLastEntryCreation(Date lastEntryCreation)
    {
        this.lastEntryCreation = lastEntryCreation;
    }

    public void overwriteEntryAttributes(Entry entry)
    {
        setDate(entry.getDate());
        setTitle(entry.getTitle());
        setType(entry.getType());
        setAmount(entry.getAmount());
        setCategory(entry.getCategory());
        setPaymentMethod(entry.getPaymentMethod());
        setComment(entry.getComment());
        setModificationTime(entry.getModificationTime());
    }

    public Entry castToEntry(Date date)
    {
        return new Entry(date,getTitle(),getType(),getAmount(),getCategory(),getPaymentMethod(),getComment(), Calendar.getInstance().getTime());
    }
}