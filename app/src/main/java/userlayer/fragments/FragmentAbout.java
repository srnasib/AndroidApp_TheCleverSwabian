package userlayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.thecleverswabian.thecleverswabian.R;

public class FragmentAbout extends TemplateFragment
{

    private Button xmlButtonContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle("About");
        view = inflater.inflate(R.layout.fragment_about, container, false);

        findXmlElements();
        configureOnClickListeners();

        return view;
    }

    protected void findXmlElements()
    {
        xmlButtonContact = view.findViewById(R.id.UI_About_Button_Contact);
    }

    protected void configureOnClickListeners()
    {
        xmlButtonContact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");

                String[] recipient = {"md1.rahman@st.ovgu.de"};
                String subject = "Issue related to The clever Swabian";
                emailIntent.putExtra(Intent.EXTRA_EMAIL, recipient);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

                startActivity(emailIntent);
            }
        });

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