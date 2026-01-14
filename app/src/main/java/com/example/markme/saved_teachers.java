package com.example.markme;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class saved_teachers extends AppCompatActivity {

    RecyclerView recyclerView;
    TeacherAdapter adapter;
    List<MODEL> teacherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.saved_teachers);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        teacherList = new ArrayList<>();
        adapter = new TeacherAdapter(this, teacherList);
        recyclerView.setAdapter(adapter);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            fetchTeachers();
        } else {
            Toast.makeText(this, "You must be logged in", Toast.LENGTH_LONG).show();
        }
    }
    private void fetchTeachers() {

        FirebaseDatabase.getInstance()
                .getReference("AllowedTeachers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        teacherList.clear();

                        if (snapshot.exists()) {
                            for (DataSnapshot tSnap : snapshot.getChildren()) {

                                MODEL t = tSnap.getValue(MODEL.class);
                                if (t != null) {
                                    t.id = tSnap.getKey(); // emailKey
                                    teacherList.add(t);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(saved_teachers.this,
                                    "No teachers found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(saved_teachers.this,
                                "Error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

}
