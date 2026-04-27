package com.sparkynox.sparklauncher.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.sparkynox.sparklauncher.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ThemeManager — manages Spark Launcher's built-in themes.
 * All themes are bundled in-app (no external imports).
 * Author: SparkyNox
 */
@Singleton
class ThemeManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefs: PreferencesManager
) {

    enum class Theme(
        val id: String,
        val displayName: String,
        val backgroundRes: Int,
        val accentColor: Int,
        val isAnime: Boolean = false,
        val description: String = ""
    ) {
        // === Anime Girls Themes ===
        SAKURA(
            id = "sakura",
            displayName = "Sakura Night",
            backgroundRes = R.drawable.bg_sakura,
            accentColor = R.color.accent_sakura,
            isAnime = true,
            description = "Cherry blossoms & soft pink glow"
        ),
        CYBER_WAIFU(
            id = "cyber_waifu",
            displayName = "Cyber Waifu",
            backgroundRes = R.drawable.bg_cyber_waifu,
            accentColor = R.color.accent_cyber,
            isAnime = true,
            description = "Neon cyberpunk anime aesthetic"
        ),
        MIKU_TEAL(
            id = "miku_teal",
            displayName = "Miku Teal",
            backgroundRes = R.drawable.bg_miku,
            accentColor = R.color.accent_teal,
            isAnime = true,
            description = "Teal & aqua vocaloid vibes"
        ),
        DEMON_SLAYER(
            id = "demon_slayer",
            displayName = "Demon Slayer",
            backgroundRes = R.drawable.bg_demon_slayer,
            accentColor = R.color.accent_crimson,
            isAnime = true,
            description = "Dark crimson & black warrior theme"
        ),
        PASTEL_IDOL(
            id = "pastel_idol",
            displayName = "Pastel Idol",
            backgroundRes = R.drawable.bg_pastel_idol,
            accentColor = R.color.accent_pastel,
            isAnime = true,
            description = "Soft pastel idol concert"
        ),
        // === Standard Themes ===
        DARK_SPARK(
            id = "dark_spark",
            displayName = "Dark Spark",
            backgroundRes = R.drawable.bg_dark_spark,
            accentColor = R.color.accent_spark_orange,
            description = "Default dark theme with spark accents"
        ),
        MINECRAFT_DIRT(
            id = "minecraft_dirt",
            displayName = "Minecraft Classic",
            backgroundRes = R.drawable.bg_minecraft,
            accentColor = R.color.accent_grass_green,
            description = "Classic Minecraft dirt & grass"
        ),
        NETHER(
            id = "nether",
            displayName = "Nether Realm",
            backgroundRes = R.drawable.bg_nether,
            accentColor = R.color.accent_lava,
            description = "Hot lava and netherrack vibes"
        ),
        END_VOID(
            id = "end_void",
            displayName = "The End",
            backgroundRes = R.drawable.bg_end,
            accentColor = R.color.accent_ender,
            description = "Purple void of The End dimension"
        ),
        OCEAN(
            id = "ocean",
            displayName = "Deep Ocean",
            backgroundRes = R.drawable.bg_ocean,
            accentColor = R.color.accent_ocean_blue,
            description = "Depths of the Minecraft ocean"
        );

        companion object {
            fun fromId(id: String): Theme =
                values().firstOrNull { it.id == id } ?: CYBER_WAIFU

            fun animeThemes(): List<Theme> = values().filter { it.isAnime }
            fun standardThemes(): List<Theme> = values().filter { !it.isAnime }
        }
    }

    var currentTheme: Theme = Theme.fromId(prefs.selectedTheme)
        private set

    fun applyTheme(theme: Theme = currentTheme) {
        currentTheme = theme
        prefs.selectedTheme = theme.id
    }

    fun getBackground(): Drawable? =
        ContextCompat.getDrawable(context, currentTheme.backgroundRes)

    fun getAccentColor(): Int =
        ContextCompat.getColor(context, currentTheme.accentColor)

    fun getAllThemes(): List<Theme> = Theme.values().toList()
}
