package com.sparkynox.sparklauncher.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * PreferencesManager — stores all launcher settings.
 * Covers all PojavLauncher + MojoLauncher settings plus Spark extras.
 * Author: SparkyNox
 */
class PreferencesManager(
    context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("spark_prefs", Context.MODE_PRIVATE)

    // ===== THEME =====
    var selectedTheme: String
        get() = prefs.getString("theme", "cyber_waifu") ?: "cyber_waifu"
        set(v) = prefs.edit().putString("theme", v).apply()

    // ===== ACCOUNT =====
    var accountType: String  // "microsoft" | "cracked"
        get() = prefs.getString("account_type", "cracked") ?: "cracked"
        set(v) = prefs.edit().putString("account_type", v).apply()

    var crackedUsername: String
        get() = prefs.getString("cracked_username", "Player") ?: "Player"
        set(v) = prefs.edit().putString("cracked_username", v).apply()

    var microsoftToken: String
        get() = prefs.getString("ms_token", "") ?: ""
        set(v) = prefs.edit().putString("ms_token", v).apply()

    var microsoftRefreshToken: String
        get() = prefs.getString("ms_refresh_token", "") ?: ""
        set(v) = prefs.edit().putString("ms_refresh_token", v).apply()

    // ===== JAVA / JVM SETTINGS (PojavLauncher compatible) =====
    var javaArguments: String
        get() = prefs.getString("java_args", DEFAULT_JAVA_ARGS) ?: DEFAULT_JAVA_ARGS
        set(v) = prefs.edit().putString("java_args", v).apply()

    var allocatedRam: Int  // MB
        get() = prefs.getInt("allocated_ram", 1024)
        set(v) = prefs.edit().putInt("allocated_ram", v).apply()

    var javaVersion: String  // "8", "17", "21"
        get() = prefs.getString("java_version", "17") ?: "17"
        set(v) = prefs.edit().putString("java_version", v).apply()

    var gcType: String  // "G1GC", "ZGC", "Shenandoah", "Serial"
        get() = prefs.getString("gc_type", "G1GC") ?: "G1GC"
        set(v) = prefs.edit().putString("gc_type", v).apply()

    // ===== RENDERER SETTINGS =====
    var renderer: String  // "opengles2", "opengles3", "vulkan_zink", "virgl"
        get() = prefs.getString("renderer", "opengles3") ?: "opengles3"
        set(v) = prefs.edit().putString("renderer", v).apply()

    var useVirGL: Boolean
        get() = prefs.getBoolean("use_virgl", false)
        set(v) = prefs.edit().putBoolean("use_virgl", v).apply()

    var useZink: Boolean
        get() = prefs.getBoolean("use_zink", false)
        set(v) = prefs.edit().putBoolean("use_zink", v).apply()

    // ===== GAME SETTINGS =====
    var gamePath: String
        get() = prefs.getString("game_path", "") ?: ""
        set(v) = prefs.edit().putString("game_path", v).apply()

    var customResolutionEnabled: Boolean
        get() = prefs.getBoolean("custom_res", false)
        set(v) = prefs.edit().putBoolean("custom_res", v).apply()

    var customResolutionW: Int
        get() = prefs.getInt("res_w", 854)
        set(v) = prefs.edit().putInt("res_w", v).apply()

    var customResolutionH: Int
        get() = prefs.getInt("res_h", 480)
        set(v) = prefs.edit().putInt("res_h", v).apply()

    var gameScale: Float  // 0.5 to 2.0
        get() = prefs.getFloat("game_scale", 1.0f)
        set(v) = prefs.edit().putFloat("game_scale", v).apply()

    // ===== CONTROLS =====
    var controlsProfile: String
        get() = prefs.getString("controls_profile", "default") ?: "default"
        set(v) = prefs.edit().putString("controls_profile", v).apply()

    var mouseSpeed: Float
        get() = prefs.getFloat("mouse_speed", 1.0f)
        set(v) = prefs.edit().putFloat("mouse_speed", v).apply()

    var enableGyroscope: Boolean
        get() = prefs.getBoolean("gyro", false)
        set(v) = prefs.edit().putBoolean("gyro", v).apply()

    var enableHapticFeedback: Boolean
        get() = prefs.getBoolean("haptic", true)
        set(v) = prefs.edit().putBoolean("haptic", v).apply()

    var virtualMouseEnabled: Boolean
        get() = prefs.getBoolean("virtual_mouse", true)
        set(v) = prefs.edit().putBoolean("virtual_mouse", v).apply()

    // ===== PERFORMANCE =====
    var enablePerformanceMode: Boolean
        get() = prefs.getBoolean("perf_mode", false)
        set(v) = prefs.edit().putBoolean("perf_mode", v).apply()

    var enableFpsCounter: Boolean
        get() = prefs.getBoolean("fps_counter", true)
        set(v) = prefs.edit().putBoolean("fps_counter", v).apply()

    var maxFps: Int  // 0 = unlimited
        get() = prefs.getInt("max_fps", 0)
        set(v) = prefs.edit().putInt("max_fps", v).apply()

    var limitBackground: Boolean
        get() = prefs.getBoolean("limit_bg", true)
        set(v) = prefs.edit().putBoolean("limit_bg", v).apply()

    // ===== DOWNLOAD =====
    var downloadThreads: Int
        get() = prefs.getInt("dl_threads", 4)
        set(v) = prefs.edit().putInt("dl_threads", v).apply()

    var autoInstallDeps: Boolean
        get() = prefs.getBoolean("auto_deps", true)
        set(v) = prefs.edit().putBoolean("auto_deps", v).apply()

    // ===== UI / MISC =====
    var showNewsOnHome: Boolean
        get() = prefs.getBoolean("show_news", true)
        set(v) = prefs.edit().putBoolean("show_news", v).apply()

    var animeEffectsEnabled: Boolean
        get() = prefs.getBoolean("anime_effects", true)
        set(v) = prefs.edit().putBoolean("anime_effects", v).apply()

    var selectedLanguage: String
        get() = prefs.getString("language", "en") ?: "en"
        set(v) = prefs.edit().putString("language", v).apply()

    var crashReportsEnabled: Boolean
        get() = prefs.getBoolean("crash_reports", true)
        set(v) = prefs.edit().putBoolean("crash_reports", v).apply()

    companion object {
        const val DEFAULT_JAVA_ARGS =
            "-XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:+UnlockExperimentalVMOptions " +
            "-XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:G1HeapRegionSize=32M " +
            "-XX:G1MixedGCCountTarget=4 -Dfml.ignoreInvalidMinecraftCertificates=true " +
            "-Dfml.ignorePatchDiscrepancies=true"
    }
}
