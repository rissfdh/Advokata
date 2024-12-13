package com.example.projectadvocata.ui.compliment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.projectadvocata.R

class DetailNews : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_news)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Berita"

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val imgPhoto: ImageView = findViewById(R.id.img_item_photo)
        val tvName: TextView = findViewById(R.id.tv_item_name)
        val tvDescription: TextView = findViewById(R.id.tv_item_description)

        progressBar.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            val newsName = intent.getStringExtra("NEWS_NAME")
            val newsDescription = intent.getStringExtra("NEWS_DESCRIPTION")
            val newsPhoto = intent.getIntExtra("NEWS_PHOTO", -1)

            if (newsName == null || newsDescription == null || newsPhoto == -1) {
                throw IllegalArgumentException("Data tidak valid")
            }

            tvName.text = newsName
            tvDescription.text = newsDescription
            Glide.with(this)
                .load(newsPhoto)
                .into(imgPhoto)

            progressBar.visibility = View.GONE
        }, 1000)
    }
}
