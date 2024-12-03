package com.example.projectadvocata.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
        setContentView(R.layout.activity_welcome)

        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        signUpButton = findViewById(R.id.signupButton)

        checkLoginStatus()
        supportActionBar?.hide()
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
            val user = userPreference.getUser().first()

            if (user.isLoggedIn) {
                navigateToMain()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
