package com.example.markme;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class adduser_for_admin extends AppCompatActivity {

    TextInputEditText email, name, subject, phone, password;
    Button savebtn;
    TextView backText;

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

        backText.setOnClickListener(v -> finish());
        savebtn.setOnClickListener(v -> saveTeacher());
    }

    private void saveTeacher() {
        String userName = name.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userSubject = subject.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if (userName.isEmpty()) { name.setError("Name required"); return; }
        if (userEmail.isEmpty()) { email.setError("Email required"); return; }
        if (userSubject.isEmpty()) { subject.setError("Subject required"); return; }
        if (userPhone.isEmpty()) { phone.setError("Phone required"); return; }
        if (userPassword.isEmpty()) { password.setError("Password required"); return; }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String adminId = auth.getCurrentUser().getUid();

        // Create teacher account in Firebase Auth
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnSuccessListener(authResult -> {
                    String teacherUid = authResult.getUser().getUid();

                    MODEL teacher = new MODEL(userName, userEmail, userSubject, userPhone, userPassword);
                    teacher.id = teacherUid;

                    // 1️⃣ Save teacher profile under /Users/Teachers/<teacherUid>
                    DatabaseReference teacherRef = FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child("Teachers")
                            .child(teacherUid);

                    teacherRef.setValue(teacher)
                            .addOnSuccessListener(unused -> {
                                // 2️⃣ Link teacher under admin for announcements
                                DatabaseReference adminTeacherRef = FirebaseDatabase.getInstance()
                                        .getReference("Users")
                                        .child("Admins")
                                        .child(adminId)
                                        .child("Teachers")
                                        .child(teacherUid);

                                adminTeacherRef.setValue(true)
                                        .addOnSuccessListener(aVoid ->
                                                Toast.makeText(this,
                                                        "Teacher added successfully",
                                                        Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e ->
                                                Toast.makeText(this,
                                                        e.getMessage(),
                                                        Toast.LENGTH_LONG).show());
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this,
                                            e.getMessage(),
                                            Toast.LENGTH_LONG).show());
                })
                .addOnFailureListener(e -> {
                    if(e.getMessage().contains("already in use")) {
                        Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
