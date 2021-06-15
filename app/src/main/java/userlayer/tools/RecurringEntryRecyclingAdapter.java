package userlayer.tools;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.thecleverswabian.thecleverswabian.R;
import java.util.ArrayList;
import logiclayer.model.RecurringEntry;
import main.MainActivity;
import userlayer.fragments.FragmentAddNewEntry;


public class RecurringEntryRecyclingAdapter extends RecyclerView.Adapter<RecurringEntryRecyclingAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<RecurringEntry> listOfRecurringEntries;
    private static final int VIEW_TYPE_CONTENT = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    public RecurringEntryRecyclingAdapter(Context context, ArrayList<RecurringEntry> listOfRecurringEntries)
    {
        this.context = context;
        this.listOfRecurringEntries = listOfRecurringEntries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == VIEW_TYPE_CONTENT)
        {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_recurring_entry, parent, false);
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
            holder.itemView.setTag(listOfRecurringEntries.get(position));
            RecurringEntry recurringEntry = listOfRecurringEntries.get(position);
            String prefix = "- ";

            //If the entry is an income, center the category
            if (recurringEntry.getType())
            {
                prefix = "+ ";
                holder.paymentMethod.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0f));
                holder.category.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 100f));
                holder.amount.setTextColor(context.getResources().getColor(R.color.colorTextGreen));
            }

            holder.amount.setText(prefix + String.format("%.2f", recurringEntry.getAmount()) + " €");
            holder.startDateText.setText("Start: " + UserUtil.dateToString(recurringEntry.getDate()));
            holder.intervalText.setText("Interval: " + recurringEntry.getInterval().getName());


            if (recurringEntry.getEndDate() != null)
            {
                holder.endDateText.setText("End: " + UserUtil.dateToString(recurringEntry.getEndDate()));
            }
            else
            {
                holder.endDateText.setText("");
            }

            holder.titleText.setText(recurringEntry.getTitle());
            holder.category.setText(recurringEntry.getCategory().getName());
            holder.category.setCompoundDrawablesWithIntrinsicBounds(recurringEntry.getCategory().getIconAdress(), 0, 0, 0);

            if(!recurringEntry.getType())
            {
                holder.paymentMethod.setText(recurringEntry.getPaymentMethod().getName());
                holder.paymentMethod.setCompoundDrawablesWithIntrinsicBounds(recurringEntry.getPaymentMethod().getIconAdress(), 0, 0, 0);
            }
       }
    }

    @Override
    public int getItemViewType(int position)
    {
        return (position == listOfRecurringEntries.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount()
    {
        return listOfRecurringEntries.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView startDateText;
        TextView intervalText;
        TextView endDateText;
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

            startDateText = itemView.findViewById(R.id.UI_ElementRecurringEntry_Text_StartDate);
            intervalText = itemView.findViewById(R.id.UI_ElementRecurringEntry_Text_Interval);
            endDateText = itemView.findViewById(R.id.UI_ElementRecurringEntry_Text_EndDate);
            titleText = itemView.findViewById(R.id.UI_ElementRecurringEntry_Text_Title);
            category = itemView.findViewById(R.id.UI_ElementRecurringEntry_Text_Category);
            paymentMethod = itemView.findViewById(R.id.UI_ElementRecurringEntry_Text_PaymentMethod);
            amount = itemView.findViewById(R.id.UI_ElementRecurringEntry_Text_Amount);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    FragmentAddNewEntry.setRecurringEntry((RecurringEntry) view.getTag());
                    FragmentAddNewEntry.setEditMode(true);
                    FragmentAddNewEntry.setRecurring(true);
                    MainActivity.changeFragment(R.id.nav_addNewEntry);
                }
            });
        }
    }
}