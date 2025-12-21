package com.example.markme;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        fetchTeachers();
    }

    private void fetchTeachers() {
        FirebaseDatabase.getInstance()
                .getReference("Users")
                .child("Teachers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        teacherList.clear();
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            MODEL user = userSnapshot.getValue(MODEL.class);
                            if (user != null) {
                                user.id = userSnapshot.getKey(); // assign Firebase key as id
                                teacherList.add(user);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(saved_teachers.this,
                                error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
