package com.sparkynox.sparklauncher

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import androidx.work.Configuration
import com.sparkynox.sparklauncher.theme.ThemeManager
import com.sparkynox.sparklauncher.utils.CrashHandler
import com.sparkynox.sparklauncher.utils.PreferencesManager
import com.sparkynox.sparklauncher.utils.CrashHandler
import dagger.hilt.android.HiltAndroidApp

/**
 * Spark Launcher Application class
 * Author: SparkyNox
 */
@HiltAndroidApp
class SparkApplication : Application(), Configuration.Provider {

    companion object {
        lateinit var instance: SparkApplication
            private set

        fun context(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Install crash handler
        CrashHandler.install(this)

        // Init theme — no Hilt field injection in Application, init manually
        val prefs = PreferencesManager(this)
        ThemeManager(this, prefs).applyTheme()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}
