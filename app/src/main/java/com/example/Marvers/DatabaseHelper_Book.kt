package com.example.Marvers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper_Book(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_BOOKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT NOT NULL,"
                + KEY_NICKNAME + " TEXT NOT NULL,"
                + KEY_PHOTO + " TEXT NOT NULL,"
                + KEY_EMAIL + " TEXT NOT NULL,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_BIRTH_DATE + " TEXT NOT NULL,"
                + KEY_PHONE + " TEXT NOT NULL,"
                + KEY_TIMESTAMP + " INTEGER"
                + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        onCreate(db)
    }

    fun addBook(book: Book): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, book.name)
            put(KEY_NICKNAME, book.nickname)
            put(KEY_PHOTO, book.photoPath)
            put(KEY_EMAIL, book.email)
            put(KEY_ADDRESS, book.address)
            put(KEY_BIRTH_DATE, book.birthDate)
            put(KEY_PHONE, book.phoneNumber)
            put(KEY_TIMESTAMP, book.timestamp)
        }
        val result = db.insert(TABLE_BOOKS, null, values)
        db.close()
        return result
    }

    fun updateBook(book: Book): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, book.name)
            put(KEY_NICKNAME, book.nickname)
            put(KEY_PHOTO, book.photoPath)
            put(KEY_EMAIL, book.email)
            put(KEY_ADDRESS, book.address)
            put(KEY_BIRTH_DATE, book.birthDate)
            put(KEY_PHONE, book.phoneNumber)
        }

        val result = db.update(TABLE_BOOKS, values, "$KEY_ID = ?", arrayOf(book.id.toString()))
        db.close()
        return result
    }

    fun deleteBook(bookId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_BOOKS, "$KEY_ID = ?", arrayOf(bookId.toString()))
        db.close()
        return result
    }

    val allBooks: List<Book>
        get() {
            val books = mutableListOf<Book>()
            val selectQuery = "SELECT * FROM $TABLE_BOOKS ORDER BY $KEY_TIMESTAMP DESC"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            cursor.use {
                if (it.moveToFirst()) {
                    do {
                        books.add(it.toBook())
                    } while (it.moveToNext())
                }
            }
            db.close()
            return books
        }

    private fun Cursor.toBook(): Book {
        return Book().apply {
            id = getIntOrNull(getColumnIndex(KEY_ID)) ?: 0
            name = getStringOrNull(getColumnIndex(KEY_NAME)) ?: ""
            nickname = getStringOrNull(getColumnIndex(KEY_NICKNAME)) ?: ""
            photoPath = getStringOrNull(getColumnIndex(KEY_PHOTO)) ?: ""
            email = getStringOrNull(getColumnIndex(KEY_EMAIL)) ?: ""
            address = getStringOrNull(getColumnIndex(KEY_ADDRESS)) ?: ""
            birthDate = getStringOrNull(getColumnIndex(KEY_BIRTH_DATE)) ?: ""
            phoneNumber = getStringOrNull(getColumnIndex(KEY_PHONE)) ?: ""
            timestamp = getLongOrNull(getColumnIndex(KEY_TIMESTAMP)) ?: 0L
        }
    }

    private fun Cursor.getIntOrNull(columnIndex: Int): Int? {
        return if (columnIndex != -1 && !isNull(columnIndex)) getInt(columnIndex) else null
    }

    private fun Cursor.getStringOrNull(columnIndex: Int): String? {
        return if (columnIndex != -1 && !isNull(columnIndex)) getString(columnIndex) else null
    }

    private fun Cursor.getLongOrNull(columnIndex: Int): Long? {
        return if (columnIndex != -1 && !isNull(columnIndex)) getLong(columnIndex) else null
    }

    companion object {
        private const val DATABASE_NAME = "BookDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_BOOKS = "books"

        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_NICKNAME = "nickname"
        private const val KEY_PHOTO = "photo"
        private const val KEY_EMAIL = "email"
        private const val KEY_ADDRESS = "address"
        private const val KEY_BIRTH_DATE = "birth_date"
        private const val KEY_PHONE = "phone"
        private const val KEY_TIMESTAMP = "timestamp"
    }
}
