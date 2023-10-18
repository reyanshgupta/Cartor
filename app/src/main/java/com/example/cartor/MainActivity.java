package com.example.cartor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    EditText loginemail, loginpass;
    Button loginbutton;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); //make navbar transparent
        setContentView(R.layout.activity_main);

        register = findViewById(R.id.register);
        loginemail = findViewById(R.id.loginemail);
        loginpass = findViewById(R.id.loginpass);
        loginbutton = findViewById(R.id.loginbutton);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateusername() | !validatepassword()){

                }else{
                    checkUser();
                }
            }
        });
    }

    public Boolean validateusername(){
        String user = loginemail.getText().toString();
        if(user.isEmpty()){
            loginemail.setError("Username cannot be empty!");
            return false;
        }else{
            loginemail.setError(null);
            return true;
        }
    }

    public Boolean validatepassword(){
        String pass = loginpass.getText().toString();
        if(pass.isEmpty()){
            loginpass.setError("Password cannot be empty!");
            return false;
        }else{
            loginpass.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String email = loginemail.getText().toString().trim();
        String password = loginpass.getText().toString().trim();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Sign in the user using the provided email and password
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign-in was successful, check if the email is verified
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                            if (currentUser != null && currentUser.isEmailVerified()) {
                                // Email is verified, allow login
                                Intent i = new Intent(MainActivity.this, LandingPage.class);
                                startActivity(i);
                            } else {
                                // Email is not verified, show a toast message
                                Toast.makeText(MainActivity.this, "Please verify your email first.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Sign-in failed, show an error message
                            Toast.makeText(MainActivity.this, "Authentication failed. Check your email and password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }





    public void registertv(View view){
        Intent i = new Intent(MainActivity.this, RegisterPage.class);
        startActivity(i);
    }
}