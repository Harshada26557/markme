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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminpage extends AppCompatActivity {

    TextInputEditText email, password;
    Button loginBtn;
    TextView registerText, backText;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email_);
        password = findViewById(R.id.pass_);
        loginBtn = findViewById(R.id.login_);
        registerText = findViewById(R.id.register_text);
        backText = findViewById(R.id.txt_back);

        loginBtn.setOnClickListener(v -> loginAdmin());

        registerText.setOnClickListener(v ->
                startActivity(new Intent(adminpage.this, admin_register.class))
        );

        backText.setOnClickListener(v ->
                startActivity(new Intent(adminpage.this, MainActivity.class))
        );
    }

    private void loginAdmin() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)) { email.setError("Email required"); return; }
        if (TextUtils.isEmpty(userPassword)) { password.setError("Password required"); return; }

        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = auth.getCurrentUser().getUid();
                        DatabaseReference ref = FirebaseDatabase.getInstance()
                                .getReference("Users")
                                .child("Admins")
                                .child(uid);

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Toast.makeText(adminpage.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(adminpage.this, admin_mainactivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(adminpage.this, "You are not registered as Admin", Toast.LENGTH_LONG).show();
                                    auth.signOut();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                Toast.makeText(adminpage.this, "Database error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(adminpage.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
