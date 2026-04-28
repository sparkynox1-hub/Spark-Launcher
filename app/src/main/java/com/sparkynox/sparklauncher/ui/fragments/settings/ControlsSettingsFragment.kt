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
class ControlsSettingsFragment : Fragment() {
    @Inject lateinit var prefs: PreferencesManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val ctx = requireContext()
        val root = ScrollView(ctx)
        val ll = LinearLayout(ctx).apply { orientation=LinearLayout.VERTICAL; setPadding(48,48,48,48) }
        root.addView(ll)

        val tvMouseSpeed = TextView(ctx).apply { text="Mouse Speed: %.1fx".format(prefs.mouseSpeed); textSize=14f; setTextColor(0xFFF0F0F5.toInt()) }
        val sliderMouse = Slider(ctx).apply {
            valueFrom=0.1f; valueTo=5f; stepSize=0.1f; value=prefs.mouseSpeed
            addOnChangeListener { _,v,_ -> prefs.mouseSpeed=v; tvMouseSpeed.text="Mouse Speed: %.1fx".format(v) }
        }

        fun cb(label: String, checked: Boolean, onChange: (Boolean)->Unit) =
            CheckBox(ctx).apply { text=label; isChecked=checked; setTextColor(0xFFF0F0F5.toInt()); setOnCheckedChangeListener { _,v -> onChange(v) }; setPadding(0,16,0,0) }

        ll.addView(tvMouseSpeed); ll.addView(sliderMouse)
        ll.addView(cb("Virtual Mouse",    prefs.virtualMouseEnabled)  { prefs.virtualMouseEnabled=it })
        ll.addView(cb("Gyroscope",        prefs.enableGyroscope)      { prefs.enableGyroscope=it })
        ll.addView(cb("Haptic Feedback",  prefs.enableHapticFeedback) { prefs.enableHapticFeedback=it })
        return root
    }
}
