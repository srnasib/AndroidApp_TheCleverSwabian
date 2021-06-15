package userlayer.tools;


import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalInputTextWatcher implements InputFilter
{
    private int digitsBeforeDecOp;
    private int digitsAfterDecOp;
    private Pattern mPattern;

    private static final int DIGITS_BEFORE_DECOP_DEFAULT = 100;
    private static final int DIGITS_AFTER_DECOP_DEFAULT = 100;

    public DecimalInputTextWatcher(Integer digitsBeforeDecOp, Integer digitsAfterDecOp)
    {
        this.digitsBeforeDecOp = (digitsBeforeDecOp != null ? digitsBeforeDecOp : DIGITS_BEFORE_DECOP_DEFAULT);
        this.digitsAfterDecOp = (digitsAfterDecOp != null ? digitsAfterDecOp : DIGITS_AFTER_DECOP_DEFAULT);

        if (UserUtil.getDecimalSeparator().equals(","))
        {
            mPattern = Pattern.compile("-?[0-9]{0," + (digitsBeforeDecOp) + "}+((,[0-9]{0," + (digitsAfterDecOp) + "})?)||(,)?");
        }
        else
        {
            mPattern = Pattern.compile("-?[0-9]{0," + (digitsBeforeDecOp) + "}+((\\.[0-9]{0," + (digitsAfterDecOp) + "})?)||(\\.)?");
        }
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
    {
        String replacement = source.subSequence(start, end).toString();
        String newVal = dest.subSequence(0, dstart).toString() + replacement + dest.subSequence(dend, dest.length()).toString();
        Matcher matcher = mPattern.matcher(newVal);
        if (matcher.matches())
        {
            return null;
        }

        if (TextUtils.isEmpty(source))
        {
            return dest.subSequence(dstart, dend);
        }
        else
        {
            return "";
        }
    }
}