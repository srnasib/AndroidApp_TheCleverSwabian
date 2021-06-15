package userlayer.tools;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thecleverswabian.thecleverswabian.R;

import java.util.ArrayList;

import logiclayer.model.Entry;
import main.MainActivity;
import userlayer.fragments.FragmentAddNewEntry;


public class EntryRecyclingAdapter extends RecyclerView.Adapter<EntryRecyclingAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<Entry> listOfEntries;
    private static final int VIEW_TYPE_CONTENT = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    public EntryRecyclingAdapter(Context context, ArrayList<Entry> listOfEntries)
    {
        this.context = context;
        this.listOfEntries = listOfEntries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == VIEW_TYPE_CONTENT)
        {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_entry, parent, false);
            ViewHolderContent viewHolder = new ViewHolderContent(view);
            return viewHolder;
        }
        else
        {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_end_of_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        if (holder instanceof ViewHolderContent)
        {
            holder.itemView.setTag(listOfEntries.get(position));
            Entry entry = listOfEntries.get(position);
            String prefix = "- ";

            //If the entry is an income, center the category
            if (entry.getType())
            {
                prefix = "+ ";
                holder.paymentMethod.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0f));
                holder.category.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 100f));
                holder.amount.setTextColor(context.getResources().getColor(R.color.colorTextGreen));
            }

            //If there's no title, center the date
            if (entry.getTitle().matches(""))
            {
                holder.titleText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0f));
                holder.dateText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 100f));
                holder.dateText.setGravity(Gravity.LEFT | Gravity.CENTER);
            }

            holder.amount.setText(prefix + String.format("%.2f", entry.getAmount()) + " €");
            holder.dateText.setText(UserUtil.dateToString(entry.getDate()));
            holder.titleText.setText(entry.getTitle());
            holder.category.setText(entry.getCategory().getName());
            holder.category.setCompoundDrawablesWithIntrinsicBounds(entry.getCategory().getIconAdress(), 0, 0, 0);

            if(!entry.getType())
            {
                holder.paymentMethod.setText(entry.getPaymentMethod().getName());
                holder.paymentMethod.setCompoundDrawablesWithIntrinsicBounds(entry.getPaymentMethod().getIconAdress(), 0, 0, 0);
            }
       }
    }

    @Override
    public int getItemViewType(int position)
    {
        return (position == listOfEntries.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount()
    {
        return listOfEntries.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView dateText;
        TextView titleText;
        TextView category;
        TextView paymentMethod;
        TextView amount;

        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }

    public class ViewHolderContent extends ViewHolder
    {

        public ViewHolderContent(View itemView)
        {
            super(itemView);

            dateText = itemView.findViewById(R.id.UI_ElementEntry_Text_Date);
            titleText = itemView.findViewById(R.id.UI_ElementEntry_Text_Title);
            category = itemView.findViewById(R.id.UI_ElementEntry_Text_Category);
            paymentMethod = itemView.findViewById(R.id.UI_ElementEntry_Text_PaymentMethod);
            amount = itemView.findViewById(R.id.UI_ElementEntry_Text_Amount);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    FragmentAddNewEntry.setEntry((Entry) view.getTag());
                    FragmentAddNewEntry.setEditMode(true);
                    MainActivity.changeFragment(R.id.nav_addNewEntry);
                }
            });
        }
    }
}