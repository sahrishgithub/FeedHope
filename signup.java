package com.example.helloword;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class signup extends AppCompatActivity {
    EditText firstname,lastname,email,password,phone;
    Button signupbtn,btngotologin;
    dbhelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        btngotologin = findViewById(R.id.signupbtn);
        btngotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signup.this,Home.class);
                startActivity(intent);
            }
        });
        firstname=findViewById(R.id.fname);
        lastname=findViewById(R.id.lname);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        phone=findViewById(R.id.phone);
        signupbtn=findViewById(R.id.signupbtn);
        dbhelper=new dbhelper(this);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fnam,lnam,mail,pass,phon;
                fnam=firstname.getText().toString();
                lnam=lastname.getText().toString();
                mail=email.getText().toString();
                pass=password.getText().toString();
                phon=phone.getText().toString();
                if(fnam.equals("") || lnam.equals("") || mail.equals("") || pass.equals("") || phon.equals("")){
                    Toast.makeText(signup.this, "Please fill all the required field!",Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean signupsuccessfully=dbhelper.insertData(fnam,lnam,mail,pass,phon);
                    if(signupsuccessfully){
                        Toast.makeText(signup.this, "User signup successfully!",Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(signup.this, "User signup fail!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}