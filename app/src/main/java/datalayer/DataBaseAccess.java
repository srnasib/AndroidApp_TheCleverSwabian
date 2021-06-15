package datalayer;

import main.MainActivity;

public interface DataBaseAccess
{
    public DatabaseHelper databaseHelper = DatabaseHelper.getInstance(MainActivity.getContext());


}
