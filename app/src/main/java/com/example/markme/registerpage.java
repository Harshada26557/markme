package com.example.markme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class registerpage extends AppCompatActivity {

    private static final String TAG = "Register_page";

    private TextInputEditText emailEditText, passwordEditText, nameEditText, rollEditText;
    private AutoCompleteTextView selectautocomplete;
    private Button registerButton;
    private FirebaseAuth mAuth;
    TextView registerLink,backtext;

    private final String[] div = {"select year", "1st", "2nd", "3rd","4th"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpage);

        // Getting XML components
        nameEditText = findViewById(R.id.name_);
        rollEditText = findViewById(R.id.roll_);
        emailEditText = findViewById(R.id.email_);
        passwordEditText = findViewById(R.id.pass_);
        selectautocomplete = findViewById(R.id.class_);
        registerButton = findViewById(R.id._btn);
        registerLink = findViewById(R.id.register_text);
        backtext = findViewById(R.id.txt_back);
        backtext.setOnClickListener(v ->
                startActivity(new Intent(registerpage.this,MainActivity.class))
        );

        registerLink.setOnClickListener(v ->
                startActivity(new Intent(registerpage.this, loginpage.class))
        );

        mAuth = FirebaseAuth.getInstance();

        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, div);
        selectautocomplete.setAdapter(classAdapter);

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {

        String name = nameEditText.getText().toString().trim();
        String roll = rollEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String selectedClass = selectautocomplete.getText().toString().trim();


        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(roll)) {
            Toast.makeText(this, "Please enter roll number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedClass.equalsIgnoreCase("select year") || selectedClass.isEmpty()) {
            Toast.makeText(this, "Please select class/year", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Failed: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser == null) return;

                    DatabaseReference userRef = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(firebaseUser.getUid());

                    // Data to save
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("name", name);
                    userMap.put("roll", roll);
                    userMap.put("email", email);
                    userMap.put("class", selectedClass);

                    // Upload to Firebase
                    userRef.setValue(userMap).addOnCompleteListener(roleTask -> {
                        if (roleTask.isSuccessful()) {
                            Intent intent = new Intent(registerpage.this, studentpage.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Error: " +
                                            roleTask.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                });
    }
}
