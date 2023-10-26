package com.example.cartor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment {

    private TextView username, name, carboncredits, treesplanted, carbonemitted;
    private RecyclerView leaderboardlist;

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
        carboncredits = view.findViewById(R.id.carboncreditsComm);
        treesplanted = view.findViewById(R.id.treesplanted);
        carbonemitted = view.findViewById(R.id.carbonemitted);
        leaderboardlist = view.findViewById(R.id.leaderboardlist);



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

                        Integer CarbonEmitted = snapshot.child("carbonemitted").getValue(Integer.class);
                        carbonemitted.setText(CarbonEmitted + " Kg");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        Query leaderboardQuery = usersRef.orderByChild("treeplanted").limitToLast(10);

        leaderboardQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<HelperClass> leaderboard = new ArrayList<>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    HelperClass user = userSnapshot.getValue(HelperClass.class);
                    leaderboard.add(user);
                }

                LeaderboardAdapter adapter = new LeaderboardAdapter(leaderboard);
                leaderboardlist.setLayoutManager(new LinearLayoutManager(getContext()));
                leaderboardlist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                
                Log.e("Leaderboard", "Error reading data from the database: " + databaseError.getMessage());
            }
        });


        return view;
    }
}