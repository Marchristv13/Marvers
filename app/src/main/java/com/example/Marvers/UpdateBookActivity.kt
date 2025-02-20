package com.example.Marvers

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.Marvers.databinding.ActivityUpdateBookBinding
import java.util.*

class UpdateBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBookBinding
    private lateinit var dbHelper: DatabaseHelper_Book
    private lateinit var book: Book
    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityUpdateBookBinding.inflate(layoutInflater)
            setContentView(binding.root)

            dbHelper = DatabaseHelper_Book(this)

            if (intent.hasExtra("book")) {
                book = intent.getSerializableExtra("book") as Book
                Log.d("UpdateBookActivity", "Received book: $book")
                loadBookData()
                setupClickListeners()
            } else {
                Log.e("UpdateBookActivity", "No book data received")
                Toast.makeText(this, "Error: Book data not found", Toast.LENGTH_SHORT).show()
                finish()
            }
        } catch (e: Exception) {
            Log.e("UpdateBookActivity", "Error in onCreate", e)
            Toast.makeText(this, "Error initializing update screen", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadBookData() {
        try {
            binding.edtName.setText(book.name ?: "")
            binding.edtNickname.setText(book.nickname ?: "")
            binding.edtEmail.setText(book.email ?: "")
            binding.edtAddress.setText(book.address ?: "")
            binding.edtBirthDate.setText(book.birthDate ?: "")
            binding.edtPhone.setText(book.phoneNumber ?: "")

            if (!book.photoPath.isNullOrEmpty()) {
                try {
                    binding.imgPhoto.setImageURI(Uri.parse(book.photoPath))
                } catch (e: Exception) {
                    Log.e("UpdateBookActivity", "Error loading image", e)
                    binding.imgPhoto.setImageResource(R.drawable.watasi)
                }
            } else {
                binding.imgPhoto.setImageResource(R.drawable.watasi)
            }
        } catch (e: Exception) {
            Log.e("UpdateBookActivity", "Error loading book data", e)
            Toast.makeText(this, "Error loading book data", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupClickListeners() {
        binding.btnUpdatePhoto.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            } catch (e: Exception) {
                Log.e("UpdateBookActivity", "Error launching image picker", e)
                Toast.makeText(this, "Error selecting photo", Toast.LENGTH_SHORT).show()
            }
        }

        binding.edtBirthDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnUpdate.setOnClickListener {
            if (validateInputs()) {
                showUpdateConfirmationDialog()
            }
        }

        binding.btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun showDatePicker() {
        try {
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
        } catch (e: Exception) {
            Log.e("UpdateBookActivity", "Error showing date picker", e)
            Toast.makeText(this, "Error showing date picker", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(): Boolean {
        if (binding.edtName.text.toString().isEmpty()) {
            showError("Please enter a name")
            return false
        }
        if (binding.edtEmail.text.toString().isEmpty()) {
            showError("Please enter an email")
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

    private fun showUpdateConfirmationDialog() {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to update this book?")
            .setPositiveButton("Yes") { _, _ -> updateBook() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to delete this book?")
            .setPositiveButton("Yes") { _, _ -> deleteBook() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun updateBook() {
        try {
            book.apply {
                name = binding.edtName.text.toString()
                nickname = binding.edtNickname.text.toString()
                photoPath = selectedImageUri?.toString() ?: photoPath
                email = binding.edtEmail.text.toString()
                address = binding.edtAddress.text.toString()
                birthDate = binding.edtBirthDate.text.toString()
                phoneNumber = binding.edtPhone.text.toString()
            }

            dbHelper.updateBook(book)
            setResult(Activity.RESULT_OK)
            finish()
        } catch (e: Exception) {
            Log.e("UpdateBookActivity", "Error updating book", e)
            Toast.makeText(this, "Error updating book", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteBook() {
        try {
            dbHelper.deleteBook(book.id)
            setResult(Activity.RESULT_OK)
            finish()
        } catch (e: Exception) {
            Log.e("UpdateBookActivity", "Error deleting book", e)
            Toast.makeText(this, "Error deleting book", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
                selectedImageUri = data.data
                if (selectedImageUri != null) {
                    binding.imgPhoto.setImageURI(selectedImageUri)
                } else {
                    Toast.makeText(this, "Error selecting image", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("UpdateBookActivity", "Error handling image selection result", e)
            Toast.makeText(this, "Error processing selected image", Toast.LENGTH_SHORT).show()
        }
    }
}