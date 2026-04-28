package com.sparkynox.sparklauncher.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.sparkynox.sparklauncher.R
import com.sparkynox.sparklauncher.databinding.ActivitySettingsBinding
import com.sparkynox.sparklauncher.theme.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    @Inject lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.background = themeManager.getBackground()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Settings"
        }
        binding.toolbar.setTitleTextColor(themeManager.getAccentColor())

        // Set up settings navigation
        setupSettingsCategories()
    }

    private fun setupSettingsCategories() {
        binding.apply {
            // Java / JVM Settings
            categoryJava.setOnClickListener {
                openCategory("Java & JVM")
            }
            // Renderer Settings
            categoryRenderer.setOnClickListener {
                openCategory("Renderer")
            }
            // Controls
            categoryControls.setOnClickListener {
                openCategory("Controls")
            }
            // Theme
            categoryTheme.setOnClickListener {
                openCategory("Theme")
            }
            // Account
            categoryAccount.setOnClickListener {
                openCategory("Account")
            }
            // Performance
            categoryPerformance.setOnClickListener {
                openCategory("Performance")
            }
            // Download
            categoryDownload.setOnClickListener {
                openCategory("Download")
            }
            // About
            categoryAbout.setOnClickListener {
                openCategory("About")
            }
        }
    }

    private fun openCategory(category: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings_container,
                SettingsCategoryFragment.newInstance(category))
            .addToBackStack(category)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (!supportFragmentManager.popBackStackImmediate()) onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
