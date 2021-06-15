package userlayer.activities;

import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.thecleverswabian.thecleverswabian.R;

import logiclayer.controller.Constants;
import main.MainActivity;
import userlayer.tools.UserUtil;

public class ActivityLogin extends TemplateActivity implements Constants
{
    EditText xmlEditTextUsername;
    EditText xmlEditTextPassword;
    Button xmlButtonLogin;
    TextView xmlButtonResetPassword;
    ImageView xmlButtonRevealPassword;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setTitle("Login");
        setContentView(R.layout.activity_login);

        findXmlElements();
        configureOnClickListeners();
        updateUI();
    }

    protected void findXmlElements()
    {
        xmlEditTextUsername = findViewById(R.id.UI_Login_Edittext_Username);
        xmlEditTextPassword =  findViewById(R.id.UI_Login_Edittext_Password);
        xmlButtonLogin = findViewById(R.id.UI_Login_Button_Login);
        xmlButtonResetPassword = findViewById(R.id.UI_Login_Text_Register);
        xmlButtonRevealPassword = findViewById(R.id.UI_Login_Button_Password_Reveal);

    }

    private void updateUI()
    {
        if(!settingsManager.getSecurityQASetup())
        {
            xmlButtonResetPassword.setClickable(false);
            xmlButtonResetPassword.setVisibility(View.INVISIBLE);
        }


        int passwordSetup = settingsManager.getPasswordSetup();

        if(passwordSetup == PASSWORD_SETUP_USERNAME_AND_PIN || passwordSetup == PASSWORD_SETUP_JUST_PIN)
        {
            xmlEditTextPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        }
        else
        {
            xmlEditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        if(passwordSetup == PASSWORD_SETUP_JUST_PASSWORD || passwordSetup ==  PASSWORD_SETUP_JUST_PIN)
        {
            xmlEditTextUsername.setText(settingsManager.getUsername());
            xmlEditTextUsername.setFocusable(false);
        }
    }

    protected void configureOnClickListeners()
    {
        xmlEditTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    xmlButtonLogin.performClick();
                }
                return false;
            }
        });

        xmlButtonResetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                changeActivity(ActivityCheckSecurityQuestions.class);
            }
        });

        xmlButtonLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                checkLogin();
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


    private void checkLogin()
    {
        String username = xmlEditTextUsername.getText().toString();
        String password = xmlEditTextPassword.getText().toString();
        int passwordSetup = settingsManager.getPasswordSetup();

        if(passwordSetup == PASSWORD_SETUP_USERNAME_AND_PASSWORD || passwordSetup == PASSWORD_SETUP_USERNAME_AND_PIN)
        {
            if (!settingsManager.getUsername().equals(username))
            {
                UserUtil.displayText("Username not matching!");
                xmlEditTextUsername.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                imm.showSoftInput(xmlEditTextUsername, InputMethodManager.SHOW_IMPLICIT);
                return;
            }
        }
        if(!settingsManager.checkPassword(password))
        {
            UserUtil.displayText("Password not matching!");
            xmlEditTextPassword.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(xmlEditTextPassword, InputMethodManager.SHOW_IMPLICIT);
            return;
        }
        MainActivity.loggedIn = true;
        changeActivity(MainActivity.class);

    }

}
