package com.example.cartor;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chaquo.python.Python;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class LandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);


        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.navigation_dashboard) {
                replaceFragment(new DashboardFragment());
                return true;
            } else if (itemId == R.id.navigation_community) {
                replaceFragment(new CommunityFragment());
                return true;
            } else if (itemId == R.id.navigation_marketplace) {
                replaceFragment(new MarketplaceFragment());
                return true;
            } else if (itemId == R.id.navigation_profile) {
                replaceFragment(new ProfileFragment());
                return true;
            } else {
                return false;
            }
        });
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            // Handle the result of image selection here.
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                // Find your fragment and its ImageView
                DashboardFragment fragment = (DashboardFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                ImageView imageView = fragment.getView().findViewById(R.id.imageSelect);

                // Display the selected image in the ImageView
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    protected void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
