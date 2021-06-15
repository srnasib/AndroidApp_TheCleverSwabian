package userlayer.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.thecleverswabian.thecleverswabian.R;

import logiclayer.controller.SettingsManager;
import userlayer.tools.ListAdapterWithPreselectionAndIcons;

public class ActivityDateFormat extends TemplateActivity
{

    private ListView xmlListDateFormats;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setTitle("Change date format");
        setContentView(R.layout.activity_dateformat);

        findXmlElements();
        setAdapters();
        configureOnClickListeners();
    }

    protected void findXmlElements()
    {
        xmlListDateFormats = findViewById(R.id.UI_DateFormat_List);

    }

    private void setAdapters()
    {
        ListAdapterWithPreselectionAndIcons adapter = new ListAdapterWithPreselectionAndIcons(getActivity(), SettingsManager.DATEFORMATS, settingsManager.getDateFormat());
        xmlListDateFormats.setAdapter(adapter);

        xmlListDateFormats.setSelection(settingsManager.getDateFormat());
        xmlListDateFormats.setItemChecked(settingsManager.getDateFormat(), true);
        xmlListDateFormats.setFocusable(true);
        xmlListDateFormats.setFocusableInTouchMode(true);
    }

    protected void configureOnClickListeners()
    {
        xmlListDateFormats.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                settingsManager.setDateFormat(position);
                finish();
            }
        });
    }

}
