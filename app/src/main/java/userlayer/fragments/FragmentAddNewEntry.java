package userlayer.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.thecleverswabian.thecleverswabian.R;

import java.util.Calendar;
import java.util.Date;

import logiclayer.controller.IntervalManager;
import logiclayer.model.Category;
import logiclayer.model.Entry;
import logiclayer.model.Interval;
import logiclayer.model.PaymentMethod;
import logiclayer.model.RecurringEntry;
import logiclayer.tools.LogicUtil;
import userlayer.activities.ActivityManageCategories;
import userlayer.tools.CustomDatePicker;
import userlayer.tools.DecimalInputTextWatcher;
import userlayer.tools.SpinnerAdapterWithIcons;
import userlayer.tools.UserUtil;

public class FragmentAddNewEntry extends TemplateFragment
{
    private static Entry entry;
    private static RecurringEntry recurringEntry;
    private static Entry editEntryCopy;
    private static RecurringEntry editRecurringEntryCopy;
    private static boolean recurring = false;
    private static boolean cameFromRecurringScreen = false;
    private static boolean cameFromListOfEntriesScreen = false;
    private static boolean editMode = false;

    private Date today;

    private TextView xmlTextCategory;
    private TextView xmlTextPaymentMethod;
    private ImageView xmlImageCategoryIcon;
    private ImageView xmlImagePaymentMethodIcon;
    private TextView xmlTextType;
    private ImageView xmlImageRecurringButton;
    private Button xmlButtonCancel;
    private ImageButton xmlButtonDelete;
    private Button xmlButtonConfirm;
    private LinearLayout xmlLayoutPaymentMethod;
    private LinearLayout xmlLayoutCategoryButton;
    private LinearLayout xmlLayoutPaymentMethodButton;
    private TextView xmlTextCategorySpinnerIcon;
    private TextView xmlTextPaymentMethodSpinnerIcon;
    private EditText xmlEditTextAmount;
    private EditText xmlEditTextTitle;
    private TextView xmlTextDate;
    private EditText xmlEditTextComment;
    private Spinner xmlSpinnerCategory;
    private Spinner xmlSpinnerPaymentMethod;
    private Spinner xmlSpinnerInterval;
    private TextView xmlTextDateInfo;
    private LinearLayout xmlLayoutComment;
    private LinearLayout xmlLayoutInterval;
    private LinearLayout xmlLayoutEndDate;
    private Space xmlSpaceBeforeComment;
    private Space xmlSpaceAfterComment;
    private TextView xmlTextEndDate;
    private TextView xmlTextInterval;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_add_new_entry, container, false);

        today = LogicUtil.getToday();

        initialiseEntryObjects();
        findXmlElements();
        configureOnClickListeners();
        setAdapters();
        updateUI();
        setHasOptionsMenu(true);

        return view;
    }


    @Override
    public void onDestroy()
    {
        if (editMode)
        {
            editMode = false;
            clearEntry();
        }
        else
        {
            bufferEntry();
        }
        super.onDestroy();
    }

    public void initialiseEntryObjects()
    {
        if (!editMode)
        {
            //Creates new entry object, prevents interfering with older entry objects
            if (entry != null)
            {
                entry = new Entry(entry);
            }
            else
            {
                entry = new Entry(today, "", false, 0, categoryManager.getDefaultCategory(false), paymentMethodManager.getDefaultPaymentMethod(), "", Calendar.getInstance().getTime());
            }

            if (recurringEntry != null)
            {
                recurringEntry = new RecurringEntry(recurringEntry);
            }
            else
            {
                recurringEntry = new RecurringEntry(entry, intervalManager.getDefaultInterval(), null);
            }
        }

    }

    protected void findXmlElements()
    {
        xmlTextCategory = view.findViewById(R.id.UI_AddNewEntry_Text_Category);
        xmlTextPaymentMethod =  view.findViewById(R.id.UI_AddNewEntry_Text_PaymentMethod);
        xmlImageCategoryIcon =  view.findViewById(R.id.UI_AddNewEntry_Image_CategoryIcon);
        xmlImagePaymentMethodIcon =  view.findViewById(R.id.UI_AddNewEntry_Image_PaymentMethod);
        xmlTextType =  view.findViewById(R.id.UI_AddNewEntry_Type_Type);
        xmlImageRecurringButton =  view.findViewById(R.id.UI_AddNewEntry_Button_Recurring);
        xmlButtonCancel =  view.findViewById(R.id.UI_AddNewEntry_Button_Cancel);
        xmlButtonDelete =  view.findViewById(R.id.UI_AddNewEntry_Button_Delete);
        xmlButtonConfirm =  view.findViewById(R.id.UI_AddNewEntry_Button_Confirm);
        xmlLayoutPaymentMethod =  view.findViewById(R.id.UI_AddNewEntry_Layout_PaymentMethodSection);
        xmlLayoutCategoryButton =  view.findViewById(R.id.UI_AddNewEntry_Layout_CategoryButton);
        xmlLayoutPaymentMethodButton =  view.findViewById(R.id.UI_AddNewEntry_Layout_PaymentMethodButton);
        xmlEditTextAmount =  view.findViewById(R.id.UI_AddNewEntry_EditText_Amount);
        xmlEditTextTitle = view.findViewById(R.id.UI_AddNewEntry_EditText_Title);
        xmlTextDate =  view.findViewById(R.id.UI_AddNewEntry_Text_Date);
        xmlEditTextComment =  view.findViewById(R.id.UI_AddNewEntry_EditText_Comment);
        xmlSpinnerCategory =  view.findViewById(R.id.UI_AddNewEntry_Spinner_CategorySpinner);
        xmlSpinnerPaymentMethod =  view.findViewById(R.id.UI_AddNewEntry_Spinner_PaymentMethodSpinner);
        xmlTextCategorySpinnerIcon =  view.findViewById(R.id.UI_AddNewEntry_Text_CategoryButtonSpinnerIcon);
        xmlTextPaymentMethodSpinnerIcon =  view.findViewById(R.id.UI_AddNewEntry_Text_PaymentMethodButtonSpinnerIcon);
        xmlTextDateInfo =  view.findViewById(R.id.UI_AddNewEntry_Text_DateInfo);
        xmlLayoutComment =  view.findViewById(R.id.UI_AddNewEntry_Layout_Comment);
        xmlLayoutInterval =  view.findViewById(R.id.UI_AddNewEntry_Layout_Interval);
        xmlLayoutEndDate =  view.findViewById(R.id.UI_AddNewEntry_Layout_EndDate);
        xmlSpaceBeforeComment =  view.findViewById(R.id.UI_AddNewEntry_Space_SpaceBeforeComment);
        xmlSpaceAfterComment =  view.findViewById(R.id.UI_AddNewEntry_Space_SpaceAfterComment);
        xmlTextEndDate =  view.findViewById(R.id.UI_AddNewEntry_Text_EndDate);
        xmlTextInterval =  view.findViewById(R.id.UI_AddNewEntry_Text_Interval);
        xmlSpinnerInterval =  view.findViewById(R.id.UI_AddNewEntry_Spinner_IntervalSpinner);
    }


    protected void configureOnClickListeners()
    {
        configureDateInput();
        configureButtonInput();
        configureSpinnerInput();
        configureAmountInput();
        configureCommentInput();
    }

    private void configureDateInput()
    {

        xmlTextDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day)
                    {
                        xmlTextDate.setText(UserUtil.dateToString(year, month, day));
                        entry.setDate(LogicUtil.intsToDate(year, month, day));

                        //if interval > end date - start date
                        if (recurring)
                        {
                            if (recurringEntry.getEndDate() != null && LogicUtil.addToDate(entry.getDate(), recurringEntry.getInterval().getType(), recurringEntry.getInterval().getMultiplicator()).after(recurringEntry.getEndDate()))
                            {
                                recurringEntry.setEndDate(LogicUtil.addToDate(entry.getDate(), recurringEntry.getInterval().getType(), recurringEntry.getInterval().getMultiplicator()));
                                updateUI();
                            }
                        }

                    }
                };

                datePickerPopUp(false, dateListener);
            }
        });

        xmlTextEndDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day)
                    {
                        xmlTextEndDate.setText(UserUtil.dateToString(year, month, day));
                        recurringEntry.setEndDate(LogicUtil.intsToDate(year, month, day));
                    }
                };

                datePickerPopUp(true, dateListener);
            }
        });
    }

    private void datePickerPopUp(boolean datePickerType, DatePickerDialog.OnDateSetListener dateListener)
    {
        //datePickerID = false: start date
        //datePickerID = true: end date

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new CustomDatePicker(getContext(), dateListener, year, month, day, datePickerType);
        datePickerDialog.show();

        if (datePickerType)
        {
            Date minDate = LogicUtil.addToDate(entry.getDate(), recurringEntry.getInterval());
            datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
            datePickerDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    xmlTextEndDate.setText("None");
                    recurringEntry.setEndDate(null);
                    datePickerDialog.cancel();
                }
            });
        }
        else
        {
            if (recurring)
            {
                Date minDate = getMinStartDate();
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
            }
            else
            {
                datePickerDialog.getDatePicker().setMinDate(LogicUtil.intsToDate(1900, 0, 1).getTime());
            }

        }
    }

    private Date getMinStartDate()
    {
        Date minDate = LogicUtil.addToDate(today, recurringEntry.getInterval().getType(), -recurringEntry.getInterval().getMultiplicator());
        return LogicUtil.addToDate(minDate, Calendar.DATE, 1);
    }

    private void configureButtonInput()
    {
        xmlLayoutCategoryButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                xmlSpinnerCategory.performClick();
            }
        });

        xmlTextCategorySpinnerIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                xmlSpinnerCategory.performClick();
            }
        });

        xmlLayoutPaymentMethodButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                xmlSpinnerPaymentMethod.performClick();
            }
        });

        xmlTextPaymentMethodSpinnerIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                xmlSpinnerPaymentMethod.performClick();
            }
        });

        xmlTextInterval.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                xmlSpinnerInterval.performClick();
            }
        });

        xmlButtonConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveEntry();
            }
        });

        xmlTextType.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!editMode)
                {
                    entry.setType(!entry.getType());
                    entry.setCategory(categoryManager.getDefaultCategory(entry.getType()));
                    entry.setPaymentMethod(paymentMethodManager.getDefaultPaymentMethod());
                    setAdapters();
                    updateUI();
                }
            }
        });

        xmlImageRecurringButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!editMode)
                {
                    recurring = !recurring;
                    updateUI();
                }
            }
        });

        xmlButtonCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (editMode)
                {
                    clearEntry();
                }
                else
                {
                    bufferEntry();
                }
                changeScreen(false,false);
            }
        });

        xmlButtonDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (editMode)
                {
                    deleteExistingEntry();
                }
                clearEntry();
                changeScreen(false,false);
            }
        });
    }

    private void changeScreen(boolean saved, boolean recurring)
    {
        editMode = false;
        if(saved)
        {
            if(recurring)
            {
                changeFragment(R.id.nav_recurringEntries);
            }
            else if(cameFromListOfEntriesScreen)
            {
                changeFragment(R.id.nav_listOfEntries);
            }
            else
            {
                changeFragment(R.id.nav_home);
            }
        }
        else
        {
            if(cameFromRecurringScreen)
            {
                changeFragment(R.id.nav_recurringEntries);
            }
            else if(cameFromListOfEntriesScreen)
            {
                changeFragment(R.id.nav_listOfEntries);
            }
            else
            {
                changeFragment(R.id.nav_home);
            }
        }
    }

    private void configureSpinnerInput()
    {
        AdapterView.OnItemSelectedListener categorySpinnerListener = new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                onCategorySpinnerItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        };
        xmlSpinnerCategory.setOnItemSelectedListener(categorySpinnerListener);

        AdapterView.OnItemSelectedListener paymentMethodSpinnerListener = new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                onPaymentMethodSpinnerItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        };
        xmlSpinnerPaymentMethod.setOnItemSelectedListener(paymentMethodSpinnerListener);

        AdapterView.OnItemSelectedListener intervalSpinnerListener = new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                onIntervalSpinnerItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        };
        xmlSpinnerInterval.setOnItemSelectedListener(intervalSpinnerListener);
    }

    private void configureAmountInput()
    {
        //limit length of input on both sides of decimal operator
        xmlEditTextAmount.setFilters(new InputFilter[]{new DecimalInputTextWatcher(5, 2)});

        //update to two decimal places after input has finished
        xmlEditTextAmount.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                updateUI();
            }
        });

        //only allow local decimal operator when entering amount
        xmlEditTextAmount.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (editable.toString().contains(UserUtil.getDecimalSeparator()))
                {
                    xmlEditTextAmount.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                }
                else
                {
                    xmlEditTextAmount.setKeyListener(DigitsKeyListener.getInstance("0123456789" + UserUtil.getDecimalSeparator()));
                }
            }
        });

    }

    private void configureCommentInput()
    {
        xmlEditTextComment.addTextChangedListener(new TextWatcher()
        {
            int curserPosition = 0;
            String textContainer = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                curserPosition = xmlEditTextComment.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                xmlEditTextComment.removeTextChangedListener(this);
                final int maxLinesComment = 4;

                if (xmlEditTextComment.getLineCount() > maxLinesComment)
                {
                    xmlEditTextComment.setText(textContainer);
                    xmlEditTextComment.setSelection(curserPosition);
                }
                else
                {
                    textContainer = xmlEditTextComment.getText().toString();
                }

                xmlEditTextComment.addTextChangedListener(this);
            }
        });

    }


    private void setAdapters()
    {
        SpinnerAdapterWithIcons adapterCategory = new SpinnerAdapterWithIcons(getActivity(), categoryManager.getCategoryNames(entry.getType()), categoryManager.getCategoryIcons(entry.getType()));
        SpinnerAdapterWithIcons adapaterPaymentMethod = new SpinnerAdapterWithIcons(getActivity(), paymentMethodManager.getPaymentMethodNames(), paymentMethodManager.getPaymentMethodIcons());
        SpinnerAdapterWithIcons adapterInterval = new SpinnerAdapterWithIcons(getActivity(), intervalManager.getIntervalNames());

        xmlSpinnerCategory.setAdapter(adapterCategory);
        xmlSpinnerPaymentMethod.setAdapter(adapaterPaymentMethod);
        xmlSpinnerInterval.setAdapter(adapterInterval);

        xmlSpinnerCategory.setSelection(categoryManager.getCategoryPosition(entry.getCategory()));

        if(!entry.getType())
        {
            xmlSpinnerPaymentMethod.setSelection(paymentMethodManager.getPaymentMethodPosition(entry.getPaymentMethod()));
        }

        if(recurringEntry.getInterval()!=null)
        {
            xmlSpinnerInterval.setSelection(intervalManager.getIntervalPosition(recurringEntry.getInterval()));
        }
        else
        {
            xmlSpinnerInterval.setSelection(IntervalManager.DEFAULT_INTERVAL);
        }




    }


    private void onCategorySpinnerItemSelected(int position)
    {
        if (entry != null)
        {
            Category previousCategory = entry.getCategory();
            entry.setCategory(categoryManager.getCategories(entry.getType()).get(position));


            if (previousCategory != entry.getCategory())
            {
                updateUI();
            }
        }
    }

    private void onPaymentMethodSpinnerItemSelected(int position)
    {
        if (entry != null)
        {
            PaymentMethod previousPaymentMethod = entry.getPaymentMethod();
                entry.setPaymentMethod(paymentMethodManager.getPaymentMethods().get(position));

            if (previousPaymentMethod != entry.getPaymentMethod())
            {
                updateUI();
            }
        }
    }

    private void onIntervalSpinnerItemSelected(int position)
    {
        if (recurringEntry != null)
        {
            Interval previousInterval = recurringEntry.getInterval();

                recurringEntry.setInterval(IntervalManager.INTERVAL_LIST.get(position));
                //if interval > end date - start date
                if (recurringEntry.getEndDate() != null && LogicUtil.addToDate(entry.getDate(), recurringEntry.getInterval()).after(recurringEntry.getEndDate()))
                {
                    recurringEntry.setEndDate(LogicUtil.addToDate(entry.getDate(), recurringEntry.getInterval()));
                }

                if (entry.getDate().before(getMinStartDate()))
                {
                    entry.setDate(getMinStartDate());
                }


            if (previousInterval != recurringEntry.getInterval())
            {
                updateUI();
            }
        }
    }

    protected void updateUI()
    {
        adaptToTypeSetting();
        adaptToRecurringSetting();
        updateGeneralUI();
    }

    private void adaptToTypeSetting()
    {
        if (!entry.getType())
        {
            xmlTextType.setText("Expense");
            xmlTextType.setBackgroundResource(R.drawable.button_red);
            xmlLayoutPaymentMethod.setVisibility(View.VISIBLE);

            xmlTextPaymentMethod.setText(entry.getPaymentMethod().getName());
            xmlImagePaymentMethodIcon.setImageResource(entry.getPaymentMethod().getIconAdress());
        }
        else
        {
            xmlTextType.setText("Income");
            xmlTextType.setBackgroundResource(R.drawable.button_green);
            xmlLayoutPaymentMethod.setVisibility(View.INVISIBLE);
        }
    }

    private void adaptToRecurringSetting()
    {
        if (recurring)
        {
            if (editMode)
            {
                getActivity().setTitle("Edit existing recurring entry");
            }
            else
            {
                getActivity().setTitle("Add new recurring entry");
            }
            xmlTextDateInfo.setText("Start date:");
            xmlImageRecurringButton.setColorFilter(getContext().getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_IN);
            xmlImageRecurringButton.setBackground(getContext().getResources().getDrawable(R.drawable.button_green));

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) xmlLayoutEndDate.getLayoutParams();
            params.weight = 10f;
            xmlLayoutEndDate.setLayoutParams(params);
            xmlLayoutInterval.setLayoutParams(params);

            params = (LinearLayout.LayoutParams) xmlLayoutComment.getLayoutParams();
            params.weight = 10f;
            xmlLayoutComment.setLayoutParams(params);

            params = (LinearLayout.LayoutParams) xmlSpaceBeforeComment.getLayoutParams();
            params.weight = 1f;
            xmlSpaceBeforeComment.setLayoutParams(params);
            params = (LinearLayout.LayoutParams) xmlSpaceAfterComment.getLayoutParams();
            params.weight = 2f;
            xmlSpaceAfterComment.setLayoutParams(params);

            if (recurringEntry.getEndDate() == null)
            {
                xmlTextEndDate.setText("None");
            }
            else
            {
                xmlTextEndDate.setText(UserUtil.dateToString(recurringEntry.getEndDate()));
            }

            xmlTextInterval.setText(recurringEntry.getInterval().getName());

        }
        else
        {
            if (editMode)
            {
                getActivity().setTitle("Edit existing entry");
            }
            else
            {
                getActivity().setTitle("Add new entry");
            }

            xmlTextDateInfo.setText("Date:");
            xmlImageRecurringButton.setColorFilter(getContext().getResources().getColor(R.color.colorTextGrey), PorterDuff.Mode.SRC_IN);
            xmlImageRecurringButton.setBackground(getContext().getResources().getDrawable(R.drawable.button_beige));

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) xmlLayoutEndDate.getLayoutParams();
            params.weight = 0;
            xmlLayoutEndDate.setLayoutParams(params);
            xmlLayoutInterval.setLayoutParams(params);

            params = (LinearLayout.LayoutParams) xmlLayoutComment.getLayoutParams();
            params.weight = 20f;
            xmlLayoutComment.setLayoutParams(params);

            params = (LinearLayout.LayoutParams) xmlSpaceBeforeComment.getLayoutParams();
            params.weight = 3f;
            xmlSpaceBeforeComment.setLayoutParams(params);
            params = (LinearLayout.LayoutParams) xmlSpaceAfterComment.getLayoutParams();
            params.weight = 10f;
            xmlSpaceAfterComment.setLayoutParams(params);

        }
    }

    private void updateGeneralUI()
    {

        xmlTextDate.setText(UserUtil.dateToString(entry.getDate()));
        xmlTextCategory.setText(entry.getCategory().getName());
        xmlImageCategoryIcon.setImageResource(entry.getCategory().getIconAdress());



        if (xmlEditTextTitle.getText().toString().matches(""))
        {
            xmlEditTextTitle.setText(entry.getTitle());
        }

        if (xmlEditTextComment.getText().toString().matches(""))
        {
            xmlEditTextComment.setText(entry.getComment());
        }

        String amountText = xmlEditTextAmount.getText().toString();
        if (entry.getAmount() != 0 && amountText.matches(""))
        {
            amountText = String.format("%.2f", entry.getAmount());
            if (UserUtil.getDecimalSeparator().equals(","))
            {
                amountText.replace(".", ",");
            }
            xmlEditTextAmount.setText(amountText);
        }
        if (entry.getAmount() == 0)
        {
            xmlEditTextAmount.setText("");
        }
        //format amount to two decimal places
        if (!amountText.matches(""))
        {
            amountText = String.format("%.2f", Double.parseDouble(amountText.replace(",", ".")));
            if (UserUtil.getDecimalSeparator().equals(","))
            {
                amountText.replace(".", ",");
            }
            xmlEditTextAmount.setText(amountText);

        }
    }

    private void saveEntry()
    {
        bufferEntry();
        if (xmlEditTextAmount.getText().toString().matches("") || entry.getAmount() == 0)
        {
            //no amount given
            xmlEditTextAmount.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(xmlEditTextAmount, InputMethodManager.SHOW_IMPLICIT);
        }
        else
        {
            if (editMode)
            {
                if (recurring)
                {
                    recurringEntry.overwriteEntryAttributes(entry);
                    recurringEntryManager.updateRecurringEntry(recurringEntry,editRecurringEntryCopy);
                }
                else
                {
                    entryManager.updateEntry(entry,editEntryCopy);
                }
            }
            else
            {
                if (recurring)
                {
                    recurringEntry.overwriteEntryAttributes(entry);
                    recurringEntryManager.addRecurringEntry(recurringEntry);
                }
                else
                {
                    entryManager.addEntry(entry);
                }
            }
            boolean savedAsRecurring = recurring;
            clearEntry();
            changeScreen(true,savedAsRecurring);

        }
    }

    public void bufferEntry()
    {
        double amount;
        if (xmlEditTextAmount.getText().toString().matches(""))
        {
            amount = 0;
        }
        else
        {
            amount = Double.parseDouble(xmlEditTextAmount.getText().toString().replace(",", "."));
            //cuts off after two decimal places
            amount = Math.round(100.0 * amount) / 100.0;
        }
        entry.setModificationTime(Calendar.getInstance().getTime());
        entry.setAmount(amount);
        entry.setTitle(xmlEditTextTitle.getText().toString());
        entry.setComment(xmlEditTextComment.getText().toString());
    }

    private void clearEntry()
    {
        entry = new Entry(today, "", false, 0, categoryManager.getDefaultCategory(false), paymentMethodManager.getDefaultPaymentMethod(), "", Calendar.getInstance().getTime());
        recurringEntry = new RecurringEntry(entry, intervalManager.getDefaultInterval(), null);
        recurring = false;
        xmlEditTextAmount.setText("");
        xmlEditTextComment.setText("");
        xmlEditTextTitle.setText("");

    }

    private void deleteExistingEntry()
    {
        if (recurring)
        {
            recurringEntryManager.removeRecurringEntry(editRecurringEntryCopy);
        }
        else
        {
            entryManager.removeEntry(editEntryCopy);
        }
    }

    public static void setRecurring(boolean newRecurring)
    {
        recurring = newRecurring;
    }

    public static void setCameFromRecurringScreen(boolean newCameFromRecurringScreen)
    {
        cameFromRecurringScreen = newCameFromRecurringScreen;
    }

    public static void setEditMode(boolean newEditMode)
    {
        editMode = newEditMode;
    }

    public static void setEntry(Entry editEntry)
    {
        entry = new Entry(editEntry);
        editEntryCopy = editEntry;
    }

    public static Entry getEntry()
    {
        return entry;
    }

    public static void setRecurringEntry(RecurringEntry editRecurringEntry)
    {
        recurringEntry = new RecurringEntry(editRecurringEntry);
        editRecurringEntryCopy = editRecurringEntry;
        entry = recurringEntry.castToEntry(recurringEntry.getDate());
    }

    public static void setCameFromListOfEntriesScreen(boolean newCameFromListOfEntriesScreen)
    {
        cameFromListOfEntriesScreen = newCameFromListOfEntriesScreen;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                changeActivity(ActivityManageCategories.class);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public void onDetach()
    {
        if(isRemoving()){
            changeScreen(false,recurring);
        }
        super.onDetach();
    }

    @Override
    public void onResume()
    {
        setAdapters();
        super.onResume();
    }
}