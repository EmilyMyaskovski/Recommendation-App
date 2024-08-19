package com.example.recom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileSetupActivity extends AppCompatActivity {

    private EditText username;
    private EditText full_name;
    private Button join;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        findViews();

        join.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        String usernameValue = username.getText().toString().trim();
        String fullnameValue = full_name.getText().toString().trim();

        if (usernameValue.isEmpty() || fullnameValue.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUser != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(usernameValue)
                    .build();

            currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            saveToSharedPreferences(usernameValue, fullnameValue);
                            saveToFireBase(usernameValue, fullnameValue);

                            // Pass data to ProfileActivity
                            Intent intent = new Intent(ProfileSetupActivity.this, ProfileActivity.class);
                            intent.putExtra("USERNAME", usernameValue);
                            intent.putExtra("FULL_NAME", fullnameValue);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ProfileSetupActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveToSharedPreferences(String usernameValue, String fullnameValue) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USERNAME", usernameValue);
        editor.putString("FULL_NAME", fullnameValue);
        editor.apply();
    }

    private void saveToFireBase(String usernameValue, String fullNameValue) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("username", usernameValue);
        user.put("fullName", fullNameValue);

        db.collection("users").document(currentUser.getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d("ProfileSetupActivity", "User info successfully written!"))
                .addOnFailureListener(e -> Log.d("ProfileSetupActivity", "Error writing user info", e));
    }

    private void findViews() {
        username = findViewById(R.id.username);
        full_name = findViewById(R.id.full_name);
        join = findViewById(R.id.join);
    }
}
