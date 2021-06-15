package userlayer.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.thecleverswabian.thecleverswabian.R;

import logiclayer.model.Category;
import logiclayer.model.Entry;
import userlayer.fragments.FragmentAddNewEntry;
import userlayer.tools.CustomAlertDialog;
import userlayer.tools.ListAdapterWithPreselectionAndIcons;

public class ActivityManageCategories extends TemplateActivity
{

    private ListView xmlListExpenseCategories;
    private ListView xmlListIncomeCategories;
    private LinearLayout xmlButtonRevertExpenseCategories;
    private LinearLayout xmlButtonRevertIncomeCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);
        getSupportActionBar().setTitle("Manage Categories");

        findXmlElements();
        configureOnClickListeners();

        setAdapters();
    }

    protected void findXmlElements()
    {
        xmlListExpenseCategories = findViewById(R.id.UI_ManageCategories_List_ExpenseCategories);
        xmlListIncomeCategories =  findViewById(R.id.UI_ManageCategories_List_IncomeCategories);

        xmlButtonRevertExpenseCategories = findViewById(R.id.UI_ManageCategories_Button_RevertExpenseCategories);
        xmlButtonRevertIncomeCategories = findViewById(R.id.UI_ManageCategories_Button_RevertIncomeCategories);
    }


    protected void configureOnClickListeners()
    {
        xmlButtonRevertExpenseCategories.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                askForRevertingList(false);
            }
        });

        xmlButtonRevertIncomeCategories.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                askForRevertingList(true);
            }
        });

        xmlListExpenseCategories.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                askForCategoryModifying(false, position);
            }
        });

        xmlListIncomeCategories.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                askForCategoryModifying(true, position);
            }
        });
    }

    private void askForRevertingList(final boolean type)
    {
        CustomAlertDialog alertDialog = new CustomAlertDialog(getActivity());
        String typeName;
        if (!type)
        {
            typeName = "expense";
        }
        else
        {
            typeName = "income";
        }
        alertDialog.setMessage("This will convert all custom " + typeName + " categories into 'Other'.");

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Resume", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                categoryManager.revertToPresetCategories(type);
                setAdapters();
            }
        });

        alertDialog.show();
    }

    private void askForCategoryModifying(final boolean type, final int position)
    {
        CustomAlertDialog alertDialog = new CustomAlertDialog(getActivity());
        alertDialog.setTitle("Modify '" + categoryManager.getCategoryNames(type)[position] + "'");

        if (!categoryManager.getCategories(type).get(position).getNotDeletable())
        {
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Delete", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    askForDeleting(type, position);
                }
            });
        }
        if (categoryManager.getDefaultCategoryPosition(type) != position)
        {
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Set as Default", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    setCategoryAsDefault(type, position);
                }
            });
        }
        alertDialog.show();
    }

    private void askForDeleting(final boolean type, final int position)
    {
        CustomAlertDialog alertDialog = new CustomAlertDialog(getActivity());
        alertDialog.setMessage("This will convert all entries of this category into 'Other'.");

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Resume", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                deleteCategory(type, position);
            }
        });
        alertDialog.show();
    }


    private void deleteCategory(boolean type, int position)
    {
        if (categoryManager.getDefaultCategoryPosition(type) == position)
        {
            categoryManager.setOtherAsDefault(type);
        }

        Category category = categoryManager.getCategories(type).get(position);
        Entry entry = FragmentAddNewEntry.getEntry();
        if (entry != null)
        {
            if (!entry.getType() && entry.getCategory().getName().equals(category.getName()))
            {
                entry.setCategory(categoryManager.getDefaultCategory(false));
                FragmentAddNewEntry.setEntry(entry);
            }
        }

        categoryManager.removeCategory(position, type);
        saveScrollPosition(type);
    }

    private void setCategoryAsDefault(boolean type, int position)
    {
        categoryManager.setDefaultCategory(position, type);
        saveScrollPosition(type);
    }

    private void saveScrollPosition(boolean type)
    {
        if (!type)
        {
            int index = xmlListExpenseCategories.getFirstVisiblePosition();
            View listView = xmlListExpenseCategories.getChildAt(0);
            int top = (listView == null) ? 0 : (listView.getTop() - xmlListExpenseCategories.getPaddingTop());
            setAdapters();
            xmlListExpenseCategories.setSelectionFromTop(index, top);
        }
        else
        {
            int index = xmlListIncomeCategories.getFirstVisiblePosition();
            View listView = xmlListIncomeCategories.getChildAt(0);
            int top = (listView == null) ? 0 : (listView.getTop() - xmlListIncomeCategories.getPaddingTop());
            setAdapters();
            xmlListIncomeCategories.setSelectionFromTop(index, top);
        }
    }


    private void setAdapters()
    {
        String[] expenseCategoryNames = categoryManager.getCategoryNames(false);
        String[] incomeCategoryNames = categoryManager.getCategoryNames(true);

        int[] expenseCategoryIcons = categoryManager.getCategoryIcons(false);
        int[] incomeCategoryIcons = categoryManager.getCategoryIcons(true);

        int defaultExpenseCategory = categoryManager.getDefaultCategoryPosition(false);
        int defaultIncomeCategory = categoryManager.getDefaultCategoryPosition(true);

        ListAdapterWithPreselectionAndIcons expenseCategoriesAdapter = new ListAdapterWithPreselectionAndIcons(this, expenseCategoryNames, expenseCategoryIcons, defaultExpenseCategory);
        ListAdapterWithPreselectionAndIcons incomeCategoriesAdapter = new ListAdapterWithPreselectionAndIcons(this, incomeCategoryNames, incomeCategoryIcons, defaultIncomeCategory);

        xmlListExpenseCategories.setAdapter(expenseCategoriesAdapter);
        xmlListIncomeCategories.setAdapter(incomeCategoriesAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_add:
                changeActivity(ActivityAddNewCategory.class);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }


}

