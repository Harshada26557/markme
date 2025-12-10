package com.example.markme;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    CardView card1,card2,card3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        card1 = findViewById(R.id.student_card);

        card1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, loginpage.class);
            startActivity(intent);
        });
        card2 = findViewById(R.id.teacher_card);

        card2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, teacherpage.class);
            startActivity(intent);
        });
        card3 = findViewById(R.id.admin_card);

        card3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, adminpage.class);
            startActivity(intent);
        });


    }
}