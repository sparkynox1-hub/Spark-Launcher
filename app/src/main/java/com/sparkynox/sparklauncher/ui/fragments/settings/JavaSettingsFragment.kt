package com.sparkynox.sparklauncher.ui.fragments.settings

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.sparkynox.sparklauncher.databinding.FragmentJavaSettingsBinding
import com.sparkynox.sparklauncher.utils.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JavaSettingsFragment : Fragment() {

    private var _binding: FragmentJavaSettingsBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var prefs: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJavaSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // RAM slider
        binding.sliderRam.apply {
            valueFrom = 512f
            valueTo = 8192f
            stepSize = 256f
            value = prefs.allocatedRam.toFloat()
            addOnChangeListener { _, value, _ ->
                prefs.allocatedRam = value.toInt()
                updateRamLabel(value.toInt())
            }
        }
        updateRamLabel(prefs.allocatedRam)

        // Java version spinner
        val javaVersions = listOf("8", "11", "17", "21")
        binding.spinnerJavaVersion.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            javaVersions
        )
        binding.spinnerJavaVersion.setSelection(javaVersions.indexOf(prefs.javaVersion))
        binding.spinnerJavaVersion.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p: android.widget.AdapterView<*>?, v: View?, pos: Int, id: Long) {
                    prefs.javaVersion = javaVersions[pos]
                }
                override fun onNothingSelected(p: android.widget.AdapterView<*>?) {}
            }

        // GC type
        val gcTypes = listOf("G1GC", "ZGC", "Shenandoah", "Serial", "Parallel")
        binding.spinnerGcType.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, gcTypes
        )
        binding.spinnerGcType.setSelection(gcTypes.indexOf(prefs.gcType).coerceAtLeast(0))
        binding.spinnerGcType.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p: android.widget.AdapterView<*>?, v: View?, pos: Int, id: Long) {
                    prefs.gcType = gcTypes[pos]
                    updateDefaultArgs()
                }
                override fun onNothingSelected(p: android.widget.AdapterView<*>?) {}
            }

        // Custom JVM args
        binding.etJavaArgs.setText(prefs.javaArguments)
        binding.btnSaveArgs.setOnClickListener {
            prefs.javaArguments = binding.etJavaArgs.text.toString().trim()
            binding.btnSaveArgs.text = "Saved ✓"
        }

        // Reset to defaults
        binding.btnResetArgs.setOnClickListener {
            binding.etJavaArgs.setText(PreferencesManager.DEFAULT_JAVA_ARGS)
            prefs.javaArguments = PreferencesManager.DEFAULT_JAVA_ARGS
        }
    }

    private fun updateRamLabel(ram: Int) {
        val gb = ram / 1024.0
        binding.tvRamValue.text = if (gb >= 1) "%.1f GB".format(gb) else "$ram MB"
    }

    private fun updateDefaultArgs() {
        val gc = prefs.gcType
        val gcArg = when (gc) {
            "G1GC" -> "-XX:+UseG1GC -XX:MaxGCPauseMillis=20"
            "ZGC" -> "-XX:+UseZGC"
            "Shenandoah" -> "-XX:+UseShenandoahGC"
            "Serial" -> "-XX:+UseSerialGC"
            "Parallel" -> "-XX:+UseParallelGC"
            else -> "-XX:+UseG1GC"
        }
        val base = PreferencesManager.DEFAULT_JAVA_ARGS
            .replace(Regex("-XX:\\+Use\\w+GC(\\s+-XX:MaxGCPauseMillis=\\d+)?"), gcArg)
        binding.etJavaArgs.setText(base)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
