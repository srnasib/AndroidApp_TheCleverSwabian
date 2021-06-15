package userlayer.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.thecleverswabian.thecleverswabian.R;

import logiclayer.controller.Constants;
import main.MainActivity;
import userlayer.tools.CustomAlertDialog;
import userlayer.tools.UserUtil;

public class ActivityRegister extends TemplateActivity implements Constants
{
    public static boolean changePasswordMode = true;

    private EditText xmlEditTextUsername;
    private EditText xmlEditTextPassword;
    private EditText xmlEditTextConfirmPassword;
    private EditText xmlEditTextOldPassword;
    private TextView xmlButtonConfirm;
    private Switch xmlSwitchUsername;
    private Switch xmlSwitchPassword;
    private TextView xmlButtonSkip;
    private ImageView xmlButtonRevealPassword;
    private ImageView xmlButtonRevealConfirmPassword;
    private ImageView xmlButtonRevealOldPassword;
    private LinearLayout xmlLayoutMain;
    private LinearLayout xmlLayoutOldPassword;


    private boolean nameForLogin = true;
    private boolean fullTextPassword = true;
    private boolean changedUsername = false;

    private int previousPasswordSetup = settingsManager.getPasswordSetup();
    private boolean changedPreviousPasswordSetup = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findXmlElements();
        configureOnClickListeners();
        updateUI();
    }

    protected void findXmlElements()
    {
        xmlEditTextUsername = findViewById(R.id.UI_Register_Edittext_Username);
        xmlEditTextPassword = findViewById(R.id.UI_Register_Edittext_Password);
        xmlEditTextConfirmPassword = findViewById(R.id.UI_Register_Edittext_ConfirmPassword);
        xmlEditTextOldPassword = findViewById(R.id.UI_Register_Edittext_OldPassword);
        xmlButtonConfirm = findViewById(R.id.UI_Register_Button_Register);
        xmlSwitchUsername = findViewById(R.id.UI_Register_Switch_Username);
        xmlSwitchPassword = findViewById(R.id.UI_Register_Switch_Password);
        xmlButtonSkip = findViewById(R.id.UI_Register_Button_Skip);
        xmlButtonRevealPassword = findViewById(R.id.UI_Register_Button_Password_Reveal);
        xmlButtonRevealConfirmPassword = findViewById(R.id.UI_Register_Button_ConfirmPassword_Reveal);
        xmlButtonRevealOldPassword = findViewById(R.id.UI_Register_Button_OldPassword_Reveal);
        xmlLayoutMain = findViewById(R.id.UI_Register_Layout_MainLayout);
        xmlLayoutOldPassword = findViewById(R.id.UI_Register_Layout_OldPassword);
    }


    protected void configureOnClickListeners()
    {
        xmlSwitchUsername.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                nameForLogin = isChecked;
                changedPreviousPasswordSetup = true;
                updateUI();
            }
        });
        xmlSwitchPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                fullTextPassword = isChecked;
                xmlEditTextPassword.setText("");
                xmlEditTextConfirmPassword.setText("");
                changedPreviousPasswordSetup = true;
                updateUI();
            }
        });

        xmlButtonConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                confirmRegistration();
            }
        });
        xmlButtonSkip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                skipRegistration();
            }
        });
        xmlButtonRevealPassword.setOnTouchListener(new View.OnTouchListener()
        {
            int start = 0;
            int end = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                revealPassword(event, start, end);
                return true;
            }
        });

        xmlButtonRevealConfirmPassword.setOnTouchListener(new View.OnTouchListener()
        {
            int start = 0;
            int end = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                revealConfirmPassword(event, start, end);
                return true;
            }
        });


        xmlButtonRevealOldPassword.setOnTouchListener(new View.OnTouchListener()
        {
            int start = 0;
            int end = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                revealOldPassword(event, start, end);
                return true;
            }
        });

    }


    private void revealPassword(MotionEvent event, int start, int end)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //reveal password on button push
                start = xmlEditTextPassword.getSelectionStart();
                end = xmlEditTextPassword.getSelectionEnd();
                xmlEditTextPassword.setTransformationMethod(null);
                xmlEditTextPassword.setSelection(start, end);
                break;
            case MotionEvent.ACTION_UP:
                //hide password again on button release
                xmlEditTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                xmlEditTextPassword.setSelection(start, end);
                break;
        }
    }

    private void revealConfirmPassword(MotionEvent event, int start, int end)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //reveal password on button push
                start = xmlEditTextConfirmPassword.getSelectionStart();
                end = xmlEditTextConfirmPassword.getSelectionEnd();
                xmlEditTextConfirmPassword.setTransformationMethod(null);
                xmlEditTextConfirmPassword.setSelection(start, end);
                break;
            case MotionEvent.ACTION_UP:
                //hide password again on button release
                xmlEditTextConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                xmlEditTextConfirmPassword.setSelection(start, end);
                break;
        }

    }

    private void revealOldPassword(MotionEvent event, int start, int end)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //reveal password on button push
                start = xmlEditTextOldPassword.getSelectionStart();
                end = xmlEditTextOldPassword.getSelectionEnd();
                xmlEditTextOldPassword.setTransformationMethod(null);
                xmlEditTextOldPassword.setSelection(start, end);
                break;
            case MotionEvent.ACTION_UP:
                //hide password again on button release
                xmlEditTextOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                xmlEditTextOldPassword.setSelection(start, end);
                break;
        }
    }


    private void updateUI()
    {
        if (settingsManager.getSetupProgress() == SETUP_PROGRESS_NONE)
        {
            this.setTitle("Welcome to the clever Swabian!");
        }
        else
        {
            this.setTitle("Security settings");
        }

        ActionBar actionBar = getSupportActionBar();
        if (changePasswordMode)
        {
            if (actionBar != null)
            {
                xmlButtonSkip.setClickable(false);
                xmlButtonSkip.setVisibility(View.INVISIBLE);
            }
            if(!changedPreviousPasswordSetup)
            {
                if(previousPasswordSetup == PASSWORD_SETUP_JUST_PASSWORD || previousPasswordSetup ==  PASSWORD_SETUP_JUST_PIN)
                {
                    xmlSwitchUsername.setChecked(false);
                    changedPreviousPasswordSetup = true;
                }
                if(previousPasswordSetup == PASSWORD_SETUP_JUST_PIN || previousPasswordSetup ==  PASSWORD_SETUP_USERNAME_AND_PIN)
                {
                    xmlSwitchPassword.setChecked(false);
                    changedPreviousPasswordSetup = true;
                }
            }
            if (settingsManager.getPasswordSetup() == PASSWORD_SETUP_DISABLED)
            {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) xmlLayoutOldPassword.getLayoutParams();
                params.weight = 0f;
                xmlLayoutOldPassword.setLayoutParams(params);
            }
        }
        else
        {
            if (actionBar != null)
            {
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowHomeEnabled(false);
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) xmlLayoutOldPassword.getLayoutParams();
            params.weight = 0f;
            xmlLayoutOldPassword.setLayoutParams(params);
        }

        if (!changedUsername)
        {
            xmlEditTextUsername.setText(settingsManager.getUsername());
            changedUsername = true;
        }

        if (fullTextPassword)
        {

            xmlEditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            xmlEditTextConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        else
        {
            xmlEditTextPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            xmlEditTextConfirmPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        }

        if (previousPasswordSetup == PASSWORD_SETUP_USERNAME_AND_PASSWORD || previousPasswordSetup == PASSWORD_SETUP_USERNAME_AND_PIN)
        {
            xmlEditTextOldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        else
        {
            xmlEditTextOldPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        }

        if (nameForLogin)
        {
            xmlSwitchUsername.setChecked(true);
        }

        if (fullTextPassword)
        {
            xmlSwitchPassword.setChecked(true);
        }

        if (!xmlEditTextUsername.getText().toString().equals(""))
        {
            changedUsername = true;
        }




    }

    private void confirmRegistration()
    {
        String userName = xmlEditTextUsername.getText().toString();
        String password = xmlEditTextPassword.getText().toString();
        String confirmPassword = xmlEditTextConfirmPassword.getText().toString();

        if (userName.length() == 0)
        {
            UserUtil.displayText("Please enter a username.");
            xmlEditTextUsername.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(xmlEditTextUsername, InputMethodManager.SHOW_IMPLICIT);
            return;
        }
        if (changePasswordMode && settingsManager.getPasswordSetup() != PASSWORD_SETUP_DISABLED)
        {
            String oldPassword = xmlEditTextOldPassword.getText().toString();
            if (!settingsManager.checkPassword(oldPassword))
            {
                UserUtil.displayText("Old password is not matching!");
                xmlEditTextOldPassword.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                imm.showSoftInput(xmlEditTextOldPassword, InputMethodManager.SHOW_IMPLICIT);
                return;
            }
        }


        if (password.length() < 4)
        {
            UserUtil.displayText("The minimum password length is 4.");
            xmlEditTextPassword.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(xmlEditTextPassword, InputMethodManager.SHOW_IMPLICIT);
            return;
        }

        if (!password.equals(confirmPassword))
        {
            UserUtil.displayText("The password is not matching!");
            xmlEditTextConfirmPassword.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(xmlEditTextConfirmPassword, InputMethodManager.SHOW_IMPLICIT);
            return;
        }


        settingsManager.setPasswordSetup(nameForLogin, fullTextPassword);
        settingsManager.setPassword(password);
        settingsManager.setUsername(userName);

        MainActivity.loggedIn = true;

        if (changePasswordMode)
        {
            ActivitySetupSecurityQuestions.changePasswordMode = true;
        }
        else
        {
            settingsManager.setSetupProgress(Constants.SETUP_PROGRESS_REGISTRATION);
        }
        changeActivity(ActivitySetupSecurityQuestions.class);
    }

    private void skipRegistration()
    {
        CustomAlertDialog alertDialog = new CustomAlertDialog(getActivity());
        alertDialog.setMessage("By not setting up password protection, your personal payment data might be revealed to others.");

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Resume", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                settingsManager.setPasswordSetup(PASSWORD_SETUP_DISABLED);
                settingsManager.setSetupProgress(Constants.SETUP_PROGRESS_SECURITY_QUESTIONS);
                MainActivity.loggedIn = true;
                changeActivity(MainActivity.class);
            }
        });
        alertDialog.show();
    }


    @Override
    public void onDestroy()
    {
        changePasswordMode = true;
        super.onDestroy();
    }


}
