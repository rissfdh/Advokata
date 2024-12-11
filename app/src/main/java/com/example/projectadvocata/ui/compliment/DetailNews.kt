package com.example.projectadvocata.ui.compliment

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.projectadvocata.R

class DetailNews : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_news)

        // Mendapatkan data dari Intent dengan key yang benar
        val newsName = intent.getStringExtra("NEWS_NAME")
        val newsDescription = intent.getStringExtra("NEWS_DESCRIPTION")
        val newsPhoto = intent.getIntExtra("NEWS_PHOTO", -1)

        // Validasi data
        if (newsName == null || newsDescription == null || newsPhoto == -1) {
            throw IllegalArgumentException("Data tidak valid")
        }

        // Mendapatkan referensi tampilan
        val imgPhoto: ImageView = findViewById(R.id.img_item_photo)
        val tvName: TextView = findViewById(R.id.tv_item_name)
        val tvDescription: TextView = findViewById(R.id.tv_item_description)

        // Menampilkan data pada tampilan
        tvName.text = newsName
        tvDescription.text = newsDescription
        Glide.with(this)
            .load(newsPhoto)  // Menampilkan gambar menggunakan Glide
            .into(imgPhoto)

        // Tombol kembali
        val backButton: ImageButton = findViewById(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }
    }
}
