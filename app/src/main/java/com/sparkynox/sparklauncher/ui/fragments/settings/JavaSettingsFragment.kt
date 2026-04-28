package com.sparkynox.sparklauncher.ui.fragments.settings

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import com.sparkynox.sparklauncher.utils.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JavaSettingsFragment : Fragment() {
    @Inject lateinit var prefs: PreferencesManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val ctx = requireContext()
        val root = ScrollView(ctx)
        val ll = LinearLayout(ctx).apply { orientation = LinearLayout.VERTICAL; setPadding(48,48,48,48) }
        root.addView(ll)

        // RAM label + slider
        val tvRam = TextView(ctx).apply { text = ramLabel(prefs.allocatedRam); textSize=16f; setTextColor(0xFFF0F0F5.toInt()) }
        val sliderRam = Slider(ctx).apply {
            valueFrom=512f; valueTo=8192f; stepSize=256f; value=prefs.allocatedRam.toFloat()
            addOnChangeListener { _, v, _ -> prefs.allocatedRam=v.toInt(); tvRam.text=ramLabel(v.toInt()) }
        }

        // Java version spinner
        val tvJava = TextView(ctx).apply { text="Java Version"; textSize=14f; setTextColor(0xFF9090A0.toInt()); setPadding(0,24,0,4) }
        val javaVersions = arrayOf("8","11","17","21")
        val spinnerJava = Spinner(ctx).apply {
            adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, javaVersions)
            setSelection(javaVersions.indexOf(prefs.javaVersion).coerceAtLeast(0))
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) { prefs.javaVersion=javaVersions[pos] }
                override fun onNothingSelected(p: AdapterView<*>?) {}
            }
        }

        // GC type spinner
        val tvGC = TextView(ctx).apply { text="Garbage Collector"; textSize=14f; setTextColor(0xFF9090A0.toInt()); setPadding(0,24,0,4) }
        val gcTypes = arrayOf("G1GC","ZGC","Shenandoah","Serial","Parallel")
        val spinnerGC = Spinner(ctx).apply {
            adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, gcTypes)
            setSelection(gcTypes.indexOf(prefs.gcType).coerceAtLeast(0))
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) { prefs.gcType=gcTypes[pos] }
                override fun onNothingSelected(p: AdapterView<*>?) {}
            }
        }

        // JVM args
        val tvArgs = TextView(ctx).apply { text="JVM Arguments"; textSize=14f; setTextColor(0xFF9090A0.toInt()); setPadding(0,24,0,4) }
        val etArgs = EditText(ctx).apply { setText(prefs.javaArguments); minLines=3; gravity=Gravity.TOP; textSize=11f }
        val btnSave = Button(ctx).apply {
            text="Save Args"
            setOnClickListener { prefs.javaArguments=etArgs.text.toString().trim(); text="Saved ✓" }
        }
        val btnReset = Button(ctx).apply {
            text="Reset to Default"
            setOnClickListener { etArgs.setText(PreferencesManager.DEFAULT_JAVA_ARGS); prefs.javaArguments=PreferencesManager.DEFAULT_JAVA_ARGS }
        }

        ll.addView(tvRam); ll.addView(sliderRam)
        ll.addView(tvJava); ll.addView(spinnerJava)
        ll.addView(tvGC); ll.addView(spinnerGC)
        ll.addView(tvArgs); ll.addView(etArgs); ll.addView(btnSave); ll.addView(btnReset)
        return root
    }

    private fun ramLabel(ram: Int): String {
        val gb = ram / 1024.0
        return if (gb >= 1) "RAM: %.1f GB".format(gb) else "RAM: $ram MB"
    }
}
