package com.sparkynox.sparklauncher.ui.fragments.settings

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.sparkynox.sparklauncher.databinding.FragmentRendererSettingsBinding
import com.sparkynox.sparklauncher.utils.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RendererSettingsFragment : Fragment() {

    private var _binding: FragmentRendererSettingsBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var prefs: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRendererSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val renderers = listOf(
            "opengles2" to "OpenGL ES 2.0 (Compatible, slow)",
            "opengles3" to "OpenGL ES 3.0 (Recommended)",
            "vulkan_zink" to "Vulkan via Zink (Experimental)",
            "virgl" to "VirGL (VirtualGL)"
        )

        val displayNames = renderers.map { it.second }
        binding.spinnerRenderer.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            displayNames
        )

        val currentIdx = renderers.indexOfFirst { it.first == prefs.renderer }.coerceAtLeast(0)
        binding.spinnerRenderer.setSelection(currentIdx)
        binding.spinnerRenderer.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p: android.widget.AdapterView<*>?, v: View?, pos: Int, id: Long) {
                    prefs.renderer = renderers[pos].first
                    updateRendererHint(renderers[pos].first)
                }
                override fun onNothingSelected(p: android.widget.AdapterView<*>?) {}
            }

        updateRendererHint(prefs.renderer)

        // VirGL toggle
        binding.switchVirgl.isChecked = prefs.useVirGL
        binding.switchVirgl.setOnCheckedChangeListener { _, checked -> prefs.useVirGL = checked }

        // Zink toggle
        binding.switchZink.isChecked = prefs.useZink
        binding.switchZink.setOnCheckedChangeListener { _, checked -> prefs.useZink = checked }

        // Custom resolution
        binding.switchCustomRes.isChecked = prefs.customResolutionEnabled
        binding.layoutCustomRes.visibility = if (prefs.customResolutionEnabled) View.VISIBLE else View.GONE
        binding.switchCustomRes.setOnCheckedChangeListener { _, checked ->
            prefs.customResolutionEnabled = checked
            binding.layoutCustomRes.visibility = if (checked) View.VISIBLE else View.GONE
        }

        binding.etResW.setText(prefs.customResolutionW.toString())
        binding.etResH.setText(prefs.customResolutionH.toString())

        binding.btnSaveRes.setOnClickListener {
            prefs.customResolutionW = binding.etResW.text.toString().toIntOrNull() ?: 854
            prefs.customResolutionH = binding.etResH.text.toString().toIntOrNull() ?: 480
            binding.btnSaveRes.text = "Saved ✓"
        }
    }

    private fun updateRendererHint(renderer: String) {
        binding.tvRendererHint.text = when (renderer) {
            "opengles2" -> "⚠️ Best compatibility, but slowest. Use for very old devices."
            "opengles3" -> "✅ Best balance of compatibility and performance. Recommended."
            "vulkan_zink" -> "⚡ Best performance on Vulkan-capable devices. May be unstable."
            "virgl" -> "🔬 Experimental VirtualGL backend. For advanced users only."
            else -> ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
