package com.example.projectadvocata.ui.profile

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projectadvocata.R

class AboutUsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutus)
        supportActionBar?.hide()

        val aboutUsTitle: TextView = findViewById(R.id.aboutUsTitle)
        val aboutUsDescription: TextView = findViewById(R.id.aboutUsDescription)
        val backButton: Button = findViewById(R.id.backButton)

    }

    fun onBackButtonClicked(view: android.view.View) {
        finish()
    }
}