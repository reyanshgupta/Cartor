package com.example.cartor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    EditText loginusername, loginpass;
    Button loginbutton;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); //make navbar transparent
        setContentView(R.layout.activity_main);

        register = findViewById(R.id.register);
        loginusername = findViewById(R.id.loginusername);
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
        String user = loginusername.getText().toString();
        if(user.isEmpty()){
            loginusername.setError("Username cannot be empty!");
            return false;
        }else{
            loginusername.setError(null);
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

    public void checkUser(){
        String username = loginusername.getText().toString().trim();
        String password = loginpass.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(username);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    loginusername.setError(null);
                    String passwordfromDB = snapshot.child(username).child("password").getValue(String.class);

                    if(passwordfromDB.equals(password)){
                        loginusername.setError(null);
                        Intent i = new Intent(MainActivity.this, HomePage.class);
                        startActivity(i);
                    }else{
                        loginpass.setError("Invalid Credentials!");
                        loginpass.requestFocus();
                    }
                }else{
                    loginusername.setError("User does not exist!");
                    loginusername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void registertv(View view){
        Intent i = new Intent(MainActivity.this, RegisterPage.class);
        startActivity(i);
    }
}