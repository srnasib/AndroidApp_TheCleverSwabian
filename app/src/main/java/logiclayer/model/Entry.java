package logiclayer.model;

import java.util.Date;

public class Entry
{
    private Date date;
    private String title;
    private boolean type;
    //false = expense
    //true = income
    private double amount;
    //amount is always positive
    private Category category;
    private PaymentMethod paymentMethod;
    private String comment;
    private Date modificationTime;


    public Entry(Date date, String title, boolean type, double amount, Category category, PaymentMethod paymentMethod, String comment, Date modificationTime)
    {
        this.date = date;
        this.title = title;
        this.type = type;
        this.amount = amount;
        this.category = category;

        if(type)
        {
            paymentMethod = null;
        }
        else
        {
            this.paymentMethod = paymentMethod;
        }

        this.comment = comment;
        this.modificationTime = modificationTime;
    }

    //Clone function
    public Entry(Entry another)
    {
        this.date = another.date;
        this.title = another.title;
        this.type = another.type;
        this.amount = another.amount;
        this.category = another.category;
        if(type)
        {
            paymentMethod = null;
        }
        else
        {
            this.paymentMethod = another.paymentMethod;
        }
        this.comment = another.comment;
        this.modificationTime = another.modificationTime;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public boolean getType()
    {
        return type;
    }

    public void setType(boolean type)
    {
        this.type = type;
        if(type)
        {
            paymentMethod = null;
        }
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

    public PaymentMethod getPaymentMethod()
    {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod)
    {
        if(!type)
        {
            this.paymentMethod = paymentMethod;
        }
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Date getModificationTime()
    {
        return modificationTime;
    }

    public void setModificationTime(Date modificationTime)
    {
        this.modificationTime = modificationTime;
    }
}
