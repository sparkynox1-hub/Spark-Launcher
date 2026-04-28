package com.sparkynox.sparklauncher.ui.fragments.settings

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.sparkynox.sparklauncher.utils.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DownloadSettingsFragment : Fragment() {
    @Inject lateinit var prefs: PreferencesManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
        }

        val tvThreads = TextView(requireContext()).apply {
            text = "Download threads: ${prefs.downloadThreads}"
            textSize = 16f
            setTextColor(0xFFF0F0F5.toInt())
        }
        val seekThreads = SeekBar(requireContext()).apply {
            max = 8
            progress = prefs.downloadThreads
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                    val t = progress.coerceAtLeast(1)
                    prefs.downloadThreads = t
                    tvThreads.text = "Download threads: $t"
                }
                override fun onStartTrackingTouch(sb: SeekBar?) {}
                override fun onStopTrackingTouch(sb: SeekBar?) {}
            })
        }

        val cbAutoDeps = CheckBox(requireContext()).apply {
            text = "Auto-install dependencies"
            isChecked = prefs.autoInstallDeps
            setTextColor(0xFFF0F0F5.toInt())
            setOnCheckedChangeListener { _, checked -> prefs.autoInstallDeps = checked }
        }

        root.addView(tvThreads)
        root.addView(seekThreads)
        root.addView(cbAutoDeps)
        return root
    }
}
