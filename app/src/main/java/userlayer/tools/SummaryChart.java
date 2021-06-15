package userlayer.tools;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.thecleverswabian.thecleverswabian.R;

import java.util.ArrayList;

public class SummaryChart extends AppCompatActivity {
    PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_chart);

        pieChart=(PieChart) findViewById (R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.25f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(35f);

        pieChart.animateY(1000, Easing.EaseInOutCubic);

        ArrayList<PieEntry>yValues=new ArrayList<>();
        yValues.add(new PieEntry(34f,"Food"));
        yValues.add(new PieEntry(23f,"Travel"));
        yValues.add(new PieEntry(14f,"Leisure"));
        yValues.add(new PieEntry(35f,"Shopping"));
        yValues.add(new PieEntry(40f,"Rent"));
        yValues.add(new PieEntry(23f,"Gym"));

        Description description=new Description();
        description.setText("The summary of the Expenses Last month");
        description.setTextSize(10);
        pieChart.setDescription(description);

        PieDataSet dataSet=new PieDataSet(yValues,"Monthly Expenses");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(6f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data=new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);





    }
}
