package com.example.markme;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class admin_announcement extends AppCompatActivity {

    EditText edtTitle, edtDescription;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_announcement);

        edtTitle = findViewById(R.id.etTitle);
        edtDescription = findViewById(R.id.etDescription);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> sendAnnouncement());
    }

    private void sendAnnouncement() {

        String title = edtTitle.getText().toString().trim();
        String desc = edtDescription.getText().toString().trim();

        if (title.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
            return;
        }

        String adminId = FirebaseAuth.getInstance().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Announcements")
                .child(adminId);

        String id = ref.push().getKey();

        Announcement announcement = new Announcement(
                id,
                title,
                desc,
                System.currentTimeMillis()
        );

        ref.child(id).setValue(announcement)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Announcement sent", Toast.LENGTH_SHORT).show();
                    edtTitle.setText("");
                    edtDescription.setText("");
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
