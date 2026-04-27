package com.sparkynox.sparklauncher.ui.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sparkynox.sparklauncher.R
import com.sparkynox.sparklauncher.databinding.ActivityMainBinding
import com.sparkynox.sparklauncher.utils.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply themed animated background
        binding.bgView.background = themeManager.getBackground()

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        // Accent color tint for bottom nav
        binding.bottomNav.itemIconTintList =
            android.content.res.ColorStateList.valueOf(themeManager.getAccentColor())
        binding.bottomNav.itemTextColor =
            android.content.res.ColorStateList.valueOf(themeManager.getAccentColor())
    }
}
