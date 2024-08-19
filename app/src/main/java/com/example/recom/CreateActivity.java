package com.example.recom;
import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int CAPTURE_IMAGE = 2;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_READ_STORAGE_PERMISSION = 1001;

    private RatingBar rating_bar;
    private EditText overview_bar;
    private Spinner category_bar;
    private EditText link_bar;
    private TextView title;
    private Button publish;
    private Button image_upload;
    private ImageButton back_button;
    private Uri selectedImageUri;
    private Uri photoUri;
    private String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        findViews();
        initViews();
    }

    private void initViews() {
        image_upload.setOnClickListener(v -> {
            requestPermissionsIfNecessary();
        });

        back_button.setOnClickListener(v -> finish());

        publish.setOnClickListener(v -> saveDataToDatabase());


    }

    private void findViews() {
        publish = findViewById(R.id.publish);
        image_upload = findViewById(R.id.image_upload);
        rating_bar = findViewById(R.id.rating_bar);
        overview_bar = findViewById(R.id.overview_bar);
        category_bar = findViewById(R.id.category_bar);
        link_bar = findViewById(R.id.link_bar);
        back_button = findViewById(R.id.back_button);
        title = findViewById(R.id.title);
    }

    private void requestPermissionsIfNecessary() {
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        boolean shouldShowRationale = false;
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                shouldShowRationale = true;
                break;
            }
        }

        if (shouldShowRationale) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This app needs camera and storage permissions to upload photos.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        ActivityCompat.requestPermissions(CreateActivity.this, permissions, PERMISSION_REQUEST_CODE);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK) {
            // photo saved to file
            selectedImageUri = FileProvider.getUriForFile(this, "com.example.recom.fileprovider", new File(currentPhotoPath));
            grantUriPermission(getPackageName(), selectedImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            image_upload.setVisibility(View.VISIBLE);
        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            grantUriPermission(getPackageName(), selectedImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            image_upload.setVisibility(View.VISIBLE);
        }
        if(selectedImageUri != null)
            image_upload.setText("📎image attached");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageSourceOptions();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_READ_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Storage permission is required to access images.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void showImageSourceOptions() {
        String[] options = {"Camera", "Gallery"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                openCamera();
            } else if (which == 1) {
                openGallery();
            }
        });
        builder.show();
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Handle the error
            }
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this,
                        "com.example.recom.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void saveDataToDatabase() {
        float rating = rating_bar.getRating();
        String overview = overview_bar.getText().toString();
        String category = category_bar.getSelectedItem().toString();
        String link = link_bar.getText().toString();
        String title = this.title.getText().toString();
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        LocalDate postedOn = LocalDate.now();
        String formattedDate = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
        Uri image = selectedImageUri;

        Log.d("CreateActivity", "UserID: " + userId);

        // Save to SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("POST_DATE", formattedDate);
        editor.apply();

        Map<String, Object> recommendationData = new HashMap<>();
        recommendationData.put("rating", rating);
        recommendationData.put("title", title);
        recommendationData.put("overview", overview);
        recommendationData.put("category", category);
        recommendationData.put("link", link);
        recommendationData.put("username", username);
        recommendationData.put("postedOn", formattedDate);
        recommendationData.put("imageUri", image != null ? image.toString() : null);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("recommendations");
        String recommendationId = dbRef.push().getKey();  // Generate a unique key for the recommendation

        if (recommendationId != null) {
            Log.d("CreateActivity", "Saving data under UserID: " + userId);

            dbRef.child(recommendationId).setValue(recommendationData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("CreateActivity", "Data saved successfully");
                    Toast.makeText(CreateActivity.this, "Recommendation saved successfully", Toast.LENGTH_SHORT).show();

                    // Prepare the data to send back to ProfileActivity
                    Intent intent = new Intent();
                    intent.putExtra("POST_RATING", rating);
                    intent.putExtra("POST_OVERVIEW", overview);
                    intent.putExtra("POST_CATEGORY", category);
                    intent.putExtra("POST_LINK", link);
                    intent.putExtra("POST_TITLE", title);
                    intent.putExtra("USERNAME", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    intent.putExtra("POST_IMAGE_URI", selectedImageUri != null ? selectedImageUri.toString() : null);
                    intent.putExtra("POST_DATE", formattedDate);
                    setResult(RESULT_OK, intent);
                    finish();

                } else {
                    Log.e("CreateActivity", "Failed to save data", task.getException());  // Log failure
                    Toast.makeText(CreateActivity.this, "Failed to save recommendation", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("CreateActivity", "Failed to generate recommendationId");
        }
    }
}