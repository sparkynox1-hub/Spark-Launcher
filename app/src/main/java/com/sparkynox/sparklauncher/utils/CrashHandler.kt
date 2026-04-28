package com.sparkynox.sparklauncher.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import com.sparkynox.sparklauncher.BuildConfig
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * CrashHandler — saves crash logs to storage for debugging.
 * Author: SparkyNox
 */
object CrashHandler : Thread.UncaughtExceptionHandler {

    private var defaultHandler: Thread.UncaughtExceptionHandler? = null
    private lateinit var context: Context

    fun install(context: Context) {
        this.context = context.applicationContext
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            saveCrashLog(throwable)
        } catch (_: Exception) { /* Don't crash the crash handler */ }
        defaultHandler?.uncaughtException(thread, throwable)
    }

    private fun saveCrashLog(throwable: Throwable) {
        val sw = StringWriter()
        throwable.printStackTrace(PrintWriter(sw))

        val report = buildString {
            appendLine("=== Spark Launcher Crash Report ===")
            appendLine("Author: SparkyNox")
            appendLine("Version: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
            appendLine("Time: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())}")
            appendLine("Device: ${Build.MANUFACTURER} ${Build.MODEL}")
            appendLine("Android: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
            appendLine("ABI: ${Build.SUPPORTED_ABIS.joinToString()}")
            appendLine()
            appendLine("=== Stack Trace ===")
            appendLine(sw.toString())
        }

        val logDir = File(context.getExternalFilesDir(null), "crash_logs")
        logDir.mkdirs()
        val logFile = File(logDir, "crash_${System.currentTimeMillis()}.txt")
        logFile.writeText(report)
    }

    fun getCrashLogs(): List<File> {
        val logDir = File(context.getExternalFilesDir(null), "crash_logs")
        return logDir.listFiles()?.sortedByDescending { it.lastModified() } ?: emptyList()
    }

    fun clearLogs() {
        val logDir = File(context.getExternalFilesDir(null), "crash_logs")
        logDir.deleteRecursively()
    }
}
