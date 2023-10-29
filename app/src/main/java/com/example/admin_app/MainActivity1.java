package com.example.admin_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity1 extends AppCompatActivity {
          EditText et1,et2;
           Button b,rb;

           DatabaseReference studentDbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        et1=findViewById(R.id.email);
        et2=findViewById(R.id.password);
        b=findViewById(R.id.loginbtn);
        rb=findViewById(R.id.lbtn);
        studentDbRef= FirebaseDatabase.getInstance().getReference().child("Students");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertstudentdata();
            }
        });
rb.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent =new Intent(MainActivity1.this, MainActivity.class);
        startActivity(intent);
    }
});

    }


    private void insertstudentdata() {
        String email=et1.getText().toString();
        String pass=et2.getText().toString();
        Students students=new Students(email,pass);
        studentDbRef.push().setValue(students);
       Toast.makeText(MainActivity1.this,"Data inserted",Toast.LENGTH_SHORT).show();

    }
}