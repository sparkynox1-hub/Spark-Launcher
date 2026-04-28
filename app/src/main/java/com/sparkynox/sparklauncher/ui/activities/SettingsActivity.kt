package com.sparkynox.sparklauncher.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"

        binding.categoryJava.setOnClickListener { openCategory("Java & JVM") }
        binding.categoryRenderer.setOnClickListener { openCategory("Renderer") }
        binding.categoryControls.setOnClickListener { openCategory("Controls") }
        binding.categoryTheme.setOnClickListener { openCategory("Theme") }
        binding.categoryAccount.setOnClickListener { openCategory("Account") }
        binding.categoryPerformance.setOnClickListener { openCategory("Performance") }
        binding.categoryDownload.setOnClickListener { openCategory("Download") }
        binding.categoryAbout.setOnClickListener { openCategory("About") }
    }

    private fun openCategory(category: String) {
        supportFragmentManager.beginTransaction()
            .replace(com.sparkynox.sparklauncher.R.id.settings_container,
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
