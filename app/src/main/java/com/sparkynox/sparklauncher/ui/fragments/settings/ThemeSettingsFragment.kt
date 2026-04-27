package com.sparkynox.sparklauncher.ui.fragments.settings

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sparkynox.sparklauncher.databinding.FragmentThemeSettingsBinding
import com.sparkynox.sparklauncher.theme.ThemeManager
import com.sparkynox.sparklauncher.ui.adapters.ThemeAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ThemeSettingsFragment : Fragment() {

    private var _binding: FragmentThemeSettingsBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var themeManager: ThemeManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemeSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvSectionAnime.text = "✨ Anime Themes"
        binding.tvSectionStandard.text = "🎮 Standard Themes"

        // Anime themes grid
        binding.rvAnimeThemes.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = ThemeAdapter(
                themes = ThemeManager.Theme.animeThemes(),
                currentTheme = themeManager.currentTheme,
                onSelect = { theme ->
                    themeManager.applyTheme(theme)
                    // Refresh activity background
                    requireActivity().window.decorView.background = themeManager.getBackground()
                    (adapter as ThemeAdapter).currentTheme = theme
                    adapter?.notifyDataSetChanged()
                }
            )
        }

        // Standard themes grid
        binding.rvStandardThemes.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = ThemeAdapter(
                themes = ThemeManager.Theme.standardThemes(),
                currentTheme = themeManager.currentTheme,
                onSelect = { theme ->
                    themeManager.applyTheme(theme)
                    requireActivity().window.decorView.background = themeManager.getBackground()
                    (adapter as ThemeAdapter).currentTheme = theme
                    adapter?.notifyDataSetChanged()
                    (binding.rvAnimeThemes.adapter as ThemeAdapter).notifyDataSetChanged()
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
