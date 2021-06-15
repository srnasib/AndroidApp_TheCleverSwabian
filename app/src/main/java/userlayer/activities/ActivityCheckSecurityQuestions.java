package userlayer.activities;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.thecleverswabian.thecleverswabian.R;

import logiclayer.controller.Constants;
import userlayer.tools.UserUtil;

public class ActivityCheckSecurityQuestions extends TemplateActivity implements Constants
{
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
        this.setTitle("Answer security questions");
        setContentView(R.layout.activity_security_questions);

        findXmlElements();
        configureOnClickListeners();
        setupUI();
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

    }


    private void setupUI()
    {
        xmlButtonDismiss.setClickable(false);
        xmlButtonDismiss.setVisibility(View.INVISIBLE);

        xmlEditTextQuestion1.setText(settingsManager.getSecurityQuestions()[0]);
        xmlEditTextQuestion1.setFocusable(false);
        xmlEditTextQuestion2.setText(settingsManager.getSecurityQuestions()[1]);
        xmlEditTextQuestion2.setFocusable(false);
    }

    private void confirm()
    {
        String answer1 = xmlEditTextAnswer1.getText().toString();
        String answer2 = xmlEditTextAnswer2.getText().toString();

        if(!settingsManager.checkSecurityQA1(answer1))
        {
            UserUtil.displayText("Please enter the correct answer for the first question.");
            xmlEditTextAnswer1.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(xmlEditTextAnswer1, InputMethodManager.SHOW_IMPLICIT);
            return;
        }

        if(!settingsManager.checkSecurityQA2(answer2))
        {
            UserUtil.displayText("Please enter the correct answer for the second question.");
            xmlEditTextAnswer2.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(xmlEditTextAnswer2, InputMethodManager.SHOW_IMPLICIT);
            return;
        }

        settingsManager.setPasswordSetup(Constants.PASSWORD_SETUP_DISABLED);
        ActivityRegister.changePasswordMode = false;
        changeActivity(ActivityRegister.class);

    }



}
