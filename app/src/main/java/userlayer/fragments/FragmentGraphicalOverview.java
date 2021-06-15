package userlayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thecleverswabian.thecleverswabian.R;

public class FragmentGraphicalOverview extends TemplateFragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle("Graphical overview");

        view = inflater.inflate(R.layout.fragment_graphical_overview, container, false);

        findXmlElements();
        configureOnClickListeners();

        return view;
    }

    protected void findXmlElements()
    {
    }

    protected void configureOnClickListeners()
    {
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