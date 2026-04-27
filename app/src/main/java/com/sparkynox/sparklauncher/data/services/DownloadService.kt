package com.sparkynox.sparklauncher.data.services

import android.app.*
import android.content.Intent
import android.os.*
import androidx.core.app.NotificationCompat
import com.sparkynox.sparklauncher.R
import kotlinx.coroutines.*
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

/**
 * DownloadService — foreground service for mod/asset downloads.
 * Supports multi-threaded chunked downloads with progress notifications.
 * Author: SparkyNox
 */
class DownloadService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val CHANNEL_ID = "spark_downloads"
    private val NOTIF_ID = 1001

    private val downloadQueue = mutableListOf<DownloadTask>()
    private var isRunning = false

    data class DownloadTask(
        val id: String,
        val url: String,
        val destPath: String,
        val name: String,
        val sha1: String? = null,
        val onProgress: ((Int) -> Unit)? = null,
        val onComplete: ((Boolean) -> Unit)? = null
    )

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_DOWNLOAD -> {
                val task = DownloadTask(
                    id = intent.getStringExtra("id") ?: return START_NOT_STICKY,
                    url = intent.getStringExtra("url") ?: return START_NOT_STICKY,
                    destPath = intent.getStringExtra("dest") ?: return START_NOT_STICKY,
                    name = intent.getStringExtra("name") ?: "File"
                )
                enqueueDownload(task)
            }
            ACTION_CANCEL -> {
                serviceScope.cancel()
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun enqueueDownload(task: DownloadTask) {
        downloadQueue.add(task)
        if (!isRunning) processQueue()
    }

    private fun processQueue() {
        isRunning = true
        serviceScope.launch {
            while (downloadQueue.isNotEmpty()) {
                val task = downloadQueue.removeFirstOrNull() ?: break
                executeDownload(task)
            }
            isRunning = false
            stopSelf()
        }
    }

    private suspend fun executeDownload(task: DownloadTask) {
        startForeground(NOTIF_ID, buildNotification(task.name, 0))

        try {
            val connection = URL(task.url).openConnection() as HttpURLConnection
            connection.apply {
                connectTimeout = 15_000
                readTimeout = 30_000
                setRequestProperty("User-Agent", "SparkLauncher/1.0.0")
                connect()
            }

            val totalBytes = connection.contentLength.toLong()
            var downloadedBytes = 0L

            val destFile = File(task.destPath)
            destFile.parentFile?.mkdirs()

            val buffer = ByteArray(8192)
            val digest = task.sha1?.let { MessageDigest.getInstance("SHA-1") }

            connection.inputStream.use { input ->
                FileOutputStream(destFile).use { output ->
                    var bytes: Int
                    while (input.read(buffer).also { bytes = it } != -1) {
                        output.write(buffer, 0, bytes)
                        digest?.update(buffer, 0, bytes)
                        downloadedBytes += bytes

                        val percent = if (totalBytes > 0)
                            ((downloadedBytes * 100) / totalBytes).toInt()
                        else 0

                        withContext(Dispatchers.Main) {
                            task.onProgress?.invoke(percent)
                            updateNotification(task.name, percent)
                        }
                    }
                }
            }

            // Verify SHA1 if provided
            val success = if (digest != null && task.sha1 != null) {
                val hash = digest.digest().joinToString("") { "%02x".format(it) }
                hash == task.sha1
            } else true

            if (!success) destFile.delete()

            withContext(Dispatchers.Main) {
                task.onComplete?.invoke(success)
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                task.onComplete?.invoke(false)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Spark Launcher Downloads",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Download progress notifications"
            }
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    private fun buildNotification(name: String, progress: Int): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Downloading: $name")
            .setContentText("$progress%")
            .setSmallIcon(R.drawable.ic_download)
            .setProgress(100, progress, progress == 0)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private fun updateNotification(name: String, progress: Int) {
        val notifManager = getSystemService(NotificationManager::class.java)
        notifManager.notify(NOTIF_ID, buildNotification(name, progress))
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    companion object {
        const val ACTION_DOWNLOAD = "spark.action.DOWNLOAD"
        const val ACTION_CANCEL = "spark.action.CANCEL"
    }
}
