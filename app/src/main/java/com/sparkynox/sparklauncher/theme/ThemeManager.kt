package com.sparkynox.sparklauncher.theme

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.sparkynox.sparklauncher.R
import com.sparkynox.sparklauncher.utils.PreferencesManager

/**
 * ThemeManager — manages Spark Launcher built-in themes.
 * All themes use real wallpaper images bundled in-app.
 * Author: SparkyNox
 */
class ThemeManager(
    private val context: Context,
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
            description = "Angel girl pink starry sky"
        ),
        CYBER_WAIFU(
            id = "cyber_waifu",
            displayName = "Cyber Waifu",
            backgroundRes = R.drawable.bg_cyber_waifu,
            accentColor = R.color.accent_cyber,
            isAnime = true,
            description = "Blue rose girl night city"
        ),
        MIKU_TEAL(
            id = "miku_teal",
            displayName = "Miku Teal",
            backgroundRes = R.drawable.bg_miku,
            accentColor = R.color.accent_teal,
            isAnime = true,
            description = "Green flowers closeup"
        ),
        DEMON_SLAYER(
            id = "demon_slayer",
            displayName = "Demon Slayer",
            backgroundRes = R.drawable.bg_demon_slayer,
            accentColor = R.color.accent_crimson,
            isAnime = true,
            description = "Lanterns & torii gates at dusk"
        ),
        PASTEL_IDOL(
            id = "pastel_idol",
            displayName = "Pastel Idol",
            backgroundRes = R.drawable.bg_pastel_idol,
            accentColor = R.color.accent_pastel,
            isAnime = true,
            description = "Cat girl with teal halo"
        ),
        // === Standard Themes ===
        DARK_SPARK(
            id = "dark_spark",
            displayName = "Dark Spark",
            backgroundRes = R.drawable.bg_dark_spark,
            accentColor = R.color.accent_spark_orange,
            description = "Dreamy clouds anime field"
        ),
        MINECRAFT_DIRT(
            id = "minecraft_dirt",
            displayName = "Minecraft Classic",
            backgroundRes = R.drawable.bg_minecraft,
            accentColor = R.color.accent_grass_green,
            description = "Warm anime sunset street"
        ),
        NETHER(
            id = "nether",
            displayName = "Nether Realm",
            backgroundRes = R.drawable.bg_nether,
            accentColor = R.color.accent_lava,
            description = "Fire lanterns red field"
        ),
        END_VOID(
            id = "end_void",
            displayName = "The End",
            backgroundRes = R.drawable.bg_end,
            accentColor = R.color.accent_ender,
            description = "Purple cherry blossom night"
        ),
        OCEAN(
            id = "ocean",
            displayName = "Deep Ocean",
            backgroundRes = R.drawable.bg_ocean,
            accentColor = R.color.accent_ocean_blue,
            description = "Galaxy shooting stars lake"
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
