package com.example.markme;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class teacher_announcement extends AppCompatActivity {

    LinearLayout container;
    String adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_announcement);

        container = findViewById(R.id.containerAnnouncements_);

        // IMPORTANT: pass this when teacher logs in
        adminId = getIntent().getStringExtra("adminId");

        loadAnnouncements();
    }

    private void loadAnnouncements() {

        FirebaseDatabase.getInstance()
                .getReference("Announcements")
                .child(adminId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        container.removeAllViews();

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Announcement a = ds.getValue(Announcement.class);

                            TextView tv = new TextView(teacher_announcement.this);
                            tv.setText(
                                    "Title: " + a.title +
                                            "\n\n" + a.description
                            );
                            tv.setPadding(24,24,24,24);
                            tv.setBackgroundColor(0xFFE5E7EB);

                            LinearLayout.LayoutParams params =
                                    new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0,0,0,20);
                            tv.setLayoutParams(params);

                            container.addView(tv);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }
}
