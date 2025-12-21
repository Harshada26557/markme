package com.example.markme;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class adduser_for_admin extends AppCompatActivity {

    TextInputEditText email, name, subject, phone, password;
    Button savebtn;
    TextView backText;

    private String teacherId = null; // stores UID for edit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser_for_admin);

        email = findViewById(R.id.email_);
        name = findViewById(R.id.name_);
        subject = findViewById(R.id.position_);
        phone = findViewById(R.id.mono_);
        password = findViewById(R.id.pass_);
        savebtn = findViewById(R.id.register_);
        backText = findViewById(R.id.txt_back);

        // Check if this is an edit operation
        teacherId = getIntent().getStringExtra("id");
        if (teacherId != null) {
            name.setText(getIntent().getStringExtra("name"));
            email.setText(getIntent().getStringExtra("email"));
            subject.setText(getIntent().getStringExtra("subject"));
            phone.setText(getIntent().getStringExtra("phone"));
            password.setText(getIntent().getStringExtra("password"));
        }

        savebtn.setOnClickListener(v -> saveUser());
        backText.setOnClickListener(v -> finish());
    }

    private void saveUser() {
        String userName = name.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userSubject = subject.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if (userName.isEmpty()) { name.setError("Name required"); return; }
        if (userEmail.isEmpty()) { email.setError("Email required"); return; }
        if (userSubject.isEmpty()) { subject.setError("Subject required"); return; }
        if (userPhone.isEmpty()) { phone.setError("Phone required"); return; }

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    MODEL user = new MODEL(userName, userEmail, userSubject, userPhone, userPassword);
                    user.id = uid;

                    FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child("Teachers")
                            .child("uid")
                            .setValue(user)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(this, "Teacher added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            })

                            .addOnFailureListener(e ->
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show()

                            );
                })
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
