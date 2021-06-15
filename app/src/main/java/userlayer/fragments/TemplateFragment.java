package userlayer.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import logiclayer.controller.ManagerAccess;
import main.MainActivity;


public abstract class TemplateFragment extends Fragment implements ManagerAccess
{
    protected View view;


    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(String title);
    }

    protected abstract void findXmlElements();

    protected abstract void configureOnClickListeners();

    public void changeFragment(int id)
    {
        MainActivity.changeFragment(id);
    }

    public void changeActivity(Class activity)
    {
        Intent i = new Intent(getActivity(), activity);
        startActivity(i);
    }

}
