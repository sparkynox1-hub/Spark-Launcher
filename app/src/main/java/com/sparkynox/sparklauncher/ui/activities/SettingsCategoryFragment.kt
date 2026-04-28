package com.sparkynox.sparklauncher.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sparkynox.sparklauncher.databinding.FragmentSettingsCategoryBinding
import com.sparkynox.sparklauncher.theme.ThemeManager
import com.sparkynox.sparklauncher.ui.fragments.settings.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsCategoryFragment : Fragment() {

    private var _binding: FragmentSettingsCategoryBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var themeManager: ThemeManager

    companion object {
        fun newInstance(category: String) = SettingsCategoryFragment().apply {
            arguments = Bundle().apply { putString("category", category) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val category = arguments?.getString("category") ?: return
        val fragment: Fragment = when (category) {
            "Java & JVM" -> JavaSettingsFragment()
            "Renderer" -> RendererSettingsFragment()
            "Controls" -> ControlsSettingsFragment()
            "Theme" -> ThemeSettingsFragment()
            "Account" -> AccountSettingsFragment()
            "Performance" -> PerformanceSettingsFragment()
            "Download" -> DownloadSettingsFragment()
            "About" -> AboutFragment()
            else -> return
        }
        childFragmentManager.beginTransaction()
            .replace(binding.settingsFragmentContainer.id, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}