package com.sparkynox.sparklauncher

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import androidx.work.Configuration
import androidx.work.WorkManager
import com.sparkynox.sparklauncher.utils.CrashHandler
import com.sparkynox.sparklauncher.utils.ThemeManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Spark Launcher Application class
 * Author: SparkyNox
 */
@HiltAndroidApp
class SparkApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var themeManager: ThemeManager

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

        // Apply saved theme
        themeManager.applyTheme()

        // Init WorkManager with custom config
        WorkManager.initialize(
            this,
            workManagerConfiguration
        )
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
