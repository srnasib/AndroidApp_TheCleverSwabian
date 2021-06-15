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

import logiclayer.model.Entry;
import userlayer.tools.EntryRecyclingAdapter;

public class FragmentListOfEntries extends TemplateFragment
{
    private RecyclerView xmlRecyclerViewEntryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_list_of_entries, container, false);
        getActivity().setTitle("List of entries");

        findXmlElements();
        configureOnClickListeners();
        setHasOptionsMenu(true);
        generateEntryList(inflater,container);

        return view;
    }

    protected void findXmlElements()
    {
        xmlRecyclerViewEntryList = view.findViewById(R.id.UI_ListOfEntries_RecyclerView);
    }


    protected void configureOnClickListeners()
    {
    }

    private void generateEntryList(LayoutInflater inflater, ViewGroup container)
    {
        ArrayList<Entry> entryList = entryManager.getEntries();


        Comparator comparator = new Comparator<Entry>()
        {
            public int compare(Entry entry1, Entry entry2)
            {
                int compare = entry1.getDate().compareTo(entry2.getDate());
                if(compare == 0)
                {
                    compare = entry1.getModificationTime().compareTo(entry2.getModificationTime());
                }
                return compare;
            }
        };


        entryList = entryManager.getSortedEntries(true, comparator);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        xmlRecyclerViewEntryList.setLayoutManager(layoutManager);
        RecyclerView.Adapter mAdapter = new EntryRecyclingAdapter(getContext(), entryList);
        xmlRecyclerViewEntryList.setAdapter(mAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_add:
                FragmentAddNewEntry.setRecurring(false);
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
