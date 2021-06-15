package userlayer.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import logiclayer.controller.ManagerAccess;

public abstract class TemplateActivity extends AppCompatActivity implements ManagerAccess
{
    protected abstract void findXmlElements();

    protected abstract void configureOnClickListeners();



    public void changeActivity(Class activity)
    {
        Intent i = new Intent(this, activity);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();    //Call the back button's method
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Context getActivity()
    {
        return this;
    }
}
