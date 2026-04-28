package com.sparkynox.sparklauncher.ui.fragments.settings

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.sparkynox.sparklauncher.utils.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RendererSettingsFragment : Fragment() {
    @Inject lateinit var prefs: PreferencesManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val ctx = requireContext()
        val root = ScrollView(ctx)
        val ll = LinearLayout(ctx).apply { orientation = LinearLayout.VERTICAL; setPadding(48,48,48,48) }
        root.addView(ll)

        val renderers = listOf(
            "opengles2" to "OpenGL ES 2.0 (Compatible)",
            "opengles3" to "OpenGL ES 3.0 (Recommended)",
            "vulkan_zink" to "Vulkan via Zink (Experimental)",
            "virgl" to "VirGL (VirtualGL)"
        )
        val tvTitle = TextView(ctx).apply { text="Renderer Backend"; textSize=16f; setTextColor(0xFFF0F0F5.toInt()) }
        val spinner = Spinner(ctx).apply {
            adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, renderers.map { it.second })
            setSelection(renderers.indexOfFirst { it.first==prefs.renderer }.coerceAtLeast(0))
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) { prefs.renderer=renderers[pos].first }
                override fun onNothingSelected(p: AdapterView<*>?) {}
            }
        }

        fun switchRow(label: String, checked: Boolean, onChange: (Boolean)->Unit): CheckBox =
            CheckBox(ctx).apply { text=label; isChecked=checked; setTextColor(0xFFF0F0F5.toInt()); setOnCheckedChangeListener { _,v -> onChange(v) }; setPadding(0,16,0,0) }

        val cbVirGL  = switchRow("Enable VirGL",   prefs.useVirGL)  { prefs.useVirGL=it }
        val cbZink   = switchRow("Enable Zink",    prefs.useZink)   { prefs.useZink=it }
        val cbCustRes= switchRow("Custom Resolution", prefs.customResolutionEnabled) { prefs.customResolutionEnabled=it }

        ll.addView(tvTitle); ll.addView(spinner)
        ll.addView(cbVirGL); ll.addView(cbZink); ll.addView(cbCustRes)
        return root
    }
}
