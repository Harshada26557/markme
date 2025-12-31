package com.example.markme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class teacher_mainactivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_mainactivity);
        button= findViewById(R.id.admin_anouncement);
        button.setOnClickListener(v ->
                startActivity(new Intent(teacher_mainactivity.this, teacher_announcement.class))
        );

    }
}