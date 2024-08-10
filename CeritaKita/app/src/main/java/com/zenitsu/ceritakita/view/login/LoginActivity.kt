package com.zenitsu.ceritakita.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.zenitsu.ceritakita.data.pref.UserModel
import com.zenitsu.ceritakita.view.ViewModelFactory
import com.zenitsu.ceritakita.utils.Result
import com.zenitsu.ceritakita.view.story.main.MainActivity
import com.zenitsu.ceritakita.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()

        binding.loginButton.setOnClickListener {
            val emailText = binding.emailEditText.text.toString()
            val passwordText = binding.passwordEditText.text.toString()
            viewModel.login(emailText, passwordText).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            val email = binding.emailEditText.text.toString()
                            val token = result.data.loginResult?.token ?: ""
                            Log.d(TAG, "Result.Success token: $token")
                            viewModel.saveSession(UserModel(email, token, true))
                            result.data.message?.let { showToast(it) }
                            showLoading(false)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                        is Result.Error -> {
                            showToast(result.error)
                            showLoading(false)
                        }
                    }
                }
            }
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loginProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}