package com.sparkynox.sparklauncher.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sparkynox.sparklauncher.databinding.ActivitySplashBinding
import com.sparkynox.sparklauncher.utils.PreferencesManager
import com.sparkynox.sparklauncher.theme.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var prefs: PreferencesManager

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Full-screen immersive
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply themed background
        binding.root.background = themeManager.getBackground()

        // Animate logo
        binding.logoImage.apply {
            alpha = 0f
            scaleX = 0.6f
            scaleY = 0.6f
            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(700)
                .start()
        }

        binding.taglineText.apply {
            alpha = 0f
            translationY = 30f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(400)
                .setDuration(500)
                .start()
        }

        lifecycleScope.launch {
            delay(1800)
            navigateNext()
        }
    }

    private fun navigateNext() {
        val dest = when {
            prefs.accountType == "microsoft" && prefs.microsoftToken.isEmpty() ->
                AuthActivity::class.java
            prefs.accountType == "cracked" && prefs.crackedUsername == "Player" ->
                AuthActivity::class.java
            else -> MainActivity::class.java
        }
        startActivity(Intent(this, dest))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
