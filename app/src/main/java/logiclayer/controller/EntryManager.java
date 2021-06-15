package logiclayer.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import datalayer.DataBaseAccess;
import logiclayer.model.Category;
import logiclayer.model.Entry;


public class EntryManager implements DataBaseAccess
{
    private static ArrayList<Entry> entries;

    //allows only one object at once
    private static volatile EntryManager instance;

    private EntryManager()
    {
        loadEntries();
    }

    static EntryManager getInstance()
    {
        if (instance == null)
        {
            synchronized (EntryManager.class)
            {
                if (instance == null)
                {
                    instance = new EntryManager();
                }
            }
        }
        return instance;
    }

    public ArrayList<Entry> getEntries()
    {
        return entries;
    }


    //returns operation success
    public boolean addEntry(Entry entry)
    {
        boolean successful = entries.add(entry);
        if (successful)
        {
            saveNewEntry(entry);
        }
        return successful;
    }

    //returns operation success
    public boolean removeEntry(int position)
    {
        if(position>=entries.size())
        {
            return false;
        }
        else
        {
            databaseHelper.removeObject(entries.get(position),databaseHelper.TABLE_ENTRIES);
            entries.remove(position);
            return true;
        }
    }

    //returns operation success
    public boolean removeEntry(Entry entry)
    {
        for (int i = 0; i < entries.size(); i++)
        {
            if (entries.get(i).equals(entry))
            {
                databaseHelper.removeObject(entry,databaseHelper.TABLE_ENTRIES);
                entries.remove(i);
                return true;
            }
        }
        return false;
    }

    //returns operation success
    public boolean updateEntry(Entry updatedEntry, Entry oldEntry)
    {
        return removeEntry(oldEntry) && addEntry(updatedEntry);
    }

    public void removeAllEntries()
    {
        entries.clear();
        databaseHelper.clearTable(databaseHelper.TABLE_ENTRIES);
    }

    //returns operation success
    private boolean saveAndOverwriteEntries()
    {
        return databaseHelper.addObjectArrayList(entries,databaseHelper.TABLE_ENTRIES);
    }

    //returns operation success
    private boolean saveNewEntry(Entry entry)
    {
        return databaseHelper.addObject(entry,databaseHelper.TABLE_ENTRIES);
    }


    private void loadEntries()
    {
        entries = (ArrayList<Entry>) databaseHelper.getObjects(databaseHelper.TABLE_ENTRIES,Entry.class);
    }


    public ArrayList<Entry> getSortedEntries(boolean order, Comparator<Entry> comparator)
    {
        ArrayList<Entry> sortedEntries = entries;

        Collections.sort(sortedEntries, comparator);

        if (order)
        {
            Collections.reverse(sortedEntries);
        }

        return sortedEntries;
    }


    public ArrayList<Entry> getFilteredEntries()
    {
        //This method has to be filled with filtering logic
        return entries;
    }


    public void removeCategoryFromEntries(Category category)
    {
        for(int i=0;i<entries.size();i++)
        {
            Entry entry = entries.get(i);
            if(entry.getCategory().equals(category))
            {
                ArrayList<Category> categories = CategoryManager.getInstance().getCategories(entry.getType());
                entry.setCategory(categories.get(categories.size()-1));
            }


        }

    }
}

