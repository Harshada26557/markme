package com.example.markme;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class studentprofile_page extends AppCompatActivity {

    private TextView userName, userEmail, userRoll, userClass;
    private ImageView profileImage;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private StorageReference storageReference;

    private Uri imageUri;
    private ActivityResultLauncher<String> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentprofile_page);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");

        // Initialize UI
        userName = findViewById(R.id.txt_name);
        userEmail = findViewById(R.id.txt_email);
        userRoll = findViewById(R.id.txt_roll);
        userClass = findViewById(R.id.txt_class);
        profileImage = findViewById(R.id.profile_image);

        loadUserData();
        loadProfileImage();

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageUri = uri;
                        profileImage.setImageURI(uri); // Show selected image immediately
                        uploadProfileImage(uri);
                    }
                }
        );

        profileImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
    }


    private void loadUserData() {
        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String name = task.getResult().child("name").getValue(String.class);
                String email = task.getResult().child("email").getValue(String.class);
                String roll = task.getResult().child("roll").getValue(String.class);
                String classYear = task.getResult().child("class").getValue(String.class);

                userName.setText("Name: " + (name != null ? name : ""));
                userEmail.setText("Email: " + (email != null ? email : ""));
                userRoll.setText("Roll No: " + (roll != null ? roll : ""));
                userClass.setText("Class: " + (classYear != null ? classYear : ""));
            } else {
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProfileImage() {
        reference.child("profileImage").get().addOnSuccessListener(snapshot -> {
            String imageUrl = snapshot.getValue(String.class);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.user) // default image if loading
                        .into(profileImage);
            } else {
                profileImage.setImageResource(R.drawable.user); // default image
            }
        }).addOnFailureListener(e -> {
            profileImage.setImageResource(R.drawable.user);
        });
    }


    private void uploadProfileImage(Uri uri) {
        StorageReference imgRef = storageReference.child(user.getUid() + ".jpg");

        imgRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot ->
                        imgRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            // Save download URL to Realtime Database
                            reference.child("profileImage").setValue(downloadUri.toString());
                            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                        })
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
