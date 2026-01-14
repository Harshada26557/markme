package com.example.markme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class teacherpage extends AppCompatActivity {

    TextInputEditText email_, password_;
    Button loginBtn;
    TextView forgotTxt, txt_back;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherpage);

        auth = FirebaseAuth.getInstance();

        // Initialize views
        email_ = findViewById(R.id.email_);
        password_ = findViewById(R.id.password_);
        loginBtn = findViewById(R.id.loginBtn);
        forgotTxt = findViewById(R.id.forgotTxt);
        txt_back = findViewById(R.id.txt_back);

        txt_back.setOnClickListener(v -> finish());

        loginBtn.setOnClickListener(v -> loginTeacher());

        forgotTxt.setOnClickListener(v -> resetPassword());
    }

    // ================= LOGIN TEACHER =================
    private void loginTeacher() {

        String email = email_.getText().toString().trim();
        String password = password_.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase Authentication login
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> checkTeacherAccess(email))
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    // ================= CHECK ADMIN APPROVAL =================
    private void checkTeacherAccess(String email) {

        String emailKey = email.replace(".", "_");

        FirebaseDatabase.getInstance()
                .getReference("AllowedTeachers")
                .child(emailKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            // Teacher is allowed by admin
                            startActivity(new Intent(teacherpage.this, teacher_mainactivity.class));
                            finish();
                        } else {
                            // Teacher is not approved
                            auth.signOut();
                            Toast.makeText(teacherpage.this,
                                    "Access denied. Please contact admin.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(teacherpage.this,
                                "Database error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    // ================= RESET PASSWORD =================
    private void resetPassword() {

        String email = email_.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter email to reset password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this,
                                "Password reset email sent",
                                Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Failed: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
    }
}
