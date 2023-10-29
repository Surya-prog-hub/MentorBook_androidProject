package com.example.admin_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_activity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://adminappproject-85c7c-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       final EditText em = findViewById(R.id.eml);
       final EditText ps = findViewById(R.id.pass);
       final Button b = findViewById(R.id.btn);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final String email = em.getText().toString();
              final   String pass = ps.getText().toString();

                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(Login_activity.this, "Please enter your email and password", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(email)) {
                               final String getPassword = snapshot.child(email).child("pass").getValue(String.class);
                                if (getPassword.equals(pass)) {
                                    Toast.makeText(Login_activity.this, "Login success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login_activity.this,MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(Login_activity.this, "Invalid Details", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login_activity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle database error if needed.
                        }
                    });
                }
            }
        });
    }
}
