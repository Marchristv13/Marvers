package com.example.Marvers

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.Marvers.databinding.ActivityAddBookBinding
import java.util.*

class AddBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBookBinding
    private lateinit var dbHelper: DatabaseHelper_Book
    private var selectedImageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper_Book(this)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Pilih Foto
        binding.btnChoosePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Pilih Tanggal
        binding.edtBirthDate.setOnClickListener {
            showDatePicker()
        }

        // Simpan Data Buku
        binding.btnSave.setOnClickListener {
            if (validateInputs()) {
                saveBook()
            }
        }

        // Tombol Back pada Toolbar
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                binding.edtBirthDate.setText("$day/${month + 1}/$year")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun validateInputs(): Boolean {
        if (binding.edtName.text.toString().isEmpty()) {
            showError("Please enter a name")
            return false
        }
        if (binding.edtNickname.text.toString().isEmpty()) {
            showError("Please enter a nickname")
            return false
        }
        if (binding.edtEmail.text.toString().isEmpty()) {
            showError("Please enter an email")
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.text.toString()).matches()) {
            showError("Invalid email format")
            return false
        }
        if (binding.edtPhone.text.toString().isEmpty()) {
            showError("Please enter a phone number")
            return false
        }
        return true
    }

    private fun showError(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun saveBook() {
        val book = Book(
            name = binding.edtName.text.toString(),
            nickname = binding.edtNickname.text.toString(),
            photoPath = selectedImageUri?.toString() ?: "No Image",
            email = binding.edtEmail.text.toString(),
            address = binding.edtAddress.text.toString(),
            birthDate = binding.edtBirthDate.text.toString(),
            phoneNumber = binding.edtPhone.text.toString(),
            timestamp = System.currentTimeMillis()
        )

        dbHelper.addBook(book)
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            if (selectedImageUri != null) {
                binding.imgPhoto.setImageURI(selectedImageUri)
            } else {
                showError("Failed to select image")
            }
        }
    }
}
