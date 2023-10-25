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
 * Use the {@link dayfrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dayfrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public dayfrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dayfrag.
     */
    // TODO: Rename and change types and number of parameters
    public static dayfrag newInstance(String param1, String param2) {
        dayfrag fragment = new dayfrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dayfrag, container, false);
        Typeface customTypeface = ResourcesCompat.getFont(requireContext(), R.font.helvetica);
        LineChart lineChart = view.findViewById(R.id.dayLineChart);


        List<String> timeOfDay = new ArrayList<>();
        timeOfDay.add("12 am");
        timeOfDay.add("1 am");
        timeOfDay.add("2 am");
        timeOfDay.add("3 am");
        timeOfDay.add("4 am");
        timeOfDay.add("5 am");
        timeOfDay.add("6 am");
        timeOfDay.add("7 am");
        timeOfDay.add("8 am");
        timeOfDay.add("9 am");
        timeOfDay.add("10 am");
        timeOfDay.add("11 am");
        timeOfDay.add("12 pm");
        timeOfDay.add("1 pm");
        timeOfDay.add("2 pm");
        timeOfDay.add("3 pm");
        timeOfDay.add("4 pm");
        timeOfDay.add("5 pm");
        timeOfDay.add("6 pm");
        timeOfDay.add("7 pm");
        timeOfDay.add("8 pm");
        timeOfDay.add("9 pm");
        timeOfDay.add("10 pm");
        timeOfDay.add("11 pm");

        // Sample data for carbon emissions
        List<Entry> carbonEmissions = new ArrayList<>();
        carbonEmissions.add(new Entry(0, 100));
        carbonEmissions.add(new Entry(1, 150));
        carbonEmissions.add(new Entry(2, 200));
        carbonEmissions.add(new Entry(3, 175));
        carbonEmissions.add(new Entry(4, 120));
        carbonEmissions.add(new Entry(5, 250));
        carbonEmissions.add(new Entry(6, 90));
        carbonEmissions.add(new Entry(7, 90));
        carbonEmissions.add(new Entry(8, 134));
        carbonEmissions.add(new Entry(9, 152));
        carbonEmissions.add(new Entry(10, 531));
        carbonEmissions.add(new Entry(11, 90));
        carbonEmissions.add(new Entry(12, 531));
        carbonEmissions.add(new Entry(13, 314));
        carbonEmissions.add(new Entry(14, 90));
        carbonEmissions.add(new Entry(15, 133));
        carbonEmissions.add(new Entry(16, 90));
        carbonEmissions.add(new Entry(17, 90));
        carbonEmissions.add(new Entry(18, 341));
        carbonEmissions.add(new Entry(19, 90));
        carbonEmissions.add(new Entry(20, 313));
        carbonEmissions.add(new Entry(21, 90));
        carbonEmissions.add(new Entry(22, 111));
        carbonEmissions.add(new Entry(23, 90));

        LineDataSet dataSet = new LineDataSet(carbonEmissions, "Carbon Emissions");
        dataSet.setColor(Color.WHITE);
        dataSet.setDrawCircles(false);

        // Set the X-axis labels to time of day
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(timeOfDay));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(customTypeface);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setAxisLineColor(Color.WHITE);

        // Set the Y-axis labels
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setTypeface(customTypeface);
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisLeft.setAxisLineColor(Color.WHITE);

        lineChart.setData(new LineData(dataSet));
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.invalidate();

        return view;
    }
}