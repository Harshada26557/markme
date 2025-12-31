package com.example.markme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class admin_mainactivity extends AppCompatActivity {

    ImageView addUserImage;
    TextView teacherCount,calender,announcement;
    CardView teacherCard, studentCard, percentageCard;

    String adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_mainactivity);


        addUserImage = findViewById(R.id.add_user);
        teacherCount = findViewById(R.id.txtTeacherCount);
        teacherCard = findViewById(R.id.teachers_);
        studentCard = findViewById(R.id.card_attendance);
        percentageCard = findViewById(R.id.card_homework);
        calender = findViewById(R.id._calender);
        announcement = findViewById(R.id.announce_admin);

        adminId = FirebaseAuth.getInstance().getCurrentUser().getUid();

      addUserImage.setOnClickListener(v ->
                startActivity(new Intent(admin_mainactivity.this, adduser_for_admin.class))
        );


        teacherCard.setOnClickListener(v ->
                startActivity(new Intent(admin_mainactivity.this, saved_teachers.class))
        );
        calender.setOnClickListener(v ->
                startActivity(new Intent(admin_mainactivity.this, calender.class))
        );
        announcement.setOnClickListener(v ->
                startActivity(new Intent(admin_mainactivity.this, admin_announcement.class))
        );


        FirebaseDatabase.getInstance()
                .getReference("Users")
                .child("Admins")
                .child(adminId)
                .child("Teachers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long count = snapshot.getChildrenCount();
                        teacherCount.setText(String.valueOf(count));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(admin_mainactivity.this,
                                error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
