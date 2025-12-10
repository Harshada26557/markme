package com.example.markme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jspecify.annotations.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class studentpage extends AppCompatActivity {

    private TextView txtDay, txtDate, txtTime, txtGreeting;
    private ImageView imageView;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentpage);

        // Initialize UI
        txtGreeting = findViewById(R.id.greeting);
        txtDate = findViewById(R.id.date);
        txtTime = findViewById(R.id.time);
        txtDay = findViewById(R.id.day);
        imageView = findViewById(R.id.user_prof);

        // Handle profile click
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(studentpage.this, studentprofile_page.class);
            startActivity(intent);
        });

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        reference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(user.getUid());


        loadProfileImageRealtime();

        updateDateInfo();
        updateTimeLive();
        updateGreeting();
    }

    private void loadProfileImageRealtime() {
        reference.child("profileImage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imageUrl = snapshot.getValue(String.class);
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(studentpage.this)
                            .load(imageUrl)
                            .placeholder(R.drawable.user)
                            .into(imageView);
                } else {
                    imageView.setImageResource(R.drawable.user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(studentpage.this, "Failed to load profile image", Toast.LENGTH_SHORT).show();
                imageView.setImageResource(R.drawable.user);
            }
        });
    }

    private void updateDateInfo() {
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        txtDay.setText(dayFormat.format(new Date()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        txtDate.setText("Date: " + dateFormat.format(new Date()));
    }

    private void updateTimeLive() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
                txtTime.setText("Time: " + timeFormat.format(new Date()));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

    private void updateGreeting() {
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH", Locale.getDefault());
        int hour = Integer.parseInt(hourFormat.format(new Date()));

        String greeting;
        if (hour >= 5 && hour < 12) greeting = "Good Morning ☀️";
        else if (hour >= 12 && hour < 17) greeting = "Good Afternoon 🌤";
        else if (hour >= 17 && hour < 21) greeting = "Good Evening 🌆";
        else greeting = "Good Night 🌙";

        txtGreeting.setText(greeting);
    }
}
