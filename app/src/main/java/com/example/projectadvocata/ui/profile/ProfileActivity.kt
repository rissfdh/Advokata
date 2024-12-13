package com.example.projectadvocata.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.projectadvocata.R
import com.example.projectadvocata.databinding.ActivityProfileBinding
import com.example.projectadvocata.ui.ViewModelFactory
import com.example.projectadvocata.ui.chatbot.ChatbotActivity
import com.example.projectadvocata.ui.login.LoginActivity

class ProfileActivity : AppCompatActivity() {

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        // Handle klik tombol kembali
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        observeSession()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogout.setOnClickListener {
            profileViewModel.logout()
        }
        binding.btnAboutUS.setOnClickListener {
            startActivity(Intent(this, AboutUsActivity::class.java))
        }
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
    }

    private fun observeSession() {
        profileViewModel.getSession().observe(this) { user ->
            if (user.isLoggedIn) {
                binding.tvName.text  =  user.name
                binding.tvEmail.text = user.email
            } else {
                binding.tvName.text  = getString(R.string.nama_user)
                binding.tvEmail.text = getString(R.string.emailuser_gmail_com)
            }
        }

        profileViewModel.isLoggedOut.observe(this) { isLoggedOut ->
            if (isLoggedOut) {
                Toast.makeText(this, "Logout Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
                finish()
            }
        }
    }
}