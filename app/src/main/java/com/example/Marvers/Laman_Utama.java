package com.example.Marvers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

public class Laman_Utama extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 123;
    private ImageView profileImage;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laman_utama);

        // Initialize UI components
        initializeViews();

        // Setup image picker
        setupImagePicker();

        // Setup click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        profileImage = findViewById(R.id.profil);
        Button btnClose = findViewById(R.id.btn_keluar);
        LinearLayout calculatorMenu = findViewById(R.id.calculator_menu);
        LinearLayout noteMenu = findViewById(R.id.note_menu);
        LinearLayout bookMenu = findViewById(R.id.book_menu); // Tambahkan inisialisasi untuk bookMenu
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        try {
                            profileImage.setImageURI(selectedImageUri);
                            Toast.makeText(this, "Profile photo updated successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(this, "Failed to update profile photo", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void setupClickListeners() {
        // Profile image click listener
        profileImage.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                openImagePicker();
            } else {
                requestStoragePermission();
            }
        });

        // Back button click listener
        Button btnClose = findViewById(R.id.btn_keluar);
        btnClose.setOnClickListener(v -> {
            Intent intent = new Intent(Laman_Utama.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Calculator menu click listener
        LinearLayout calculatorMenu = findViewById(R.id.calculator_menu);
        calculatorMenu.setOnClickListener(v -> {
            Intent intent = new Intent(Laman_Utama.this, MainActivity.class);
            startActivity(intent);
        });

        // Note menu click listener
        LinearLayout noteMenu = findViewById(R.id.note_menu);
        noteMenu.setOnClickListener(v -> {
            Intent intent = new Intent(Laman_Utama.this, NoteActivity.class);
            startActivity(intent);
        });

        // Book menu click listener
        LinearLayout bookMenu = findViewById(R.id.book_menu);
        bookMenu.setOnClickListener(v -> {
            Intent intent = new Intent(Laman_Utama.this, BookActivity.class);
            startActivity(intent);
        });
    }

    private boolean checkStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Permission denied. Cannot select profile photo.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Add any necessary state restoration here
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Add any necessary state saving here
    }
}