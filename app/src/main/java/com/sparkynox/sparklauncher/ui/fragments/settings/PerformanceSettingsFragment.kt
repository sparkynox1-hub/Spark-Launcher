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
class PerformanceSettingsFragment : Fragment() {
    @Inject lateinit var prefs: PreferencesManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val ctx = requireContext()
        val root = ScrollView(ctx)
        val ll = LinearLayout(ctx).apply { orientation=LinearLayout.VERTICAL; setPadding(48,48,48,48) }
        root.addView(ll)

        fun cb(label: String, checked: Boolean, onChange: (Boolean)->Unit) =
            CheckBox(ctx).apply { text=label; isChecked=checked; setTextColor(0xFFF0F0F5.toInt()); setOnCheckedChangeListener { _,v -> onChange(v) }; setPadding(0,16,0,0) }

        val tvFps = TextView(ctx).apply { text=fpsLabel(prefs.maxFps); textSize=14f; setTextColor(0xFFF0F0F5.toInt()) }
        val sliderFps = Slider(ctx).apply {
            valueFrom=0f; valueTo=240f; stepSize=5f; value=prefs.maxFps.toFloat()
            addOnChangeListener { _,v,_ -> prefs.maxFps=v.toInt(); tvFps.text=fpsLabel(v.toInt()) }
        }
        val tvScale = TextView(ctx).apply { text="Game Scale: ${(prefs.gameScale*100).toInt()}%"; textSize=14f; setTextColor(0xFFF0F0F5.toInt()); setPadding(0,24,0,0) }
        val sliderScale = Slider(ctx).apply {
            valueFrom=50f; valueTo=200f; stepSize=10f; value=prefs.gameScale*100
            addOnChangeListener { _,v,_ -> prefs.gameScale=v/100f; tvScale.text="Game Scale: ${v.toInt()}%" }
        }

        ll.addView(cb("Performance Mode", prefs.enablePerformanceMode) { prefs.enablePerformanceMode=it })
        ll.addView(cb("FPS Counter",      prefs.enableFpsCounter)      { prefs.enableFpsCounter=it })
        ll.addView(cb("Limit Background", prefs.limitBackground)       { prefs.limitBackground=it })
        ll.addView(tvFps); ll.addView(sliderFps)
        ll.addView(tvScale); ll.addView(sliderScale)
        return root
    }

    private fun fpsLabel(fps: Int) = if (fps==0) "Max FPS: Unlimited" else "Max FPS: $fps"
}
