package logiclayer.controller;

import datalayer.DataBaseAccess;


public class SettingsManager implements DataBaseAccess, Constants
{
    private static String username = "";
    private static String password = "";
    private static int passwordSetup;
    private static String[] securityQuestions = {"", ""};
    private static String[] securityAnswers = {"", ""};
    private static String currency = "€";
    private static int dateFormat = 0;
    private static int language = 0;
    private static int setupProgress = 0;

    //allows only one object at once
    private static volatile SettingsManager instance;


    public static final String[] DATEFORMATS = {"Day.Month.Year", "Day/Month/Year", "Day-Month-Year", "Month.Day.Year", "Month/Day/Year", "Month-Day-Year", "Year.Month.Day", "Year/Month/Day", "Year-Month-Day"};


    private SettingsManager()
    {
        loadSettings();
    }

    static SettingsManager getInstance()
    {
        if (instance == null)
        {
            synchronized (SettingsManager.class)
            {
                if (instance == null)
                {
                    instance = new SettingsManager();
                }
            }
        }
        return instance;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
        databaseHelper.clearAndAddObject(username, databaseHelper.TABLE_USERNAME);
    }


    public boolean setPassword(String password)
    {
        this.password = password;
        return databaseHelper.clearAndAddObject(password, databaseHelper.TABLE_PASSWORD);
    }

