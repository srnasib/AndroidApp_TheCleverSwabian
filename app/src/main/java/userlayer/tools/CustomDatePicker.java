package userlayer.tools;

import android.app.DatePickerDialog;
import android.content.Context;

import com.thecleverswabian.thecleverswabian.R;

public class CustomDatePicker extends DatePickerDialog
{

    public CustomDatePicker(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth, boolean neutralButton)
    {
        super(context, R.style.DateTheme, callBack, year, monthOfYear, dayOfMonth);

        setButton(BUTTON_POSITIVE, ("OK"), this);
        setButton(BUTTON_NEGATIVE, ("CANCEL"), this);
        if(neutralButton)
        {
            setButton(BUTTON_NEUTRAL, ("NONE"), this);
        }

    }
}


