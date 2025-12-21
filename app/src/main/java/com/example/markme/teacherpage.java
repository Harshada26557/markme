package com.example.markme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class teacherpage extends AppCompatActivity {

    TextInputEditText emailField, passwordField;
    Button loginBtn;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherpage);

        emailField = findViewById(R.id.email_);
        passwordField = findViewById(R.id.pass_);
        loginBtn = findViewById(R.id.login_);

        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(v -> loginTeacher());


    }

    private void loginTeacher() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty()) { emailField.setError("Email required"); return; }
        if (password.isEmpty()) { passwordField.setError("Password required"); return; }

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, teacher_mainactivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show());
    }
}
