package com.example.cartor;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {
    public static final double varEmission = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView totalScreenTimeTextView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String selectedImagePath;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        totalScreenTimeTextView = view.findViewById(R.id.totalScreenTime);
        Typeface customTypeface = ResourcesCompat.getFont(requireContext(), R.font.helvetica);

        if (hasUsageStatsPermission()) {
            Pair<Long, Long> screenTimePair = calculateScreenTime();
            displayScreenTime(screenTimePair);
        } else {
            requestUsageStatsPermission();
            fetchCartorCreditsFromFirebase(view);
        }
        Pair<Long, Long> screenTimePair = calculateScreenTime();
        long hours = screenTimePair.first;
        long minutes = screenTimePair.second;

        ProgressBar screenTimeProgressBar = view.findViewById(R.id.screenTimeProgressBar);

        screenTimeProgressBar.setProgress((int) hours);
        screenTimeProgressBar.setSecondaryProgress((int) (hours + minutes / 60.0 * 100));
        long screenTimeMillis = (hours * 60 + minutes) * 60 * 1000; // Convert hours and minutes to milliseconds
        double carbonEmissionsGrams = calculateCarbonEmissions(screenTimeMillis);
        TextView screenTimeCarbonEmissions = view.findViewById(R.id.ScreenTimeCarbonEmissions);
        screenTimeCarbonEmissions.setText(String.format("%.2f g", carbonEmissionsGrams));
        LineChart lineChart = view.findViewById(R.id.lineChart);

        List<String> daysOfWeek = new ArrayList<>();
        daysOfWeek.add("Mon");
        daysOfWeek.add("Tue");
        daysOfWeek.add("Wed");
        daysOfWeek.add("Thu");
        daysOfWeek.add("Fri");
        daysOfWeek.add("Sat");
        daysOfWeek.add("Sun");

        // Sample data for carbon emissions
        List<Entry> carbonEmissions = new ArrayList<>();
        carbonEmissions.add(new Entry(0, 100));  // Monday
        carbonEmissions.add(new Entry(1, 150));  // Tuesday
        carbonEmissions.add(new Entry(2, 200));  // Wednesday
        carbonEmissions.add(new Entry(3, 175));  // Thursday
        carbonEmissions.add(new Entry(4, 120));  // Friday
        carbonEmissions.add(new Entry(5, 250));  // Saturday
        carbonEmissions.add(new Entry(6, 90));   // Sunday

        LineDataSet dataSet = new LineDataSet(carbonEmissions, "Carbon Emissions");
        dataSet.setColor(Color.WHITE);
        dataSet.setDrawCircles(false);

        // Set the X-axis labels
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(daysOfWeek));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(customTypeface); // Set the custom font
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setAxisLineColor(Color.WHITE);

        // Set the Y-axis labels
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setTypeface(customTypeface); // Set the custom font
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisLeft.setAxisLineColor(Color.WHITE);

        lineChart.setData(new LineData(dataSet));
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.invalidate();

        TextView cartorCreditsTextView = view.findViewById(R.id.CartorCreditsDash);
        return view;
    }

    private void fetchCartorCreditsFromFirebase(View view) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        if (currentUser != null) {
            String userId = currentUser.getUid();

            usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Integer cartorCredits = dataSnapshot.child("credits").getValue(Integer.class);
                        Integer CarbonEmitted = dataSnapshot.child("carbonemitted").getValue(Integer.class);

                        if (cartorCredits != null) {
                            TextView cartorCreditsTextView = view.findViewById(R.id.CartorCreditsDash);
                            cartorCreditsTextView.setText("Cartor Credits: " + cartorCredits);
                        }

                        if(CarbonEmitted != null){
                            Pair<Long, Long> screenTimePair = calculateScreenTime();
                            long hours = screenTimePair.first;
                            long minutes = screenTimePair.second;
                            long screenTimeMillis = (hours * 60 + minutes) * 60 * 1000;
                            double emission = CarbonEmitted + calculateCarbonEmissions(screenTimeMillis);
                            usersRef.child(userId).child("carbonemitted").setValue(emission);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {

        }
    }

    private boolean hasUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) requireContext().getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), requireContext().getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }
    private void requestUsageStatsPermission() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }
    private Pair<Long, Long> calculateScreenTime() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) requireContext().getSystemService(Context.USAGE_STATS_SERVICE);
        long currentTime = System.currentTimeMillis();
        long startTime = currentTime - 24 * 60 * 60 * 1000; // 24 hours ago (1 day)
        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, currentTime);

        long totalScreenTimeMillis = 0;
        for (UsageStats stat : stats) {
            totalScreenTimeMillis += stat.getTotalTimeInForeground();
        }

        // Convert totalScreenTimeMillis to hours and minutes
        long hours = totalScreenTimeMillis / (60 * 60 * 1000); // Convert milliseconds to hours
        long minutes = (totalScreenTimeMillis / 60000) % 60; // Calculate the remaining minutes

        return new Pair<>(hours, minutes);
    }
    private void displayScreenTime(Pair<Long, Long> screenTime) {
        long hours = screenTime.first;
        long minutes = screenTime.second;

        String screenTimeText = hours + "h " + minutes + "m";
        totalScreenTimeTextView.setText(screenTimeText);
    }
    private double calculateCarbonEmissions(long screenTimeMillis) {
        // Convert screen time from milliseconds to minutes
        long minutes = screenTimeMillis / 60000;

        // Calculate carbon emissions (assuming 0.01 kg per minute)
        double carbonEmissions = 0.01 * minutes;

        return carbonEmissions;
    }

}