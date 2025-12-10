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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class loginpage extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerLink ,backtext;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        emailEditText = findViewById(R.id.email_);
        passwordEditText = findViewById(R.id.pass_);
        loginButton = findViewById(R.id.login_);
        registerLink = findViewById(R.id.register_text);
        backtext = findViewById(R.id.txt_back);
        backtext.setOnClickListener(v ->
                startActivity(new Intent(loginpage.this,MainActivity.class))
                );



        registerLink.setOnClickListener(v ->
                startActivity(new Intent(loginpage.this, registerpage.class))
        );


        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Login Failed: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    startActivity(new Intent(loginpage.this, studentpage.class));
                    finish();
                });
    }
}
