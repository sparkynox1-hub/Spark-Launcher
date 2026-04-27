package com.sparkynox.sparklauncher.ui.fragments.settings

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.sparkynox.sparklauncher.databinding.FragmentControlsSettingsBinding
import com.sparkynox.sparklauncher.utils.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ControlsSettingsFragment : Fragment() {

    private var _binding: FragmentControlsSettingsBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var prefs: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentControlsSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Controls profiles
        val profiles = listOf("Default", "Compact", "Controller", "Custom")
        binding.spinnerControlsProfile.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            profiles
        )
        val idx = profiles.indexOfFirst {
            it.lowercase() == prefs.controlsProfile
        }.coerceAtLeast(0)
        binding.spinnerControlsProfile.setSelection(idx)
        binding.spinnerControlsProfile.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p: android.widget.AdapterView<*>?, v: View?, pos: Int, id: Long) {
                    prefs.controlsProfile = profiles[pos].lowercase()
                }
                override fun onNothingSelected(p: android.widget.AdapterView<*>?) {}
            }

        // Mouse speed
        binding.sliderMouseSpeed.apply {
            valueFrom = 0.1f
            valueTo = 5.0f
            stepSize = 0.1f
            value = prefs.mouseSpeed
            addOnChangeListener { _, value, _ ->
                prefs.mouseSpeed = value
                binding.tvMouseSpeed.text = "%.1f×".format(value)
            }
        }
        binding.tvMouseSpeed.text = "%.1f×".format(prefs.mouseSpeed)

        // Virtual mouse
        binding.switchVirtualMouse.isChecked = prefs.virtualMouseEnabled
        binding.switchVirtualMouse.setOnCheckedChangeListener { _, checked ->
            prefs.virtualMouseEnabled = checked
        }

        // Gyroscope
        binding.switchGyro.isChecked = prefs.enableGyroscope
        binding.switchGyro.setOnCheckedChangeListener { _, checked ->
            prefs.enableGyroscope = checked
        }

        // Haptic feedback
        binding.switchHaptic.isChecked = prefs.enableHapticFeedback
        binding.switchHaptic.setOnCheckedChangeListener { _, checked ->
            prefs.enableHapticFeedback = checked
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
