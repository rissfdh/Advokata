package com.example.projectadvocata.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectadvocata.R
import com.example.projectadvocata.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Gunakan ViewBinding untuk menghubungkan layout
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup aksi untuk tombol "SAVE"
        binding.btnSave.setOnClickListener {
            saveProfile()
        }

        // Setup aksi untuk tombol edit foto (opsional)
        binding.fab.setOnClickListener {
            editProfilePhoto()
        }
    }

    private fun saveProfile() {
        // Implementasi aksi untuk menyimpan profil
        val name = binding.etName.text.toString()
        val city = binding.etCity.text.toString()

        if (name.isNotEmpty() && city.isNotEmpty()) {
            // Simpan data nama dan kota (opsional: kirim ke server atau simpan di database)
            showToast("Profile saved: Name - $name, City - $city")
        } else {
            showToast("Please fill out all fields")
        }
    }

    private fun editProfilePhoto() {
        // Implementasi aksi untuk mengedit foto profil
        showToast("Edit Profile Photo clicked")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
