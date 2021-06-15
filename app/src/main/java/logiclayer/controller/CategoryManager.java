package logiclayer.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import datalayer.DataBaseAccess;
import logiclayer.model.Category;

public class CategoryManager implements DataBaseAccess, ManagerAccess
{
    private static ArrayList<Category> expenseCategories;
    private static ArrayList<Category> incomeCategories;
    private static Category defaultExpenseCategory;
    private static Category defaultIncomeCategory;

    //allows only one object at once
    private static volatile CategoryManager instance;

    private CategoryManager()
    {
        loadCategories();
        if (expenseCategories.size() == 0)
        {
            revertToPresetExpenseCategories();
        }
        if (incomeCategories.size() == 0)
        {
            revertToPresetIncomeCategories();
        }
    }

    static CategoryManager getInstance()
    {
        if (instance == null)
        {
            synchronized (CategoryManager.class)
            {
                if (instance == null)
                {
                    instance = new CategoryManager();
                }
            }
        }
        return instance;
    }

    public ArrayList<Category> getCategories(boolean type)
    {
        if (!type)
        {
            return expenseCategories;
        }
        else
        {
            return incomeCategories;
        }
    }


    public Category getDefaultCategory(boolean type)
    {
        if (!type)
        {
            return defaultExpenseCategory;
        }
        else
        {
            return defaultIncomeCategory;
        }
    }

    public int getDefaultCategoryPosition(boolean type)
    {
        if (!type)
        {
            for (int i = 0; i < expenseCategories.size(); i++)
            {
                if (expenseCategories.get(i).getName().equals(defaultExpenseCategory.getName()))
                {
                    return i;
                }
            }
        }
        else
        {
            for (int i = 0; i < incomeCategories.size(); i++)
            {
                if (incomeCategories.get(i).getName().equals(defaultIncomeCategory.getName()))
                {
                    return i;
                }
            }
        }
        //not successful
        return -1;
    }

    public String[] getCategoryNames(boolean type)
    {
        if (!type)
        {
            String[] expenseCategoryNames = new String[expenseCategories.size()];
            for (int i = 0; i < expenseCategories.size(); i++)
            {
                expenseCategoryNames[i] = expenseCategories.get(i).getName();
            }
            return expenseCategoryNames;
        }
        else
        {
            String[] incomeCategoryNames = new String[incomeCategories.size()];
            for (int i = 0; i < incomeCategories.size(); i++)
            {
                incomeCategoryNames[i] = incomeCategories.get(i).getName();
            }
            return incomeCategoryNames;
        }
    }

    public int[] getCategoryIcons(boolean type)
    {
        if (!type)
        {
            int[] expenseCategoryIcons = new int[expenseCategories.size()];
            for (int i = 0; i < expenseCategories.size(); i++)
            {
                expenseCategoryIcons[i] = expenseCategories.get(i).getIconAdress();
            }
            return expenseCategoryIcons;
        }
        else
        {
            int[] incomeCategoryIcons = new int[incomeCategories.size()];
            for (int i = 0; i < incomeCategories.size(); i++)
            {
                incomeCategoryIcons[i] = incomeCategories.get(i).getIconAdress();
            }
            return incomeCategoryIcons;
        }
    }

    //returns operation success
    private boolean addCategoryWithoutSaving(final Category category)
    {
        if (!category.getType())
        {
            return expenseCategories.add(category);
        }
        else
        {
            return incomeCategories.add(category);
        }
    }

    //returns operation success
    public boolean addCategory(Category category)
    {
        boolean successful = addCategoryWithoutSaving(category);

        if (successful)
        {
            sortCategories(category.getType());
        }
        return successful;
    }

    public boolean checkIfNameExists(boolean type, String name)
    {
        String[] names = getCategoryNames(type);
        for(int i=0;i<names.length;i++)
        {
            if(names[i].equals(name))
            {
                return true;
            }
        }
        return false;
    }

