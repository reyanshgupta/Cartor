package com.example.cartor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity {

    EditText registerfull, registeruser, registeremail, registerpass;
    private static final Integer credits = 100;
    private static final Integer treeplanted = 0;
    private static final Integer points = 0;
    public static final Integer carbonemitted = 0;
    Button registerbutton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_register_page);

        registerfull = findViewById(R.id.registerfull);
        registeruser = findViewById(R.id.registeruser);
        registeremail = findViewById(R.id.registeremail);
        registerpass = findViewById(R.id.registerpass);
        registerbutton = findViewById(R.id.registerbutton);

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = registerfull.getText().toString();
                String user = registeruser.getText().toString();
                String email = registeremail.getText().toString();
                String pass = registerpass.getText().toString();

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                // Create the user in Firebase Authentication
                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(RegisterPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User created successfully in Firebase Authentication
                                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                    String uid = currentUser.getUid(); // Get the UID of the newly created user

                                    currentUser.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // Email verification link sent successfully
                                                        Toast.makeText(RegisterPage.this, "Verification email sent.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        // Error sending verification email
                                                        Exception exception = task.getException();
                                                        if (exception != null) {
                                                            Log.e("FirebaseAuth", "Error sending verification email: " + exception.getMessage());
                                                        }
                                                    }
                                                }
                                            });

                                    // Now, add user data to the Realtime Database using the UID as the parent node
                                    HelperClass helperClass = new HelperClass(name, user, email, pass, credits, treeplanted, points, carbonemitted);
                                    reference.child(uid).setValue(helperClass);

                                    // Redirect to the main activity
                                    Intent intent = new Intent(RegisterPage.this, MainActivity.class);
                                    startActivity(intent);

                                    // ... rest of your code
                                } else {
                                    // Handle user creation failure
                                    Exception exception = task.getException();
                                    if (exception != null) {
                                        Log.e("FirebaseAuth", "User creation failed: " + exception.getMessage());
                                    }
                                    Toast.makeText(RegisterPage.this, "User creation failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });


    }

}