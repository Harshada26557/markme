package com.example.markme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class calender extends AppCompatActivity {

    CalendarView calendarView;
    TextView txtSelectedDate;
    EditText edtNote;
    Button btnSaveNote;

    String selectedDateKey = "";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        calendarView = findViewById(R.id.calendarView);
        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        edtNote = findViewById(R.id.edtNote);
        btnSaveNote = findViewById(R.id.btnSaveNote);

        preferences = getSharedPreferences("CalendarNotes", MODE_PRIVATE);

        // Default today
        Calendar calendar = Calendar.getInstance();
        updateDate(calendar);
        loadNoteForDate();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, dayOfMonth);
            updateDate(selected);
            loadNoteForDate(); // 🔥 load note when date clicked
        });

        btnSaveNote.setOnClickListener(v -> {
            String note = edtNote.getText().toString().trim();

            if (note.isEmpty()) {
                Toast.makeText(this, "Write something first", Toast.LENGTH_SHORT).show();
                return;
            }

            preferences.edit()
                    .putString(selectedDateKey, note)
                    .apply();

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateDate(Calendar calendar) {
        SimpleDateFormat display =
                new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        txtSelectedDate.setText("Selected Date: " + display.format(calendar.getTime()));

        // key format
        SimpleDateFormat key =
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDateKey = key.format(calendar.getTime());
    }

    private void loadNoteForDate() {
        String savedNote = preferences.getString(selectedDateKey, "");
        edtNote.setText(savedNote);
    }
}
