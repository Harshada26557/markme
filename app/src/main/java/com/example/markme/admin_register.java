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

public class admin_register extends AppCompatActivity {

    TextInputEditText email, password, department;
    Button registerBtn;
    TextView loginText, backText;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);


        auth = FirebaseAuth.getInstance();


        email = findViewById(R.id.email_);
        password = findViewById(R.id.pass_);
        department = findViewById(R.id.department_);
        registerBtn = findViewById(R.id.register_);
        loginText = findViewById(R.id.login_text);
        backText = findViewById(R.id.txt_back);


        registerBtn.setOnClickListener(v -> registerUser());


        loginText.setOnClickListener(v ->
                startActivity(new Intent(admin_register.this, adminpage.class))
        );

        // Back Button Click
        backText.setOnClickListener(v -> finish());
    }

    private void registerUser() {

        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userdepartment = department.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)) {
            email.setError("Email required");
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            password.setError("Password required");
            return;
        }

        if (userPassword.length() < 6) {
            password.setError("Password must be at least 6 characters");
            return;
        }

        if (TextUtils.isEmpty(userdepartment)) {
            department.setError("Please add department");
            return;
        }

        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        String uid = auth.getCurrentUser().getUid();


                        AdminModel admin = new AdminModel(
                                userEmail,
                                userdepartment
                        );


                        FirebaseDatabase.getInstance()
                                .getReference("Users")
                                .child("Admins")
                                .child(uid)
                                .setValue(admin);

                        Toast.makeText(this,
                                "Registration Successful",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(
                                admin_register.this,
                                admin_mainactivity.class
                        );
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        Toast.makeText(this,
                                "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}