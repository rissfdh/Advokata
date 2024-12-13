package com.example.projectadvocata.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projectadvocata.MainActivity
import com.example.projectadvocata.R
import com.example.projectadvocata.data.pref.UserPreference
import com.example.projectadvocata.data.pref.dataStore
import com.example.projectadvocata.ui.login.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {

    private lateinit var signUpButton: Button
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        supportActionBar?.hide()

        userPreference = UserPreference.getInstance(applicationContext.dataStore)
        signUpButton = findViewById(R.id.signupButton)
        signUpButton.visibility = View.GONE

        checkLoginStatus()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        signUpButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun checkLoginStatus() {
        lifecycleScope.launch {
            val user = userPreference.getSession().first()

            if (user.isLoggedIn) {
                // Jika sudah login, sembunyikan tombol "Get Started"
                signUpButton.visibility = View.GONE
                // Navigasi ke MainActivity setelah beberapa detik (opsional)
                Thread.sleep(1000)
                navigateToMain()
            }else  {
                signUpButton.visibility = View.VISIBLE
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
