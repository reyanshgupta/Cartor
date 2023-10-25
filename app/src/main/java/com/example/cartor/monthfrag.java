package com.example.cartor;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link monthfrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class monthfrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public monthfrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment monthfrag.
     */
    // TODO: Rename and change types and number of parameters
    public static monthfrag newInstance(String param1, String param2) {
        monthfrag fragment = new monthfrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dayfrag, container, false);
        Typeface customTypeface = ResourcesCompat.getFont(requireContext(), R.font.helvetica);
        LineChart lineChart = view.findViewById(R.id.dayLineChart);

        // Use the months of the year as X-axis labels
        List<String> monthsOfYear = new ArrayList<>();
        monthsOfYear.add("Jan");
        monthsOfYear.add("Feb");
        monthsOfYear.add("Mar");
        monthsOfYear.add("Apr");
        monthsOfYear.add("May");
        monthsOfYear.add("Jun");
        monthsOfYear.add("Jul");
        monthsOfYear.add("Aug");
        monthsOfYear.add("Sep");
        monthsOfYear.add("Oct");
        monthsOfYear.add("Nov");
        monthsOfYear.add("Dec");

        // Use the carbon emissions data for each month
        List<Entry> carbonEmissions = new ArrayList<>();
        carbonEmissions.add(new Entry(0, 1000));   // January
        carbonEmissions.add(new Entry(1, 1200));   // February
        carbonEmissions.add(new Entry(2, 900));    // March
        carbonEmissions.add(new Entry(3, 1100));   // April
        carbonEmissions.add(new Entry(4, 800));    // May
        carbonEmissions.add(new Entry(5, 1500));   // June
        carbonEmissions.add(new Entry(6, 1400));   // July
        carbonEmissions.add(new Entry(7, 1600));   // August
        carbonEmissions.add(new Entry(8, 1100));   // September
        carbonEmissions.add(new Entry(9, 900));    // October
        carbonEmissions.add(new Entry(10, 800));   // November
        carbonEmissions.add(new Entry(11, 950));   // December
        // Create a LineDataSet with the carbon emissions data
        LineDataSet dataSet = new LineDataSet(carbonEmissions, "Carbon Emissions");
        dataSet.setColor(Color.WHITE);
        dataSet.setDrawCircles(false);

        // Set the X-axis labels to months of the year
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthsOfYear));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(customTypeface); // Set the custom font
        xAxis.setTextColor(Color.WHITE);
        xAxis.setAxisLineColor(Color.WHITE);

        // Set the Y-axis labels
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setTypeface(customTypeface); // Set the custom font
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisLeft.setAxisLineColor(Color.WHITE);

        // Customize the appearance of the chart
        lineChart.setData(new LineData(dataSet));
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.invalidate();

        return view;
    }
}