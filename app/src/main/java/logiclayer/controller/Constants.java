package logiclayer.controller;

public interface Constants
{
    public static final int DF_DAYMONTHYEAR_POINTS = 0;
    public static final int DF_DAYMONTHYEAR_SLASH = 1;
    public static final int DF_DAYMONTHYEAR_DASH = 2;
    public static final int DF_MONTHDAYYEAR_POINTS = 3;
    public static final int DF_MONTHDAYYEAR_SLASH = 4;
    public static final int DF_MONTHDAYYEAR_DASH = 5;
    public static final int DF_YEARMONTHDAY_POINTS = 6;
    public static final int DF_YEARMONTHDAY_SLASH = 7;
    public static final int DF_YEARMONTHDAY_DASH = 8;

    public int PASSWORD_SETUP_USERNAME_AND_PIN = 0;
    public int PASSWORD_SETUP_USERNAME_AND_PASSWORD = 1;
    public int PASSWORD_SETUP_JUST_PIN = 2;
    public int PASSWORD_SETUP_JUST_PASSWORD = 3;
    public int PASSWORD_SETUP_DISABLED = 4;

    public int SETUP_PROGRESS_NONE = 0;
    public int SETUP_PROGRESS_REGISTRATION = 1;
    public int SETUP_PROGRESS_SECURITY_QUESTIONS = 2;
    public int SETUP_PROGRESS_DONE = 99;
}
