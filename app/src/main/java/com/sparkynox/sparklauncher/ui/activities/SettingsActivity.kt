package com.sparkynox.sparklauncher.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.sparkynox.sparklauncher.R
import com.sparkynox.sparklauncher.theme.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    @Inject lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        window.decorView.background = themeManager.getBackground()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"

        // Wire up each settings category row by ID
        mapOf(
            R.id.category_java        to "Java & JVM",
            R.id.category_renderer    to "Renderer",
            R.id.category_controls    to "Controls",
            R.id.category_theme       to "Theme",
            R.id.category_account     to "Account",
            R.id.category_performance to "Performance",
            R.id.category_download    to "Download",
            R.id.category_about       to "About"
        ).forEach { (viewId, category) ->
            findViewById<View>(viewId)?.setOnClickListener { openCategory(category) }
        }
    }

    private fun openCategory(category: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings_container, SettingsCategoryFragment.newInstance(category))
            .addToBackStack(category)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (!supportFragmentManager.popBackStackImmediate())
                onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
