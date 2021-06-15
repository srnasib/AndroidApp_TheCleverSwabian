package logiclayer.model;


import logiclayer.controller.IconManager;

public class Category
{
    private boolean type;
    //false = expense
    //true = income
    private String name;
    private String iconID;
    private boolean userCreated;
    private boolean notDeletable;

    public Category(boolean type, String name, String iconID, boolean userCreated)
    {
        this.type = type;
        this.name = name;
        this.iconID = iconID;
        this.userCreated = userCreated;
    }

    public Category(boolean type, String name, String iconID, boolean userCreated, boolean notDeletable)
    {
        this.type = type;
        this.name = name;
        this.iconID = iconID;
        this.userCreated = userCreated;
        this.notDeletable = notDeletable;
    }


    public boolean getType()
    {
        return type;
    }

    public void setType(boolean type)
    {
        this.type = type;
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

    public Boolean getUserCreated() {return userCreated;}

    public void setUserCreated(boolean userCreated) {this.userCreated = userCreated;}

    public Boolean getNotDeletable(){return notDeletable;}

    public void setNotDeletable(boolean notDeletable) {this.notDeletable = notDeletable;}
}
