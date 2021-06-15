package logiclayer.controller;

import android.content.Context;

import com.thecleverswabian.thecleverswabian.R;

import java.util.HashMap;

import main.MainActivity;

public abstract class IconManager
{
    private static Context context = MainActivity.getContext();

    public static HashMap<String, Integer> getDrawableList()
    {
        HashMap<String, Integer> drawableList = new HashMap<String, Integer>();
        drawableList.put("icon_automobile", R.drawable.icon_automobile);
        drawableList.put("icon_childcare", R.drawable.icon_childcare);
        drawableList.put("icon_clothing", R.drawable.icon_clothing);
        drawableList.put("icon_education", R.drawable.icon_education);
        drawableList.put("icon_food", R.drawable.icon_food);
        drawableList.put("icon_gift", R.drawable.icon_gift);
        drawableList.put("icon_puzzle", R.drawable.icon_puzzle);
        drawableList.put("icon_clean", R.drawable.icon_clean);
        drawableList.put("icon_leisure", R.drawable.icon_leisure);
        drawableList.put("icon_bill", R.drawable.icon_bill);
        drawableList.put("icon_home", R.drawable.icon_home);
        drawableList.put("icon_bike", R.drawable.icon_bike);
        drawableList.put("icon_transportation", R.drawable.icon_transportation);
        drawableList.put("icon_palm", R.drawable.icon_palm);
        drawableList.put("icon_points", R.drawable.icon_points);
        drawableList.put("icon_gamble", R.drawable.icon_gamble);
        drawableList.put("icon_business", R.drawable.icon_business);
        drawableList.put("icon_give", R.drawable.icon_give);
        drawableList.put("icon_bank", R.drawable.icon_bank);
        drawableList.put("icon_cash", R.drawable.icon_cash);
        drawableList.put("icon_cheque", R.drawable.icon_cheque);
        drawableList.put("icon_creditcard", R.drawable.icon_creditcard);
        drawableList.put("icon_debitcard", R.drawable.icon_debitcard);
        drawableList.put("icon_paypal", R.drawable.icon_paypal);
        drawableList.put("icon_sort", R.drawable.icon_sort);
        drawableList.put("icon_settings", R.drawable.icon_settings);
        drawableList.put("icon_send", R.drawable.icon_send);
        drawableList.put("icon_screen", R.drawable.icon_screen);
        drawableList.put("icon_restaurant", R.drawable.icon_restaurant);
        drawableList.put("icon_recurring", R.drawable.icon_recurring);
        drawableList.put("icon_plus", R.drawable.icon_plus);
        drawableList.put("icon_plane", R.drawable.icon_plane);
        drawableList.put("icon_list", R.drawable.icon_list);
        drawableList.put("icon_information", R.drawable.icon_information);
        drawableList.put("icon_filter", R.drawable.icon_filter);
        drawableList.put("icon_exit", R.drawable.icon_exit);
        drawableList.put("icon_delete", R.drawable.icon_delete);
        drawableList.put("icon_android", R.drawable.icon_android);
        drawableList.put("icon_bed", R.drawable.icon_bed);
        drawableList.put("icon_cancel", R.drawable.icon_cancel);
        drawableList.put("icon_chart", R.drawable.icon_chart);
        drawableList.put("icon_check", R.drawable.icon_check);
        drawableList.put("icon_revert", R.drawable.icon_revert);
        return drawableList;
    }

    public static int getDrawable(String key)
    {
        Integer drawable =  getDrawableList().get(key);
        if (drawable == null)
        {
            drawable = R.drawable.icon_points;
        }
        return drawable;
    }



}
