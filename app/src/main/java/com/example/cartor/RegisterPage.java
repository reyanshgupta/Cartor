package com.example.cartor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity {

    EditText registerfull, registeruser, registeremail, registerpass;
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

                HelperClass helperClass = new HelperClass(name, user, email, pass);
                reference.child(user).setValue(helperClass);

                Toast.makeText(RegisterPage.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterPage.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

}