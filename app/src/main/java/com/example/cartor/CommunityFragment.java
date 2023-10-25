package com.example.cartor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment {

    private TextView username, name, carboncredits, treesplanted, level, levelnum, remainingpoints, carbonemitted;
    private ProgressBar progressBar;

    private DatabaseReference usersRef;

    private int progressStatus = 0;
    private Handler handler = new Handler();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommunityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommunityFragment newInstance(String param1, String param2) {
        CommunityFragment fragment = new CommunityFragment();
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
        View view =  inflater.inflate(R.layout.fragment_community, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");

        username = view.findViewById(R.id.username);
        name = view.findViewById(R.id.name);
        carboncredits = view.findViewById(R.id.carboncredits);
        treesplanted = view.findViewById(R.id.treesplanted);
        level = view.findViewById(R.id.level);
        levelnum = view.findViewById(R.id.levelnum);
        remainingpoints = view.findViewById(R.id.remainingpoints);
        progressBar = view.findViewById(R.id.progressBar);
        carbonemitted = view.findViewById(R.id.carbonemitted);


        if(currentUser != null){
            String uid = currentUser.getUid();

            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String Username = snapshot.child("username").getValue(String.class);
                        username.setText(Username);

                        String Name = snapshot.child("name").getValue(String.class);
                        name.setText(Name);

                        Integer credits = snapshot.child("credits").getValue(Integer.class);
                        carboncredits.setText(String.valueOf(credits));

                        Integer treeplanted = snapshot.child("treeplanted").getValue(Integer.class);
                        treesplanted.setText(String.valueOf(treeplanted));

                        Integer points = snapshot.child("points").getValue(Integer.class);
                        setProgress(points);

                        Integer CarbonEmitted = snapshot.child("carbonemitted").getValue(Integer.class);
                        carbonemitted.setText(CarbonEmitted + " Kg");

                    }
                }

                private void setProgress(Integer points) {
                    int userlevel = (points/100)+1;
                    int progress = points % 100;

                    if (userlevel >= 1 && userlevel <= 10) {
                        progressBar.setMax(100);
                        progressBar.setProgress(progress);
                    } else {
                        progressBar.setMax(100);
                        progressBar.setProgress(100);
                    }

                    level.setText("Level: " + userlevel);
                    levelnum.setText(progress + "/100");
                    remainingpoints.setText("Achieve " + (100-progress)+" more points for next level");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return view;
    }
}