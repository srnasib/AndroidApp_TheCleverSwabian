package main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;

import com.thecleverswabian.thecleverswabian.R;

import logiclayer.controller.Constants;
import logiclayer.controller.ManagerAccess;
import userlayer.activities.ActivityLogin;
import userlayer.activities.ActivityRegister;
import userlayer.activities.ActivitySendPdf;
import userlayer.activities.ActivitySetupSecurityQuestions;
import userlayer.fragments.FragmentAbout;
import userlayer.fragments.FragmentAddNewEntry;
import userlayer.fragments.FragmentGraphicalOverview;
import userlayer.fragments.FragmentHome;
import userlayer.fragments.FragmentListOfEntries;
import userlayer.fragments.FragmentRecurringEntries;
import userlayer.fragments.FragmentSettings;
import userlayer.fragments.TemplateFragment;

public class MainActivity extends AppCompatActivity implements TemplateFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener, ManagerAccess, Constants
{
    public static Context context;
    public static Activity activity;
    public static boolean loggedIn = false;

    public static Context getContext()
    {
        return context;
    }
    public static Activity getActivity()
    {
        return activity;
    }

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.LoadingTheme);
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);

        context = getApplicationContext();
        activity = this;

        setContentView(R.layout.activity_main);
        findXmlElements();
        configureNavAndActionBar();
        recurringEntryManager.checkForCheckingForAutoAddingEntries();

        startFirstScreen();
    }

    private void configureNavAndActionBar()
    {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    private void changeActivity(Class activity)
    {
        Intent i = new Intent(this, activity);
        startActivity(i);
        finish();
    }


    private void startFirstScreen()
    {
        int setupProgress = settingsManager.getSetupProgress();

        switch (setupProgress)
        {
            case SETUP_PROGRESS_NONE:
                ActivityRegister.changePasswordMode = false;
                changeActivity(ActivityRegister.class);
                return;
            case SETUP_PROGRESS_REGISTRATION:
                changeActivity(ActivitySetupSecurityQuestions.class);
                loggedIn = true;
                return;
        }

        int passwordSetup = settingsManager.getPasswordSetup();

        if(loggedIn)
        {
            changeFragment(R.id.nav_home);
        }
        else
        {
            switch (passwordSetup)
            {
                case PASSWORD_SETUP_DISABLED:
                    changeFragment(R.id.nav_home);
                    return;
                default:
                    changeActivity(ActivityLogin.class);
                    return;
            }
        }
    }



    private void findXmlElements()
    {
        toolbar = findViewById(R.id.UI_Toolbar);
        drawer = findViewById(R.id.UI_NavDrawer);
        navigationView = findViewById(R.id.UI_NavView);
    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        Fragment fragment = null;
        String fragmentID = "";

        Pair pair = changeTab(id,fragment,fragmentID);
        fragment = (Fragment) pair.first;
        fragmentID = (String) pair.second;

        configureAddNewEntryTab(id);

        if (fragment != null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(fragmentID);
            ft.replace(R.id.UI_MainFrame, fragment, fragmentID);
            ft.commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Pair<Fragment,String> changeTab(int id, Fragment fragment, String fragmentID)
    {
        switch (id)
        {
            case R.id.nav_home:
            {
                fragment = new FragmentHome();
                fragmentID = "fragment_home";
                break;
            }
            case R.id.nav_addNewEntry:
            {
                fragment = new FragmentAddNewEntry();
                fragmentID = "fragment_addNewEntry";
                break;
            }
            case R.id.nav_listOfEntries:
            {
                fragment = new FragmentListOfEntries();
                fragmentID = "fragment_listOfEntries";
                break;
            }
            case R.id.nav_recurringEntries:
            {
                fragment = new FragmentRecurringEntries();
                fragmentID = "fragment_recurringEntries";
                break;
            }
            case R.id.nav_graphicalOverview:
            {
                fragment = new FragmentGraphicalOverview();
                fragmentID = "fragment_graphicalOverview";
                break;
            }
            case R.id.nav_send_pdf:
            {
                changeActivity(ActivitySendPdf.class);
                break;
            }
            case R.id.nav_settings:
            {
                fragment = new FragmentSettings();
                fragmentID = "fragment_settings";
                break;
            }
            case R.id.nav_about:
            {
                fragment = new FragmentAbout();
                fragmentID = "fragment_about";
                break;
            }
            case R.id.nav_exit:
            {
                this.finish();
                break;
            }
        }
        return new Pair<Fragment,String>(fragment,fragmentID);
    }


    private void configureAddNewEntryTab(int id)
    {
        Fragment fragmentRecurringEntries = getSupportFragmentManager().findFragmentByTag("fragment_recurringEntries");
        if (id == R.id.nav_addNewEntry && fragmentRecurringEntries != null && fragmentRecurringEntries.isVisible())
        {
            FragmentAddNewEntry.setCameFromRecurringScreen(true);
            FragmentAddNewEntry.setRecurring(true);
        }
        else
        {
            FragmentAddNewEntry.setCameFromRecurringScreen(false);
        }

        Fragment fragmentListofEntries = getSupportFragmentManager().findFragmentByTag("fragment_listOfEntries");
        if (fragmentListofEntries != null && fragmentListofEntries.isVisible())
        {
            FragmentAddNewEntry.setCameFromListOfEntriesScreen(true);
        }
        else
        {
            FragmentAddNewEntry.setCameFromListOfEntriesScreen(false);
        }
    }


    @Override
    public void onFragmentInteraction(String title)
    {
        getSupportActionBar().setTitle(title);
    }

    public static void changeFragment(int id)
    {
        NavigationView navigationView = getActivity().findViewById(R.id.UI_NavView);
        navigationView.setCheckedItem(id);
        navigationView.getMenu().performIdentifierAction(id, 0);
    }
}

