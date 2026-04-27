package com.sparkynox.sparklauncher.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sparkynox.sparklauncher.databinding.ActivityAuthBinding
import com.sparkynox.sparklauncher.ui.viewmodels.AuthViewModel
import com.sparkynox.sparklauncher.utils.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.background = themeManager.getBackground()

        setupTabs()
        observeViewModel()
    }

    private fun setupTabs() {
        // Tab: Cracked (offline)
        binding.tabCracked.setOnClickListener {
            binding.tabCracked.isSelected = true
            binding.tabMicrosoft.isSelected = false
            binding.layoutCracked.visibility = View.VISIBLE
            binding.layoutMicrosoft.visibility = View.GONE
        }

        // Tab: Microsoft (official)
        binding.tabMicrosoft.setOnClickListener {
            binding.tabMicrosoft.isSelected = true
            binding.tabCracked.isSelected = false
            binding.layoutMicrosoft.visibility = View.VISIBLE
            binding.layoutCracked.visibility = View.GONE
        }

        // Login cracked
        binding.btnLoginCracked.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            if (username.length < 3) {
                binding.etUsername.error = "Username must be at least 3 characters"
                return@setOnClickListener
            }
            viewModel.loginCracked(username)
        }

        // Login Microsoft
        binding.btnLoginMicrosoft.setOnClickListener {
            viewModel.loginMicrosoft(this)
        }
    }

    private fun observeViewModel() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is AuthViewModel.LoginState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnLoginCracked.isEnabled = false
                    binding.btnLoginMicrosoft.isEnabled = false
                }
                is AuthViewModel.LoginState.Success -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is AuthViewModel.LoginState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnLoginCracked.isEnabled = true
                    binding.btnLoginMicrosoft.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }
}
