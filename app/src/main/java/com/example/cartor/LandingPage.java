package com.example.cartor;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class LandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        Button py = findViewById(R.id.py);
        py.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText outputEditText = findViewById(R.id.et);
                String pythonOutput = runPythonScript();

                // Display the Python output in the EditText
                outputEditText.setText(pythonOutput);
            }
        });
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

    private String runPythonScript() {
        // Execute your Python script and capture its output
        String pythonOutput = ""; // This will store the Python script's output

        try {
            String pythonScript = "test.py";
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScript);
            processBuilder.directory(new File("test.py"));
            Process process = processBuilder.start();

            // Read the output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                pythonOutput += line + "\n"; // Append each line to the output
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();

            // Print the exit code (0 usually means success)
            System.out.println("Python script exited with code " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return pythonOutput;
    }
    protected void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