    private void sortCategories(boolean type)
    {
        //alphabetic sorting of categories
        Comparator comparator = new Comparator<Category>()
        {
            public int compare(Category category1, Category category2)
            {
                int compare = category1.getNotDeletable().compareTo(category2.getNotDeletable());

                if (compare == 0)
                {
                    compare = category1.getName().compareTo(category2.getName());
                }
                return compare;
            }
        };
        if (!type)
        {
            Collections.sort(expenseCategories, comparator);
        }
        else
        {
            Collections.sort(incomeCategories, comparator);
        }
        saveAndOverwriteCategories();
    }

    //returns operation success
    public boolean removeCategory(int position, boolean type)
    {
        if (!type)
        {
            if (position >= expenseCategories.size())
            {
                return false;
            }
            Category toRemove = expenseCategories.get(position);
            databaseHelper.removeObject(toRemove, databaseHelper.TABLE_EXPENSE_CATEGORIES);
            entryManager.removeCategoryFromEntries(toRemove);
            recurringEntryManager.removeCategoryFromRecurringEntries(toRemove);
            expenseCategories.remove(position);
        }
        else
        {
            if (position >= incomeCategories.size())
            {
                return false;
            }

            Category toRemove = incomeCategories.get(position);
            databaseHelper.removeObject(toRemove, databaseHelper.TABLE_INCOME_CATEGORIES);
            entryManager.removeCategoryFromEntries(toRemove);
            recurringEntryManager.removeCategoryFromRecurringEntries(toRemove);
            incomeCategories.remove(position);
        }
        return true;
    }


