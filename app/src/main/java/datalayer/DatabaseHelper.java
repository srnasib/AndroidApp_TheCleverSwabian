package datalayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.ArrayList;



public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "MainDatabase.db";

    public static final String TABLE_EXPENSE_CATEGORIES = "ExpenseCategories";
    public static final String TABLE_INCOME_CATEGORIES = "IncomeCategories";
    public static final String TABLE_DEFAULT_EXPENSE_CATEGORY = "DefaultExpenseCategory";
    public static final String TABLE_DEFAULT_INCOME_CATEGORY = "DefaultIncomeCategory";
    public static final String TABLE_ENTRIES = "Entries";
    public static final String TABLE_RECURRING_ENTRIES = "RecurringEntries";
    public static final String TABLE_PAYMENT_METHODS = "PaymentMethods";
    public static final String TABLE_DEFAULT_PAYMENT_METHOD = "DefaultPaymentMethod";
    public static final String TABLE_USERNAME = "Username";
    public static final String TABLE_PASSWORD = "Password";
    public static final String TABLE_PASSWORD_SETUP = "PasswordSetup";
    public static final String TABLE_SECURITY_QUESTIONS = "SecurityQuestions";
    public static final String TABLE_SECURITY_ANSWERS = "SecurtiyAnswers";
    public static final String TABLE_DATEFORMAT = "Dateformat";
    public static final String TABLE_CURRENCY = "Currency";
    public static final String TABLE_LANGUAGE = "Language";
    public static final String TABLE_SETUP_PROGRESS = "SetupProgress";

    private static final String[] tableNames =
    {
        TABLE_EXPENSE_CATEGORIES,
        TABLE_INCOME_CATEGORIES,
        TABLE_DEFAULT_EXPENSE_CATEGORY,
        TABLE_DEFAULT_INCOME_CATEGORY,
        TABLE_ENTRIES,
        TABLE_RECURRING_ENTRIES,
        TABLE_PAYMENT_METHODS,
        TABLE_DEFAULT_PAYMENT_METHOD,
        TABLE_USERNAME,
        TABLE_PASSWORD,
        TABLE_PASSWORD_SETUP,
        TABLE_SECURITY_QUESTIONS,
        TABLE_SECURITY_ANSWERS,
        TABLE_DATEFORMAT,
        TABLE_CURRENCY,
        TABLE_LANGUAGE,
        TABLE_SETUP_PROGRESS
    };

    //allows only one object at once
    private static volatile DatabaseHelper instance;

    public static DatabaseHelper getInstance(Context context)
    {
        if (instance == null)
        {
            synchronized (DatabaseHelper.class)
            {
                if (instance == null)
                {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    private DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        for(int i = 0;i<tableNames.length;i++)
        {
            sqLiteDatabase.execSQL("CREATE TABLE " + tableNames[i] + "(" + tableNames[i] + " TEXT)");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        for(int i = 0;i<tableNames.length;i++)
        {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableNames[i]);
        }
    }

    public boolean addObject(Object object, String table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(table, convertObjectToString(object));

        long row = db.insert(table, null, contentValues);
        db.close();

        if(row == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean clearAndAddObject(Object object, String table)
    {
        clearTable(table);
        return addObject(object,table);
    }

    public boolean removeObject(Object object, String table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(table, convertObjectToString(object));

        String whereClause = table+"=?";
        String[] whereArgs = new String[] {convertObjectToString(object)};

        long row = db.delete(table, whereClause,whereArgs);
        db.close();
        if(row == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean addObjectArrayList(ArrayList arrayList, String table)
    {
        clearTable(table);
        boolean successful = false;
        for (int i = 0; i < arrayList.size(); i++)
        {
            successful = addObject(arrayList.get(i), table);
            if (successful == false)
            {
                break;
            }
        }
        return successful;
    }


    public void clearTable(String table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table,null,null);
    }

    public Object getFirstObject(String table, Class endClass)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {table};
        Cursor cursor = db.query(table, columns, null, null, null, null, null);

        Object object = null;

        if (cursor.moveToFirst())
        {
            String data = cursor.getString(0);
            object = convertStringToObject(data,endClass);
        }
        else
        {
            if(endClass == int.class)
            {
                object = 0;
            }
            else if(endClass == boolean.class)
            {
                object = false;
            }
        }

        cursor.close();
        db.close();
        return object;
    }

    public ArrayList getObjects(String table, Class endClass)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {table};
        Cursor cursor = db.query(table, columns, null, null, null, null, null);

        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst())
        {
            do
            {
                String data = cursor.getString(0);
                Object object = convertStringToObject(data,endClass);
                arrayList.add(object);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return arrayList;
    }


    private String convertObjectToString(Object object)
    {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    private Object convertStringToObject(String string, Class endClass)
    {
        Gson gson = new Gson();
        return gson.fromJson(string , endClass);
    }


}
