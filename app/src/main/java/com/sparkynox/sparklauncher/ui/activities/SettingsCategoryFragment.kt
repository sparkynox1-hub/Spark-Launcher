package com.sparkynox.sparklauncher.ui.activities

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.sparkynox.sparklauncher.theme.ThemeManager
import com.sparkynox.sparklauncher.ui.fragments.settings.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsCategoryFragment : Fragment() {

    @Inject lateinit var themeManager: ThemeManager

    companion object {
        fun newInstance(category: String) = SettingsCategoryFragment().apply {
            arguments = Bundle().apply { putString("category", category) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FrameLayout(requireContext()).apply {
            id = com.sparkynox.sparklauncher.R.id.settings_fragment_container
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val category = arguments?.getString("category") ?: return
        val fragment: Fragment = when (category) {
            "Java & JVM"  -> JavaSettingsFragment()
            "Renderer"    -> RendererSettingsFragment()
            "Controls"    -> ControlsSettingsFragment()
            "Theme"       -> ThemeSettingsFragment()
            "Account"     -> AccountSettingsFragment()
            "Performance" -> PerformanceSettingsFragment()
            "Download"    -> DownloadSettingsFragment()
            "About"       -> AboutFragment()
            else          -> return
        }
        childFragmentManager.beginTransaction()
            .replace(com.sparkynox.sparklauncher.R.id.settings_fragment_container, fragment)
            .commit()
    }
}
