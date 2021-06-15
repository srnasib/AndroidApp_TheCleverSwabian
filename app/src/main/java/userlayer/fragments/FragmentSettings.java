package userlayer.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.thecleverswabian.thecleverswabian.R;

import userlayer.activities.ActivityDateFormat;
import userlayer.activities.ActivityManageCategories;
import userlayer.activities.ActivityRegister;
import userlayer.tools.CustomAlertDialog;

public class FragmentSettings extends TemplateFragment
{
    private ListView xmlListSettings;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle("Settings");
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        findXmlElements();
        configureOnClickListeners();
        setAdapters();
        return view;
    }

    protected void findXmlElements()
    {
        xmlListSettings =  view.findViewById(R.id.UI_Settings_ListView);
    }



    protected void configureOnClickListeners()
    {
        xmlListSettings.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        ActivityRegister.changePasswordMode = true;
                        changeActivity(ActivityRegister.class);
                        break;
                    case 2:
                        changeActivity(ActivityDateFormat.class);
                        break;
                    case 3:
                        changeActivity(ActivityManageCategories.class);
                        break;


                    case 5:
                        askForDeletingAllEntries();
                        break;
                }
            }
        });
    }


    private void setAdapters()
    {
        String[] items = new String[]{"Security settings", "Change currency", "Change date format", "Manage categories", "Change language", "Clear all entries"};
        ArrayAdapter<String> adapterItems = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        xmlListSettings.setAdapter(adapterItems);

    }

    @Override
    public void onDetach()
    {
        if(isRemoving()){
            changeFragment(R.id.nav_home);
        }
        super.onDetach();
    }

    private void askForDeletingAllEntries()
    {
        CustomAlertDialog alertDialog = new CustomAlertDialog(getActivity());
        alertDialog.setMessage("This will delete ALL of your entries and recurring entries.");

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Resume", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                CustomAlertDialog alertDialog = new CustomAlertDialog(getActivity());
                alertDialog.setMessage("Last change to not delete all entries!");

                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Resume", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        entryManager.removeAllEntries();
                        recurringEntryManager.removeAllRecurringEntries();
                    }
                });
                alertDialog.show();
            }
        });
        alertDialog.show();
    }




}