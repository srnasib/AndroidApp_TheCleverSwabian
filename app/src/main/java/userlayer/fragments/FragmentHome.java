package userlayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thecleverswabian.thecleverswabian.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import logiclayer.model.Entry;
import logiclayer.tools.LogicUtil;
import userlayer.activities.ActivitySendPdf;
import userlayer.tools.UserUtil;

public class FragmentHome extends TemplateFragment
{
    public static double sumOfExpenses;
    public static double sumOfIncome;

    private Button xmlButtonAddNewEntry;
    private TextView xmlTextExpenseSum;
    private TextView xmlTextIncomeSum;
    private LinearLayout xmlLayoutEntry1;
    private LinearLayout xmlLayoutEntry2;
    private LinearLayout xmlLayoutEntry3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle("Home of the clever Swabian");
        view = inflater.inflate(R.layout.fragment_home, container, false);

        findXmlElements();
        configureOnClickListeners();
        setHasOptionsMenu(true);
        generateSums();
        displayRecentEntries(inflater, container);


        return view;
    }

    protected void findXmlElements()
    {
        xmlButtonAddNewEntry =  view.findViewById(R.id.UI_Home_Button_AddNewEntry);
        xmlTextExpenseSum =  view.findViewById(R.id.UI_Home_Text_Expenses);
        xmlTextIncomeSum =  view.findViewById(R.id.UI_Home_Text_Income);
        xmlLayoutEntry1 =  view.findViewById(R.id.UI_Home_Layout_Entry1);
        xmlLayoutEntry2 =  view.findViewById(R.id.UI_Home_Layout_Entry2);
        xmlLayoutEntry3 =  view.findViewById(R.id.UI_Home_Layout_Entry3);
    }

    protected void configureOnClickListeners()
    {
        xmlButtonAddNewEntry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentAddNewEntry.setCameFromRecurringScreen(false);
                changeFragment(R.id.nav_addNewEntry);
            }
        });
    }

    private void generateSums()
    {
        Comparator comparator = new Comparator<Entry>()
        {
            public int compare(Entry entry1, Entry entry2)
            {
                return entry1.getDate().compareTo(entry2.getDate());
            }
        };

        ArrayList<Entry> entries = entryManager.getSortedEntries(true, comparator);

        sumOfExpenses = 0;
        sumOfIncome = 0;

        Date today = LogicUtil.getToday();

        Date aMonthAgo = LogicUtil.addToDate(today, Calendar.MONTH, -1);
        //prevents a monthly entry from being added twice
        aMonthAgo = LogicUtil.addToDate(aMonthAgo, Calendar.DATE, 1);

        for (int i = 0; i < entries.size(); i++)
        {
            Entry entry = (Entry) entries.get(i);

            Date entryDate = entry.getDate();

            if (LogicUtil.isDate2BetweenOrEqualDate1AndDate3(aMonthAgo, entryDate, today))
            {
                if (!entry.getType())
                {
                    sumOfExpenses = sumOfExpenses + entry.getAmount();
                }
                else
                {
                    sumOfIncome = sumOfIncome + entry.getAmount();
                }
            }
            //stop looping through too old entries
            else if(aMonthAgo.after(entryDate))
            {
                break;
            }
        }
        xmlTextExpenseSum.setText("- " + String.format("%.2f", sumOfExpenses) + " €");
        xmlTextIncomeSum.setText("+ " + String.format("%.2f", sumOfIncome) + " €");
    }

    private void displayRecentEntries(LayoutInflater inflater, ViewGroup container)
    {
        //sort by modificationTime, not entry date!
        Comparator comparator = new Comparator<Entry>()
        {
            public int compare(Entry entry1, Entry entry2)
            {
                return entry1.getModificationTime().compareTo(entry2.getModificationTime());
            }
        };
        ArrayList<Entry> entries = entryManager.getSortedEntries(true, comparator);

        if (entries.size() > 0)
        {
            Entry entry1 = entries.get(0);
            View entryView1 = generateElementEntry(entry1, inflater, container, getContext());
            setEntryListener(entryView1, entry1);
            xmlLayoutEntry1.addView(entryView1);
        }
        if (entries.size() > 1)
        {
            Entry entry2 = entries.get(1);
            View entryView2 = generateElementEntry(entry2, inflater, container, getContext());
            setEntryListener(entryView2, entry2);
            xmlLayoutEntry2.addView(entryView2);
        }
        if (entries.size() > 2)
        {
            Entry entry3 = entries.get(2);
            View entryView3 = generateElementEntry(entry3, inflater, container, getContext());
            setEntryListener(entryView3, entry3);
            xmlLayoutEntry3.addView(entryView3);
        }
    }


    private void setEntryListener(View entryView, final Entry entry)
    {
        entryView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentAddNewEntry.setEntry(entry);
                FragmentAddNewEntry.setEditMode(true);
                changeFragment(R.id.nav_addNewEntry);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.send, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_send:
                changeActivity(ActivitySendPdf.class);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private View generateElementEntry(final Entry entry, LayoutInflater inflater, ViewGroup container, Context context)
    {
        TextView dateText;
        TextView titleText;
        TextView category;
        TextView paymentMethod;
        TextView amount;
        View entryView;
        String prefix = "- ";

        entryView = inflater.inflate(R.layout.element_entry, container, false);
        dateText = entryView.findViewById(R.id.UI_ElementEntry_Text_Date);
        titleText = entryView.findViewById(R.id.UI_ElementEntry_Text_Title);
        category = entryView.findViewById(R.id.UI_ElementEntry_Text_Category);
        paymentMethod = entryView.findViewById(R.id.UI_ElementEntry_Text_PaymentMethod);
        amount = entryView.findViewById(R.id.UI_ElementEntry_Text_Amount);

        //If the entry is an income, center the category
        if (entry.getType())
        {
            prefix = "+ ";
            paymentMethod.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0f));
            category.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 100f));
            amount.setTextColor(context.getResources().getColor(R.color.colorTextGreen));
        }

        //If there's no title, center the date
        if (entry.getTitle().matches(""))
        {
            titleText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0f));
            dateText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 100f));
            dateText.setGravity(Gravity.LEFT | Gravity.CENTER);
        }

        amount.setText(prefix + String.format("%.2f", entry.getAmount()) + " €");
        dateText.setText(UserUtil.dateToString(entry.getDate()));
        titleText.setText(entry.getTitle());
        category.setText(entry.getCategory().getName());
        category.setCompoundDrawablesWithIntrinsicBounds(entry.getCategory().getIconAdress(), 0, 0, 0);

        if(!entry.getType())
        {
            paymentMethod.setText(entry.getPaymentMethod().getName());
            paymentMethod.setCompoundDrawablesWithIntrinsicBounds(entry.getPaymentMethod().getIconAdress(), 0, 0, 0);
        }

        entryView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return entryView;
    }


}