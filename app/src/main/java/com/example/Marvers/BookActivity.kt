package com.example.Marvers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Marvers.databinding.ActivityBookBinding

class BookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookBinding
    private lateinit var dbHelper: DatabaseHelper_Book
    private lateinit var adapter: BookAdapter
    private val ADD_BOOK_REQUEST = 1
    private val UPDATE_BOOK_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityBookBinding.inflate(layoutInflater)
            setContentView(binding.root)

            dbHelper = DatabaseHelper_Book(this)
            setupRecyclerView()
            setupButtons()
            loadBooks()
        } catch (e: Exception) {
            Log.e("BookActivity", "Error in onCreate", e)
            Toast.makeText(this, "Error initializing activity", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupRecyclerView() {
        try {
            adapter = BookAdapter()
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = adapter

            adapter.setOnEditClickListener { book ->
                try {
                    Log.d("BookActivity", "Editing book: $book")
                    val intent = Intent(this, UpdateBookActivity::class.java)
                    intent.putExtra("book", book)
                    startActivityForResult(intent, UPDATE_BOOK_REQUEST)
                } catch (e: Exception) {
                    Log.e("BookActivity", "Error launching edit activity", e)
                    Toast.makeText(this, "Error opening edit screen", Toast.LENGTH_SHORT).show()
                }
            }

            adapter.setOnDeleteClickListener { book ->
                showDeleteConfirmationDialog(book)
            }
        } catch (e: Exception) {
            Log.e("BookActivity", "Error setting up RecyclerView", e)
            Toast.makeText(this, "Error setting up book list", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDeleteConfirmationDialog(book: Book) {
        AlertDialog.Builder(this)
            .setTitle("Delete Book")
            .setMessage("Are you sure you want to delete this book?")
            .setPositiveButton("Yes") { _, _ ->
                try {
                    dbHelper.deleteBook(book.id)
                    loadBooks()
                } catch (e: Exception) {
                    Log.e("BookActivity", "Error deleting book", e)
                    Toast.makeText(this, "Error deleting book", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun setupButtons() {
        binding.fabAdd.setOnClickListener {
            try {
                startActivityForResult(
                    Intent(this, AddBookActivity::class.java),
                    ADD_BOOK_REQUEST
                )
            } catch (e: Exception) {
                Log.e("BookActivity", "Error launching add activity", e)
                Toast.makeText(this, "Error opening add screen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadBooks() {
        try {
            val books = dbHelper.allBooks
            adapter.updateBooks(books)
            binding.tvEmpty.visibility = if (books.isEmpty()) View.VISIBLE else View.GONE
        } catch (e: Exception) {
            Log.e("BookActivity", "Error loading books", e)
            Toast.makeText(this, "Error loading books", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK) {
                loadBooks()
            }
        } catch (e: Exception) {
            Log.e("BookActivity", "Error in onActivityResult", e)
            Toast.makeText(this, "Error refreshing book list", Toast.LENGTH_SHORT).show()
        }
    }
}
