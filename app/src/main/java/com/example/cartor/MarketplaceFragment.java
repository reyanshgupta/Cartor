package com.example.cartor;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MarketplaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarketplaceFragment extends Fragment {

    private DatabaseReference usersRef;
    private TextView carboncredits;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String[] couponNames = {"Plane Tickets", "Travel", "Food Coupons", "Movie Tickets"};
    int[] Images = {R.drawable.ic_plane, R.drawable.ic_car, R.drawable.ic_food, R.drawable.ic_movie};
    String[] cartorCreditsCost = {"200", "300", "500", "100"};
    private SimpleAdapter sa;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MarketplaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MarketplaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MarketplaceFragment newInstance(String param1, String param2) {
        MarketplaceFragment fragment = new MarketplaceFragment();
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
        View view = inflater.inflate(R.layout.fragment_marketplace, container, false);

        ListView listView = view.findViewById(R.id.listview);  // Check for null here
        if (listView != null) {
            List<HashMap<String, String>> itemList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                HashMap<String, String> itemMap = new HashMap<>();
                itemMap.put("Name", couponNames[i]);
                itemMap.put("Images", String.valueOf(Images[i]));
                itemList.add(itemMap);
            }

            String[] from = {"Name", "Images"};
            int[] to = {R.id.tv, R.id.iv};
            SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), itemList, R.layout.coupon_layout, from, to);
            listView.setAdapter(simpleAdapter);
        }

        carboncredits = view.findViewById(R.id.carboncredits);

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
                        Integer credits = dataSnapshot.child("credits").getValue(Integer.class);

                        carboncredits.setText(String.valueOf(credits));
                    }
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