    public boolean checkPassword(String password)
    {
        if (this.password.equals(password))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public int getPasswordSetup()
    {
        return passwordSetup;
    }

    public void setPasswordSetup(int passwordSetup)
    {
        this.passwordSetup = passwordSetup;
        databaseHelper.clearAndAddObject(passwordSetup, databaseHelper.TABLE_PASSWORD_SETUP);
    }

    public void setPasswordSetup(boolean nameForLogin, boolean fullTextPassword)
    {
        if (nameForLogin)
        {
            if (fullTextPassword)
            {
                setPasswordSetup(PASSWORD_SETUP_USERNAME_AND_PASSWORD);
            }
            else
            {
                setPasswordSetup(PASSWORD_SETUP_USERNAME_AND_PIN);
            }
        }
        else
        {
            if (fullTextPassword)
            {
                setPasswordSetup(PASSWORD_SETUP_JUST_PASSWORD);
            }
            else
            {
                setPasswordSetup(PASSWORD_SETUP_JUST_PIN);
            }
        }
    }


    public void setSecurityQAs(String q1, String q2, String a1, String a2)
    {
        securityQuestions = new String[2];
        securityAnswers = new String[2];

        securityQuestions[0] = q1;
        securityQuestions[1] = q2;
        securityAnswers[0] = a1;
        securityAnswers[1] = a2;
        databaseHelper.clearAndAddObject(securityQuestions, databaseHelper.TABLE_SECURITY_QUESTIONS);
        databaseHelper.clearAndAddObject(securityAnswers, databaseHelper.TABLE_SECURITY_ANSWERS);
    }

    public String[] getSecurityQuestions()
    {
        return securityQuestions;
    }

    public boolean getSecurityQASetup()
    {
        if (securityQuestions == null)
        {
            return false;
        }
        else
        {
            if(securityQuestions[0].equals(""))
            {
                return false;
            }
            else
            {
                return true;
            }

        }
    }

    public boolean checkSecurityQA1(String answer)
    {
        return securityAnswers[0].equals(answer);
    }

    public boolean checkSecurityQA2(String answer)
    {
        return securityAnswers[1].equals(answer);
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
        databaseHelper.clearAndAddObject(currency, databaseHelper.TABLE_CURRENCY);
    }

    public int getDateFormat()
    {
        return dateFormat;
    }

    public void setDateFormat(int dateFormat)
    {
        this.dateFormat = dateFormat;
        databaseHelper.clearAndAddObject(dateFormat, databaseHelper.TABLE_DATEFORMAT);
    }

    public int getLanguage()
    {
        return language;
    }

    public void setLanguage(int setupProgress)
    {
        this.language = language;
        databaseHelper.clearAndAddObject(language, databaseHelper.TABLE_LANGUAGE);
    }

    public int getSetupProgress()
    {
        return setupProgress;
    }

    public void setSetupProgress(int setupProgress)
    {
        this.setupProgress = setupProgress;
        databaseHelper.clearAndAddObject(setupProgress, databaseHelper.TABLE_SETUP_PROGRESS);
    }


    //returns operation success
    private boolean saveAndOverwriteSettings()
    {
        boolean successful = true;
        successful = successful && databaseHelper.clearAndAddObject(username, databaseHelper.TABLE_USERNAME);
        successful = successful && databaseHelper.clearAndAddObject(password, databaseHelper.TABLE_PASSWORD);
        successful = successful && databaseHelper.clearAndAddObject(passwordSetup, databaseHelper.TABLE_PASSWORD_SETUP);
        successful = successful && databaseHelper.clearAndAddObject(securityQuestions, databaseHelper.TABLE_SECURITY_QUESTIONS);
        successful = successful && databaseHelper.clearAndAddObject(securityAnswers, databaseHelper.TABLE_SECURITY_ANSWERS);
        successful = successful && databaseHelper.clearAndAddObject(currency, databaseHelper.TABLE_CURRENCY);
        successful = successful && databaseHelper.clearAndAddObject(dateFormat, databaseHelper.TABLE_DATEFORMAT);
        successful = successful && databaseHelper.clearAndAddObject(language, databaseHelper.TABLE_LANGUAGE);
        successful = successful && databaseHelper.clearAndAddObject(setupProgress, databaseHelper.TABLE_SETUP_PROGRESS);
        return successful;
    }

    private void loadSettings()
    {
        username = (String) databaseHelper.getFirstObject(databaseHelper.TABLE_USERNAME, String.class);
        password = (String) databaseHelper.getFirstObject(databaseHelper.TABLE_PASSWORD, String.class);
        passwordSetup = (int) databaseHelper.getFirstObject(databaseHelper.TABLE_PASSWORD_SETUP, int.class);
        securityQuestions = (String[]) databaseHelper.getFirstObject(databaseHelper.TABLE_SECURITY_QUESTIONS, String[].class);
        securityAnswers = (String[]) databaseHelper.getFirstObject(databaseHelper.TABLE_SECURITY_ANSWERS, String[].class);
        currency = (String) databaseHelper.getFirstObject(databaseHelper.TABLE_CURRENCY, String.class);
        dateFormat = (int) databaseHelper.getFirstObject(databaseHelper.TABLE_DATEFORMAT, int.class);
        language = (int) databaseHelper.getFirstObject(databaseHelper.TABLE_LANGUAGE, int.class);
        setupProgress = (int) databaseHelper.getFirstObject(databaseHelper.TABLE_SETUP_PROGRESS, int.class);
    }




    public static String formatDate(String dayofWeek, String date, String month, String year)
    {
        switch (dateFormat)
        {
            case DF_DAYMONTHYEAR_POINTS:
                return (dayofWeek + ", " + date + "." + month + "." + year);
            case DF_MONTHDAYYEAR_POINTS:
                return (dayofWeek + ", " + month + "." + date + "." + year);
            case DF_YEARMONTHDAY_POINTS:
                return (dayofWeek + ", " + year + "." + month + "." + date);
            case DF_DAYMONTHYEAR_SLASH:
                return (dayofWeek + ", " + date + "/" + month + "/" + year);
            case DF_MONTHDAYYEAR_SLASH:
                return (dayofWeek + ", " + month + "/" + date + "/" + year);
            case DF_YEARMONTHDAY_SLASH:
                return (dayofWeek + ", " + year + "/" + month + "/" + date);
            case DF_DAYMONTHYEAR_DASH:
                return (dayofWeek + ", " + date + "-" + month + "-" + year);
            case DF_MONTHDAYYEAR_DASH:
                return (dayofWeek + ", " + month + "-" + date + "-" + year);
            case DF_YEARMONTHDAY_DASH:
                return (dayofWeek + ", " + year + "-" + month + "-" + date);

            default:
                return (dayofWeek + ", " + date + "." + month + "." + year);
        }
    }


}
