package com.sparkynox.sparklauncher.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.sparkynox.sparklauncher.R
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

        binding.bgAuth.background = themeManager.getBackground()

        setupTabs()
        observeViewModel()
    }

    private fun setupTabs() {
        // Default: cracked tab active
        setTabActive(cracked = true)

        binding.tabCracked.setOnClickListener {
            setTabActive(cracked = true)
            binding.layoutCracked.visibility = View.VISIBLE
            binding.layoutMicrosoft.visibility = View.GONE
        }

        binding.tabMicrosoft.setOnClickListener {
            setTabActive(cracked = false)
            binding.layoutMicrosoft.visibility = View.VISIBLE
            binding.layoutCracked.visibility = View.GONE
        }

        binding.btnLoginCracked.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            if (username.length < 3) {
                binding.etUsername.error = "Username must be at least 3 characters"
                return@setOnClickListener
            }
            viewModel.loginCracked(username)
        }

        binding.btnLoginMicrosoft.setOnClickListener {
            viewModel.loginMicrosoft(this)
        }
    }

    private fun setTabActive(cracked: Boolean) {
        val activeDrawable = ContextCompat.getDrawable(this, R.drawable.bg_tab_selected)
        val inactiveBg = android.graphics.Color.TRANSPARENT

        if (cracked) {
            binding.tabCracked.background = activeDrawable
            binding.tabCracked.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.tabMicrosoft.setBackgroundColor(inactiveBg)
            binding.tabMicrosoft.setTextColor(ContextCompat.getColor(this, R.color.text_secondary))
        } else {
            binding.tabMicrosoft.background = activeDrawable
            binding.tabMicrosoft.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.tabCracked.setBackgroundColor(inactiveBg)
            binding.tabCracked.setTextColor(ContextCompat.getColor(this, R.color.text_secondary))
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
