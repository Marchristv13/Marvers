package com.example.Marvers

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(private var books: MutableList<Book> = mutableListOf()) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var onEditClickListener: ((Book) -> Unit)? = null
    private var onDeleteClickListener: ((Book) -> Unit)? = null

    fun setOnEditClickListener(listener: (Book) -> Unit) {
        onEditClickListener = listener
    }

    fun setOnDeleteClickListener(listener: (Book) -> Unit) {
        onDeleteClickListener = listener
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.imgPhoto)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvNickname: TextView = itemView.findViewById(R.id.tvNickname)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]

        if (!book.photoPath.isNullOrEmpty()) {
            holder.imgPhoto.setImageURI(Uri.parse(book.photoPath))
        } else {
            holder.imgPhoto.setImageResource(R.drawable.watasi)
        }

        holder.tvName.text = book.name
        holder.tvNickname.text = book.nickname
        holder.tvEmail.text = book.email

        holder.btnEdit.setOnClickListener {
            onEditClickListener?.invoke(book)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClickListener?.invoke(book)
        }
    }

    override fun getItemCount(): Int = books.size

    fun updateBooks(newBooks: List<Book>) {
        books.clear()
        books.addAll(newBooks)
        notifyDataSetChanged()
    }
}