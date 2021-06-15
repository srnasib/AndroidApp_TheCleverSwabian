package userlayer.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.thecleverswabian.thecleverswabian.R;

public class ListAdapterWithPreselectionAndIcons extends ArrayAdapter
{
    private Integer markPosition;
    private int[] icons;

    public ListAdapterWithPreselectionAndIcons(Context context, String[] names, int markPosition)
    {
        super(context, android.R.layout.simple_spinner_item, names);
        this.markPosition = markPosition;
    }

    public ListAdapterWithPreselectionAndIcons(Context context, String[] names, int[] icons, int markPosition)
    {
        super(context, android.R.layout.simple_spinner_item, names);
        this.markPosition = markPosition;
        this.icons = icons;
    }

    public ListAdapterWithPreselectionAndIcons(Context context, String[] names, int[] icons)
    {
        super(context, android.R.layout.simple_spinner_item, names);
        this.icons = icons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Context context = getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(android.R.layout.simple_list_item_1, null, false);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);

        textView.setText((String) getItem(position));
        textView.setBackground(context.getResources().getDrawable(R.drawable.button_grey));
        if(markPosition!=null)
        {
            if (position == markPosition)
            {
                textView.setBackground(context.getResources().getDrawable(R.drawable.button_grey_dark));            }
        }
        if(icons!=null)
        {
            textView.setCompoundDrawablePadding(45);
            textView.setCompoundDrawablesWithIntrinsicBounds(icons[position], 0, 0, 0);
        }

        return textView;

    }

}