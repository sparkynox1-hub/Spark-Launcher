package com.sparkynox.sparklauncher.ui.fragments.settings

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.sparkynox.sparklauncher.databinding.FragmentPerformanceSettingsBinding
import com.sparkynox.sparklauncher.utils.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PerformanceSettingsFragment : Fragment() {

    private var _binding: FragmentPerformanceSettingsBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var prefs: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerformanceSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Performance mode (low-power optimizations)
        binding.switchPerfMode.isChecked = prefs.enablePerformanceMode
        binding.switchPerfMode.setOnCheckedChangeListener { _, checked ->
            prefs.enablePerformanceMode = checked
        }

        // FPS counter
        binding.switchFpsCounter.isChecked = prefs.enableFpsCounter
        binding.switchFpsCounter.setOnCheckedChangeListener { _, checked ->
            prefs.enableFpsCounter = checked
        }

        // Max FPS slider
        binding.sliderMaxFps.apply {
            valueFrom = 0f
            valueTo = 240f
            stepSize = 5f
            value = prefs.maxFps.toFloat()
            addOnChangeListener { _, value, _ ->
                prefs.maxFps = value.toInt()
                binding.tvMaxFpsValue.text = if (value == 0f) "Unlimited" else "${value.toInt()} FPS"
            }
        }
        binding.tvMaxFpsValue.text = if (prefs.maxFps == 0) "Unlimited" else "${prefs.maxFps} FPS"

        // Limit background processing
        binding.switchLimitBg.isChecked = prefs.limitBackground
        binding.switchLimitBg.setOnCheckedChangeListener { _, checked ->
            prefs.limitBackground = checked
        }

        // Game scale
        binding.sliderGameScale.apply {
            valueFrom = 50f
            valueTo = 200f
            stepSize = 10f
            value = (prefs.gameScale * 100).toInt().toFloat()
            addOnChangeListener { _, value, _ ->
                prefs.gameScale = value / 100f
                binding.tvGameScale.text = "${value.toInt()}%"
            }
        }
        binding.tvGameScale.text = "${(prefs.gameScale * 100).toInt()}%"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
