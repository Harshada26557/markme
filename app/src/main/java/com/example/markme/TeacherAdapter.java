package com.example.markme;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {

    Context context;
    List<MODEL> list;

    public TeacherAdapter(Context context, List<MODEL> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.teacher_rows, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {

        MODEL t = list.get(position);

        h.name.setText(t.name);
        h.email.setText(t.email);
        h.subject.setText(t.subject);
        h.phone.setText(t.phone);

        // 🔄 EDIT TEACHER
        h.edit.setOnClickListener(v -> {
            Intent i = new Intent(context, adduser_for_admin.class);
            i.putExtra("key", t.id); // Firebase key
            i.putExtra("name", t.name);
            i.putExtra("email", t.email);
            i.putExtra("subject", t.subject);
            i.putExtra("phone", t.phone);
            context.startActivity(i);
        });

        // Delete teacher
        h.delete.setOnClickListener(v -> {
            int pos = h.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            FirebaseDatabase.getInstance()
                    .getReference("AllowedTeachers")
                    .child(t.id)
                    .removeValue()
                    .addOnSuccessListener(unused -> {
                        list.remove(pos);
                        notifyItemRemoved(pos);
                        Toast.makeText(context,
                                "Teacher deleted", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context,
                                    e.getMessage(), Toast.LENGTH_LONG).show()
                    );
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, subject, phone;
        Button edit, delete;

        ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.txtName);
            email = v.findViewById(R.id.txtEmail);
            subject = v.findViewById(R.id.txtSubject);
            phone = v.findViewById(R.id.txtPhone);
            edit = v.findViewById(R.id.btnEdit);
            delete = v.findViewById(R.id.btnDelete);
        }
    }
}