package userlayer.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.thecleverswabian.thecleverswabian.R;

import logiclayer.model.Category;
import logiclayer.tools.LogicUtil;

public class ActivityAddNewCategory extends TemplateActivity
{

    private boolean type;

    private TextView xmlButtonType;
    private EditText xmlEditTextName;
    private TextView xmlTextError;
    private ImageView xmlImageIcon;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setTitle("Add new category");
        setContentView(R.layout.activity_add_new_category);

        findXmlElements();
        configureOnClickListeners();
        updateUI();

    }

    protected void findXmlElements()
    {
        xmlButtonType = findViewById(R.id.UI_AddNewCategory_Button_Type);
        xmlEditTextName = findViewById(R.id.UI_AddNewCategory_EditText_Name);
        xmlTextError = findViewById(R.id.UI_AddNewCategory_Text_Error);
        xmlImageIcon = findViewById(R.id.UI_AddNewCategory_Image_Icon);

    }


    protected void configureOnClickListeners()
    {
        xmlButtonType.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                type=!type;
                updateUI();
            }
        });

    }

    private void updateUI()
    {
        xmlTextError.setText("");
        if (!type)
        {
            xmlButtonType.setText("Expense");
            xmlButtonType.setBackgroundResource(R.drawable.button_red);
        }
        else
        {
            xmlButtonType.setText("Income");
            xmlButtonType.setBackgroundResource(R.drawable.button_green);
        }
    }

    private void saveCategory()
    {
        String name = xmlEditTextName.getText().toString();
        name = LogicUtil.capitalizeString(name);

        if(name.length()<3)
        {
            xmlTextError.setText("minimum length: 3 characters");
            return;
        }
        if(categoryManager.checkIfNameExists(type,name))
        {
            xmlTextError.setText("name already exists");
            return;
        }

        xmlTextError.setText("");
        Category category = new Category(type,name, "icon_points",true);
        categoryManager.addCategory(category);
        changeActivity(ActivityManageCategories.class);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_save:
                saveCategory();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed()
    {
        changeActivity(ActivityManageCategories.class);
        super.onBackPressed();
    }
}
