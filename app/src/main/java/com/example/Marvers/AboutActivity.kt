package com.example.Marvers

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class AboutActivity : AppCompatActivity() {
    private var imgProfile: ImageView? = null
    private var tvName: TextView? = null
    private var tvClass: TextView? = null
    private var tvAbsentNumber: TextView? = null
    private var tvCreationDate: TextView? = null
    private var tvVersion: TextView? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        initializeViews()
        setupToolbar()
        setupProfile()
    }

    private fun initializeViews() {
        imgProfile = findViewById(R.id.imgProfile)
        tvName = findViewById(R.id.tvName)
        tvClass = findViewById(R.id.tvClass)
        tvAbsentNumber = findViewById(R.id.tvAbsentNumber)
        tvCreationDate = findViewById(R.id.tvCreationDate)
        tvVersion = findViewById(R.id.tvVersion)
        toolbar = findViewById(R.id.toolbar)
    }

    private fun setupToolbar() {
        toolbar!!.setNavigationOnClickListener { v: View? -> finish() }
    }

    private fun setupProfile() {
        imgProfile!!.setImageResource(R.drawable.watasi)
        tvName!!.text = "Your Name"
        tvClass!!.text = "Your Class"
        tvAbsentNumber!!.text = "Absent Number: XX"
        tvCreationDate!!.text = "Creation Date: 20 February 2025"
        tvVersion!!.text = "Version 1.0.0"
    }
}