package logiclayer.model;

import logiclayer.controller.IconManager;

public class PaymentMethod
{
    private String name;
    private String iconID;
    private boolean notDeletable;

    public PaymentMethod(String name, String iconID)
    {
        this.name = name;
        this.iconID = iconID;
    }

    public PaymentMethod(String name, String iconID, boolean notDeletable)
    {
        this.name = name;
        this.iconID = iconID;
        this.notDeletable = notDeletable;
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getIconID()
    {
        return iconID;
    }

    public int getIconAdress()
    {
        return IconManager.getDrawable(iconID);
    }

    public void setIconID(String iconID)
    {
        this.iconID = iconID;
    }

    public Boolean getNotDeletable() {return notDeletable;}

    public void setNotDeletable(boolean notDeletable) {this.notDeletable = notDeletable;}
}
