package userlayer.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CustomAlertDialog extends AlertDialog
{

    public CustomAlertDialog(Context context)
    {
        super(context);

        this.setTitle("Attention!");

        this.setButton(BUTTON_NEUTRAL,"Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

    }
}


