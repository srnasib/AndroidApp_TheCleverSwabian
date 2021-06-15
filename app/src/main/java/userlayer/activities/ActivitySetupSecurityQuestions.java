package userlayer.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.thecleverswabian.thecleverswabian.R;

import logiclayer.controller.Constants;
import main.MainActivity;
import userlayer.tools.CustomAlertDialog;
import userlayer.tools.UserUtil;

public class ActivitySetupSecurityQuestions extends TemplateActivity implements Constants
{
    public static boolean changePasswordMode = false;

    EditText xmlEditTextQuestion1;
    EditText xmlEditTextAnswer1;
    EditText xmlEditTextQuestion2;
    EditText xmlEditTextAnswer2;
    TextView xmlButtonConfirm;
    TextView xmlButtonDismiss;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setTitle("Security question setup");
        setContentView(R.layout.activity_security_questions);

        findXmlElements();
        configureOnClickListeners();
        restrictQuestionInput();
        updateUI();
    }

    protected void findXmlElements()
    {
        xmlEditTextQuestion1 = findViewById(R.id.UI_SecurityQuestions_Edittext_Question1);
        xmlEditTextAnswer1 = findViewById(R.id.UI_SecurityQuestions_Edittext_Answer1);
        xmlEditTextQuestion2 = findViewById(R.id.UI_SecurityQuestions_Edittext_Question2);
        xmlEditTextAnswer2 = findViewById(R.id.UI_SecurityQuestions_Edittext_Answer2);
        xmlButtonConfirm = findViewById(R.id.UI_SecurityQuestions_Button_Confirm);
        xmlButtonDismiss = findViewById(R.id.UI_SecurityQuestions_Button_Dismiss);
    }


    protected void configureOnClickListeners()
    {
        xmlButtonConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                confirm();
            }
        });
        xmlButtonDismiss.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dismiss();
            }
        });


    }


    private void restrictQuestionInput()
    {

        TextWatcher textWatcher = new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            public void afterTextChanged(Editable s)
            {
                for (int i = s.length(); i > 0; i--)
                {
                    if (s.subSequence(i - 1, i).toString().equals("\n"))
                    {
                        s.replace(i - 1, i, "");
                    }
                }
                String myTextString = s.toString();
            }
        };

        xmlEditTextQuestion1.addTextChangedListener(textWatcher);
        xmlEditTextQuestion2.addTextChangedListener(textWatcher);
    }


    private void confirm()
    {
        String question1 = xmlEditTextQuestion1.getText().toString();
        String question2 = xmlEditTextQuestion2.getText().toString();
        String answer1 = xmlEditTextAnswer1.getText().toString();
        String answer2 = xmlEditTextAnswer2.getText().toString();

        if (question1.length() < 2)
        {
            UserUtil.displayText("Please enter a proper security question.");
            xmlEditTextQuestion1.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(xmlEditTextQuestion1, InputMethodManager.SHOW_IMPLICIT);
            return;
        }
        if (answer1.length() < 1)
        {
            UserUtil.displayText("Please enter a proper answer to your security question.");
            xmlEditTextAnswer1.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(xmlEditTextAnswer1, InputMethodManager.SHOW_IMPLICIT);
            return;
        }
        if (question2.length() < 2)
        {
            UserUtil.displayText("Please enter a proper security question.");
            xmlEditTextQuestion2.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(xmlEditTextQuestion2, InputMethodManager.SHOW_IMPLICIT);
            return;
        }
        if (answer2.length() < 1)
        {
            UserUtil.displayText("Please enter a proper answer to your security question.");
            xmlEditTextAnswer2.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(xmlEditTextAnswer2, InputMethodManager.SHOW_IMPLICIT);
            return;
        }

        settingsManager.setSecurityQAs(question1, question2, answer1, answer2);
        settingsManager.setSetupProgress(SETUP_PROGRESS_SECURITY_QUESTIONS);
        UserUtil.displayText("Setup finished!");
        if(changePasswordMode)
        {
            finish();
        }
        else
        {
            changeActivity(MainActivity.class);
        }
    }

    private void dismiss()
    {
        CustomAlertDialog alertDialog = new CustomAlertDialog(getActivity());
        alertDialog.setMessage("By not setting up security questions, you will lose your data when forgetting the password!");

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Resume", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                UserUtil.displayText("Setup finished!");
                if(changePasswordMode)
                {
                    finish();
                }
                else
                {
                    settingsManager.setSetupProgress(SETUP_PROGRESS_SECURITY_QUESTIONS);
                    changeActivity(MainActivity.class);
                }
            }
        });
        alertDialog.show();

    }

    private void updateUI()
    {
        ActionBar actionBar = getSupportActionBar();
        if (!changePasswordMode)
        {
            if (actionBar != null)
            {
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowHomeEnabled(false);
            }
        }
        else
        {
            if(settingsManager.getSecurityQASetup())
            {
                String question1 = settingsManager.getSecurityQuestions()[0];
                String question2 = settingsManager.getSecurityQuestions()[1];

                xmlEditTextQuestion1.setText(question1);
                xmlEditTextQuestion2.setText(question2);

                settingsManager.setSecurityQAs("","","","");
            }
        }




    }

    @Override
    public void onBackPressed()
    {
        if(changePasswordMode)
        {
            dismiss();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy()
    {
        changePasswordMode = false;
        super.onDestroy();
    }
}
