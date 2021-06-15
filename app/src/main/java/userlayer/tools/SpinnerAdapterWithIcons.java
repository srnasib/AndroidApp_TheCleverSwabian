package userlayer.tools;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;


public class SpinnerAdapterWithIcons extends ArrayAdapter
{
    private String[] names;
    private int[] icons;

    public SpinnerAdapterWithIcons(Context context, String[] names, int[] icons)
    {
        super(context, android.R.layout.simple_spinner_item, names);
        this.names = names;
        this.icons = icons;
    }

    public SpinnerAdapterWithIcons(Context context, String[] names)
    {
        super(context, android.R.layout.simple_spinner_item, names);
        this.names = names;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getItemView(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return getItemView(position);
    }

    private View getItemView(int position)
    {
        CheckedTextView listItem = new CheckedTextView(getContext());

        //Extreme width used to expand the sensitive area to the full spinner width
        listItem.setLayoutParams(new AbsListView.LayoutParams(10000, ViewGroup.LayoutParams.WRAP_CONTENT));
        listItem.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        listItem.setText(names[position]);
        listItem.setTextSize(20);
        listItem.setGravity(Gravity.CENTER_VERTICAL);
        listItem.setPadding(10,25,30,25);

        if(icons!=null)
        {
            listItem.setCompoundDrawablePadding(45);
            listItem.setCompoundDrawablesWithIntrinsicBounds(icons[position], 0, 0, 0);
        }

        return listItem;
    }
}