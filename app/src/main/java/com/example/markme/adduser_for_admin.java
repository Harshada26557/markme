package com.example.markme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class adduser_for_admin extends AppCompatActivity {

    TextInputEditText name_, email_, position_, mono_;
    Button register_;
    TextView txt_back;

    FirebaseAuth auth;
    FirebaseDatabase database;

    String teacherId = null;
    boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser_for_admin);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        name_ = findViewById(R.id.name_);
        email_ = findViewById(R.id.email_);
        position_ = findViewById(R.id.position_);
        mono_ = findViewById(R.id.mono_);
        register_ = findViewById(R.id.register_);
        txt_back = findViewById(R.id.txt_back);

        txt_back.setOnClickListener(v -> finish());

        // CHECK EDIT MODE
        if (getIntent().hasExtra("key")) {
            isEdit = true;
            teacherId = getIntent().getStringExtra("key");

            name_.setText(getIntent().getStringExtra("name"));
            email_.setText(getIntent().getStringExtra("email"));
            position_.setText(getIntent().getStringExtra("subject"));
            mono_.setText(getIntent().getStringExtra("phone"));

            email_.setEnabled(false);   // email should not change
            register_.setText("Update Teacher");
        }

        register_.setOnClickListener(v -> addTeacher());
    }

    private void addTeacher() {

        String name = name_.getText().toString().trim();
        String email = email_.getText().toString().trim();
        String subject = position_.getText().toString().trim();
        String phone = mono_.getText().toString().trim();
        String password = "teacher123"; // Default password or you can have input field for password

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(subject) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String emailKey = email.replace(".", "_");

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("subject", subject);
        map.put("phone", phone);
        map.put("role", "teacher");

        // Step 1: Save in Realtime Database
        FirebaseDatabase.getInstance()
                .getReference("AllowedTeachers")
                .child(emailKey)
                .setValue(map)
                .addOnSuccessListener(unused -> {
                    // Step 2: Create Auth account
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(authResult -> {
                                Toast.makeText(this,
                                        "Teacher added successfully.\nDefault password: " + password,
                                        Toast.LENGTH_LONG).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this,
                                        "Saved in DB but failed to create Auth: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error saving teacher: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
