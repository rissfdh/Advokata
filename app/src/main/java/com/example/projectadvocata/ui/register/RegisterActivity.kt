package com.example.projectadvocata.ui.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projectadvocata.databinding.ActivityRegisterBinding
import com.example.projectadvocata.ui.ViewModelFactory
import com.example.projectadvocata.ui.login.LoginActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getAuthInstance(this)
    }
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupClickListeners()
        observeViewModel()
        setupAction()
        setupDatabaseFirebase()

    }
    private fun setupDatabaseFirebase() {
        database = FirebaseDatabase.getInstance().getReferenceFromUrl(
            "https://advokata-1109-default-rtdb.firebaseio.com/"
        )
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

    private fun setupClickListeners() {
        binding.tvLoginButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun setupAction() {
        binding.mbRegisterButton.setOnClickListener {
            val name = binding.tietNameInput.text.toString()
            val email = binding.tietEmailInput.text.toString()
            val password = binding.tietPasswordInput.text.toString()
            val confirmPassword = binding.tietConfirmPassword.text.toString()

            if (password != confirmPassword) {
                showToast("Passwords do not match")
            } else if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                registerViewModel.register(name, email, password)
            } else {
                showToast("Please fill all fields")
            }
        }

        binding.tvLoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun observeViewModel() {
        registerViewModel.isRegisterSuccessful.observe(this) { isSuccessful ->
            if (isSuccessful) {
                showToast("Registration successful!")
                navigateToLogin()
            }
        }

        registerViewModel.errorMessage.observe(this) { error ->
            error?.let {
                showErrorDialog(it)
                registerViewModel.clearErrorMessage()
            }
        }

        registerViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun navigateToLogin() {
        Intent(this, LoginActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
        finish()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(message)
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}

