package logiclayer.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import datalayer.DataBaseAccess;
import logiclayer.model.Category;
import logiclayer.model.Entry;
import logiclayer.model.RecurringEntry;
import logiclayer.tools.LogicUtil;

public class RecurringEntryManager implements ManagerAccess, DataBaseAccess
{
    private static ArrayList<RecurringEntry> recurringEntries;
    private static Date lastUpdated;

    //allows only one object at once
    private static volatile RecurringEntryManager instance;

    private RecurringEntryManager()
    {
        lastUpdated = LogicUtil.getToday();
        loadRecurringEntries();
        checkForAutoAddingEntries();
    }

    static RecurringEntryManager getInstance()
    {
        if (instance == null)
        {
            synchronized (RecurringEntryManager.class)
            {
                if (instance == null)
                {
                    instance = new RecurringEntryManager();
                }
            }
        }
        return instance;
    }

    public ArrayList<RecurringEntry> getRecurringEntries()
    {
        //always sorted by start date

        Collections.sort(recurringEntries, new Comparator<RecurringEntry>()
        {
            public int compare(RecurringEntry s1, RecurringEntry s2)
            {
                return s1.getDate().compareTo(s2.getDate());
            }
        });
        Collections.reverse(recurringEntries);

        return recurringEntries;
    }

    public ArrayList<RecurringEntry> getSortedRecurringEntries(boolean order, Comparator<RecurringEntry> comparator)
    {
        ArrayList<RecurringEntry> sortedRecurringEntries = recurringEntries;

        Collections.sort(sortedRecurringEntries, comparator);

        if (order)
        {
            Collections.reverse(sortedRecurringEntries);
        }

        return sortedRecurringEntries;
    }

    //returns operation success
    public boolean addRecurringEntry(RecurringEntry recurringEntry)
    {
        boolean successful = recurringEntries.add(recurringEntry);
        if (successful)
        {
            checkForAutoAddingSingleEntry(recurringEntry);
            saveNewRecurringEntry(recurringEntry);
        }
        return successful;
    }

    //returns operation success
    public boolean updateRecurringEntry(RecurringEntry updatedRecurringEntry, RecurringEntry oldRecurringEntry)
    {
        return removeRecurringEntry(oldRecurringEntry) && addRecurringEntry(updatedRecurringEntry);
    }

    //returns operation success
    public boolean removeRecurringEntry(int position)
    {
        if(position>=recurringEntries.size())
        {
            return false;
        }

        databaseHelper.removeObject(recurringEntries.get(position),databaseHelper.TABLE_RECURRING_ENTRIES);
        recurringEntries.remove(position);
        return true;
    }

    //returns operation success
    public boolean removeRecurringEntry(RecurringEntry recurringEntry)
    {
        for (int i = 0; i < recurringEntries.size(); i++)
        {
            if (recurringEntries.get(i).equals(recurringEntry))
            {
                databaseHelper.removeObject(recurringEntry,databaseHelper.TABLE_RECURRING_ENTRIES);
                recurringEntries.remove(i);
                return true;
            }
        }
        return false;
    }

    public void removeAllRecurringEntries()
    {
        recurringEntries.clear();
        databaseHelper.clearTable(databaseHelper.TABLE_RECURRING_ENTRIES);
    }

    //returns operation success
    private boolean saveAndOverwriteRecurringEntries()
    {
        return databaseHelper.addObjectArrayList(recurringEntries,databaseHelper.TABLE_RECURRING_ENTRIES);
    }

    //returns operation success
    private boolean saveNewRecurringEntry(RecurringEntry recurringEntry)
    {
        return databaseHelper.addObject(recurringEntry,databaseHelper.TABLE_RECURRING_ENTRIES);
    }

    private void loadRecurringEntries()
    {
        recurringEntries = (ArrayList<RecurringEntry>) databaseHelper.getObjects(databaseHelper.TABLE_RECURRING_ENTRIES,RecurringEntry.class);
    }

    public void checkForCheckingForAutoAddingEntries()
    {
        Date today = LogicUtil.getToday();
        if (today.after(lastUpdated))
        {
            lastUpdated = today;
            checkForAutoAddingEntries();
        }

    }

    private void checkForAutoAddingEntries()
    {
        for (int i = 0; i < recurringEntries.size(); i++)
        {
            checkForAutoAddingSingleEntry(recurringEntries.get(i));
        }
        saveAndOverwriteRecurringEntries();
    }

    private void checkForAutoAddingSingleEntry(RecurringEntry recurringEntry)
    {
        Date startDate = recurringEntry.getDate();
        Date endDate = recurringEntry.getEndDate();
        Date today = LogicUtil.getToday();

        boolean entryAdded;

        do
        {
            Date lastEntryCreation = recurringEntry.getLastEntryCreation();
            entryAdded = false;

            if (lastEntryCreation == null)
            {
                if (LogicUtil.isDate1AfterOrEqualToDate2(today, startDate))
                {
                    autoAddEntry(recurringEntry, startDate);
                    recurringEntry.setLastEntryCreation(startDate);
                    entryAdded = true;
                }
            }
            else
            {
                Date nextDate = LogicUtil.addToDate(lastEntryCreation, recurringEntry.getInterval(), startDate);

                if (endDate != null)
                {
                    if (nextDate.after(endDate))
                    {
                        return;
                    }
                }

                if (LogicUtil.isDate1AfterOrEqualToDate2(today, nextDate))
                {
                    autoAddEntry(recurringEntry, nextDate);
                    recurringEntry.setLastEntryCreation(nextDate);
                    entryAdded = true;
                }
            }
        } while (entryAdded);
    }

    private void autoAddEntry(RecurringEntry recurringEntry, Date date)
    {
        Entry newEntry = recurringEntry.castToEntry(date);
        entryManager.addEntry(newEntry);
    }


    public void removeCategoryFromRecurringEntries(Category category)
    {
        for (int i = 0; i < recurringEntries.size(); i++)
        {
            RecurringEntry recurringEntry = recurringEntries.get(i);
            if (recurringEntry.getCategory().equals(category))
            {
                ArrayList<Category> categories = CategoryManager.getInstance().getCategories(recurringEntry.getType());
                recurringEntry.setCategory(categories.get(categories.size() - 1));
            }


        }

    }

}

