package com.example.projectadvocata.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectadvocata.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSave.setOnClickListener {
            saveProfile()
        }

        binding.fab.setOnClickListener {
            editProfilePhoto()
        }
    }

    private fun saveProfile() {
        val name = binding.etName.text.toString()
        val city = binding.etCity.text.toString()

        if (name.isNotEmpty() && city.isNotEmpty()) {
            showToast("Profile saved: Name - $name, City - $city")
        } else {
            showToast("Please fill out all fields")
        }
    }

    private fun editProfilePhoto() {
        showToast("Edit Profile Photo clicked")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
