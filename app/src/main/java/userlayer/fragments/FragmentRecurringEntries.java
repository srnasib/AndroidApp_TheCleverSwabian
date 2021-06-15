package userlayer.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.thecleverswabian.thecleverswabian.R;

import java.util.ArrayList;
import java.util.Comparator;

import logiclayer.model.RecurringEntry;
import userlayer.tools.RecurringEntryRecyclingAdapter;

public class FragmentRecurringEntries extends TemplateFragment
{
    private RecyclerView xmlRecyclerViewRecurringEntryList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle("Recurring entries");
        view = inflater.inflate(R.layout.fragment_recurring_entries, container, false);

        findXmlElements();
        configureOnClickListeners();
        displayRecurringEntries(inflater, container);
        setHasOptionsMenu(true);

        return view;
    }


    protected void findXmlElements()
    {
        xmlRecyclerViewRecurringEntryList =  view.findViewById(R.id.UI_RecurringEntries_RecyclerView);
    }

    protected void configureOnClickListeners()
    {
    }

    private void displayRecurringEntries(LayoutInflater inflater, ViewGroup container)
    {
        ArrayList<RecurringEntry> recurringEntryList = recurringEntryManager.getRecurringEntries();
        Comparator comparator = new Comparator<RecurringEntry>()
        {
            public int compare(RecurringEntry s1, RecurringEntry s2)
            {
                return s1.getDate().compareTo(s2.getDate());
            }
        };
        recurringEntryList = recurringEntryManager.getSortedRecurringEntries(true, comparator);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        xmlRecyclerViewRecurringEntryList.setLayoutManager(layoutManager);
        RecyclerView.Adapter mAdapter = new RecurringEntryRecyclingAdapter(getContext(), recurringEntryList);
        xmlRecyclerViewRecurringEntryList.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_add:
                FragmentAddNewEntry.setRecurring(true);
                changeFragment(R.id.nav_addNewEntry);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_add, menu);
    }

    @Override
    public void onDetach()
    {
        if(isRemoving()){
            changeFragment(R.id.nav_home);
        }
        super.onDetach();
    }
}