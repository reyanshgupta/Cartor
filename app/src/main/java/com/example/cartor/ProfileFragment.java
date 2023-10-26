package com.example.cartor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private TextView level, levelnum, remainingpoints, mailemission, socialemission, callemission;

    private ProgressBar progressBar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference usersRef;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView profileUsername = view.findViewById(R.id.profileUsername);
        TextView profileName = view.findViewById(R.id.profileName);
        level = view.findViewById(R.id.level);
        levelnum = view.findViewById(R.id.levelnum);
        remainingpoints = view.findViewById(R.id.remainingpoints);
        progressBar = view.findViewById(R.id.progressBar);
        mailemission = view.findViewById(R.id.mailemission);
        socialemission = view.findViewById(R.id.socialemission);
        callemission = view.findViewById(R.id.callemission);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userNode = usersRef.child(uid);
            userNode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("username").getValue(String.class);
                        String name = dataSnapshot.child("name").getValue(String.class);
                        Integer MailEmission = dataSnapshot.child("mailemission").getValue(Integer.class);
                        Integer SocialEmission = dataSnapshot.child("socialemission").getValue(Integer.class);
                        Integer CallEmission = dataSnapshot.child("callemission").getValue(Integer.class);


                        profileUsername.setText(username);
                        profileName.setText(name);
                        mailemission.setText(MailEmission + " g CO2e");
                        socialemission.setText(SocialEmission + " g CO2e");
                        callemission.setText(CallEmission + " g CO2e");

                        Integer points = dataSnapshot.child("points").getValue(Integer.class);
                        setProgress(points);
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
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors here
                }
            });
        }

        return view;
    }
}
