package com.example.projectadvocata.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.projectadvocata.MainActivity
import com.example.projectadvocata.ui.register.RegisterActivity
import com.example.projectadvocata.databinding.ActivityLoginBinding
import com.example.projectadvocata.ui.ViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getAuthInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEmailValidation()
        setupPasswordValidation()
        setupView()
        setupAction()
        setupClickListeners()

    }
    private fun setupEmailValidation() {
        val emailEditText: EditText = binding.tietEmailInput

        emailEditText.addTextChangedListener { editable ->
            val email = editable.toString()

            val emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
            if (!email.matches(emailPattern.toRegex())) {
                binding.tietEmailInput.error = "Format email tidak valid"
            } else {
                binding.tietEmailInput.error = null
            }
        }
    }

    private fun setupPasswordValidation() {
        val passwordEditText: EditText = binding.tietPasswordInput

        passwordEditText.addTextChangedListener { editable ->
            val password = editable.toString()

            if (password.length < 8) {
                binding.tietPasswordInput.error = "Password harus minimal 8 karakter"
            } else {
                binding.tietPasswordInput.error = null
            }
        }
    }

    private fun setupClickListeners() {
        binding.tilForgotPassword.setOnClickListener {
            showToast("Fitur Reset Password akan diimplementasikan")
        }

        binding.mbGoogleLoginButton.setOnClickListener {
            showToast("Google Sign In akan diimplementasikan")
        }
        binding.tvSignupButton.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.mbLoginButton.setOnClickListener {
            val email = binding.tietEmailInput.text.toString()
            val password = binding.tietPasswordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Email dan password tidak boleh kosong")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                loginViewModel.login(email, password)
                observeViewModel()
            }
        }
    }

    private fun observeViewModel() {
        loginViewModel.isLoginSuccessful.observe(this) { isSuccessful ->
            if (isSuccessful) {
                Log.d("LoginActivity", "Login successful, navigating to MainActivity")
                AlertDialog.Builder(this).apply {
                    setTitle("Login Berhasil")
                    setMessage("Selamat datang!")
                    setPositiveButton("OK") { _, _ ->
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    create()
                    show()
                }
            }
        }

        loginViewModel.errorMessage.observe(this) { error ->
            error?.let {
                Log.e("LoginActivity", "Login error: $it")
                showToast(it)
            }
        }

        loginViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