    //returns operation success
    public boolean removeCategory(Category category)
    {
        if (!category.getType())
        {
            for (int i = 0; i < expenseCategories.size(); i++)
            {
                if (expenseCategories.get(i).getName().equals(category.getName()))
                {
                    databaseHelper.removeObject(category, databaseHelper.TABLE_EXPENSE_CATEGORIES);
                    expenseCategories.remove(i);
                    return true;
                }
            }
        }
        else
        {
            for (int i = 0; i < incomeCategories.size(); i++)
            {
                if (incomeCategories.get(i).getName().equals(category.getName()))
                {
                    databaseHelper.removeObject(category, databaseHelper.TABLE_INCOME_CATEGORIES);
                    incomeCategories.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    //returns operation success
    public boolean setDefaultCategory(int position, boolean type)
    {
        if (!type)
        {
            if (position >= expenseCategories.size())
            {
                return false;
            }
            defaultExpenseCategory = expenseCategories.get(position);
        }
        else
        {
            if (position >= incomeCategories.size())
            {
                return false;
            }
            defaultIncomeCategory = incomeCategories.get(position);

        }
        saveDefaultCategory(type);
        return true;
    }

    private boolean saveDefaultCategory(boolean type)
    {
        if(!type)
        {
            return databaseHelper.clearAndAddObject(defaultExpenseCategory, databaseHelper.TABLE_DEFAULT_EXPENSE_CATEGORY);
        }
        else
        {
            return databaseHelper.clearAndAddObject(defaultIncomeCategory, databaseHelper.TABLE_DEFAULT_INCOME_CATEGORY);
        }
    }


    //returns operation success
    private boolean saveAndOverwriteCategories()
    {
        boolean successful = true;
        successful = successful && databaseHelper.addObjectArrayList(expenseCategories, databaseHelper.TABLE_EXPENSE_CATEGORIES);
        successful = successful && databaseHelper.addObjectArrayList(incomeCategories, databaseHelper.TABLE_INCOME_CATEGORIES);

        successful = successful && databaseHelper.clearAndAddObject(defaultExpenseCategory, databaseHelper.TABLE_DEFAULT_EXPENSE_CATEGORY);
        successful = successful && databaseHelper.clearAndAddObject(defaultIncomeCategory, databaseHelper.TABLE_DEFAULT_INCOME_CATEGORY);

        return successful;
    }

    private void loadCategories()
    {
        expenseCategories = databaseHelper.getObjects(databaseHelper.TABLE_EXPENSE_CATEGORIES, Category.class);
        incomeCategories = databaseHelper.getObjects(databaseHelper.TABLE_INCOME_CATEGORIES, Category.class);

        defaultExpenseCategory = (Category) databaseHelper.getFirstObject(databaseHelper.TABLE_DEFAULT_EXPENSE_CATEGORY, Category.class);
        defaultIncomeCategory = (Category) databaseHelper.getFirstObject(databaseHelper.TABLE_DEFAULT_INCOME_CATEGORY, Category.class);
    }


    public void revertToPresetCategories(boolean type)
    {
        if (!type)
        {
            revertToPresetExpenseCategories();
        }
        else
        {
            revertToPresetIncomeCategories();
        }
    }

    private void revertToPresetExpenseCategories()
    {
        for(int i=0;i<expenseCategories.size();i++)
        {
            Category category = expenseCategories.get(i);

            if(category.getUserCreated())
            {
                entryManager.removeCategoryFromEntries(category);
                recurringEntryManager.removeCategoryFromRecurringEntries(category);
            }
        }
        expenseCategories.clear();

        addCategoryWithoutSaving(new Category(false, "Automobile", "icon_automobile",false));
        addCategoryWithoutSaving(new Category(false, "Childcare", "icon_childcare",false));
        addCategoryWithoutSaving(new Category(false, "Clothing", "icon_clothing",false));
        addCategoryWithoutSaving(new Category(false, "Education", "icon_education",false));
        addCategoryWithoutSaving(new Category(false, "Food", "icon_food",false));
        defaultExpenseCategory = expenseCategories.get(expenseCategories.size() - 1);
        addCategoryWithoutSaving(new Category(false, "Gambling", "icon_gamble",false));
        addCategoryWithoutSaving(new Category(false, "Gift", "icon_gift",false));
        addCategoryWithoutSaving(new Category(false, "Hobbies", "icon_puzzle",false));
        addCategoryWithoutSaving(new Category(false, "Household", "icon_clean",false));
        addCategoryWithoutSaving(new Category(false, "Leisure", "icon_leisure",false));
        addCategoryWithoutSaving(new Category(false, "Loans/Fees", "icon_bill",false));
        addCategoryWithoutSaving(new Category(false, "Housing", "icon_home",false));
        addCategoryWithoutSaving(new Category(false, "Sports", "icon_bike",false));
        addCategoryWithoutSaving(new Category(false, "Transportation", "icon_transportation",false));
        addCategoryWithoutSaving(new Category(false, "Vacation", "icon_palm",false));
        addCategoryWithoutSaving(new Category(false, "Other", "icon_points",false, true));
        sortCategories(false);
    }

    private void revertToPresetIncomeCategories()
    {
        for(int i=0;i<incomeCategories.size();i++)
        {
            Category category = incomeCategories.get(i);

            if(category.getUserCreated())
            {
                entryManager.removeCategoryFromEntries(category);
                recurringEntryManager.removeCategoryFromRecurringEntries(category);
            }
        }
        incomeCategories.clear();

        addCategoryWithoutSaving(new Category(true, "Gambling", "icon_gamble",false));
        addCategoryWithoutSaving(new Category(true, "Gift", "icon_gift",false));
        addCategoryWithoutSaving(new Category(true, "Rent", "icon_home",false));
        addCategoryWithoutSaving(new Category(true, "Salary", "icon_business",false));
        defaultIncomeCategory = incomeCategories.get(incomeCategories.size() - 1);
        addCategoryWithoutSaving(new Category(true, "Social benefit", "icon_give",false));
        addCategoryWithoutSaving(new Category(true, "Yield", "icon_bank",false));
        addCategoryWithoutSaving(new Category(true, "Other", "icon_points",false, true));
        sortCategories(true);
    }

    public void setOtherAsDefault(boolean type)
    {
        if (!type)
        {
            defaultExpenseCategory = expenseCategories.get(expenseCategories.size() - 1);
        }
        else
        {
            defaultIncomeCategory = incomeCategories.get(incomeCategories.size() - 1);
        }
        saveDefaultCategory(type);
    }


    public int getCategoryPosition(Category category)
    {
        if (!category.getType())
        {
            for (int i = 0; i < expenseCategories.size(); i++)
            {
                if (expenseCategories.get(i).getName().equals(category.getName()))
                {
                    return i;
                }
            }
        }
        else
        {
            for (int i = 0; i < incomeCategories.size(); i++)
            {
                if (incomeCategories.get(i).getName().equals(category.getName()))
                {
                    return i;
                }
            }
        }
        //not successful
        return -1;
    }



}
