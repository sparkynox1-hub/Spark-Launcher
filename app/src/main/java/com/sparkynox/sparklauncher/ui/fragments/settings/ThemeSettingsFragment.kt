package com.sparkynox.sparklauncher.ui.fragments.settings

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sparkynox.sparklauncher.theme.ThemeManager
import com.sparkynox.sparklauncher.ui.adapters.ThemeAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ThemeSettingsFragment : Fragment() {
    @Inject lateinit var themeManager: ThemeManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val ctx = requireContext()
        val root = ScrollView(ctx)
        val ll = LinearLayout(ctx).apply { orientation=LinearLayout.VERTICAL; setPadding(32,32,32,32) }
        root.addView(ll)

        fun section(title: String) = TextView(ctx).apply {
            text=title; textSize=16f; setTextColor(0xFFF0F0F5.toInt())
            setPadding(0,24,0,12)
        }

        fun rvThemes(themes: List<ThemeManager.Theme>): RecyclerView = RecyclerView(ctx).apply {
            layoutManager = GridLayoutManager(ctx, 2)
            adapter = ThemeAdapter(themes, themeManager.currentTheme) { theme ->
                themeManager.applyTheme(theme)
                requireActivity().window.decorView.background = themeManager.getBackground()
                (adapter as ThemeAdapter).currentTheme = theme
                adapter?.notifyDataSetChanged()
            }
        }

        ll.addView(section("✨ Anime Themes"))
        ll.addView(rvThemes(ThemeManager.Theme.animeThemes()))
        ll.addView(section("🎮 Standard Themes"))
        ll.addView(rvThemes(ThemeManager.Theme.standardThemes()))

        val cbAnime = CheckBox(ctx).apply {
            text="Anime Effects (animated mascots)"
            isChecked = true
            setTextColor(0xFFF0F0F5.toInt())
            setPadding(0,24,0,0)
        }
        ll.addView(cbAnime)
        return root
    }
}
