package com.example.cartor;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dayfrag, container, false);

        BarChart histogramChart = rootView.findViewById(R.id.histogramChart);
        int paddingInDp = 10; // 10dp
        float scale = getResources().getDisplayMetrics().density;
        int paddingInPixels = (int) (paddingInDp * scale + 0.5f);
        histogramChart.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 4.5f));
        entries.add(new BarEntry(1f, 3.2f));
        entries.add(new BarEntry(2f, 5.0f));
        // Add more entries as needed

        BarDataSet dataSet = new BarDataSet(entries, "Histogram");
        dataSet.setColor(Color.BLUE); // Customize the color of the bars

        BarData data = new BarData(dataSet);
        histogramChart.setData(data);

        // Customize the chart further (e.g., labels, axes, legend, etc.)
        Description description = new Description();
        description.setText(""); // You can add a description if needed
        histogramChart.setDescription(description);

        histogramChart.setFitBars(true);
        histogramChart.setDrawValueAboveBar(true);
        histogramChart.invalidate(); // Refresh the chart

        return rootView;
    }
}