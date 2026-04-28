package com.sparkynox.sparklauncher.ui.fragments.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.sparkynox.sparklauncher.BuildConfig
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val ctx = requireContext()
        val root = ScrollView(ctx)
        val ll = LinearLayout(ctx).apply { orientation=LinearLayout.VERTICAL; setPadding(48,48,48,48) }
        root.addView(ll)

        fun tv(text: String, size: Float = 14f, color: Int = 0xFF9090A0.toInt()) =
            TextView(ctx).apply { this.text=text; textSize=size; setTextColor(color); setPadding(0,8,0,8) }

        ll.addView(tv("Spark Launcher", 24f, 0xFFF0F0F5.toInt()))
        ll.addView(tv("Version ${BuildConfig.VERSION_NAME}"))
        ll.addView(tv("Made with ❤️ by SparkyNox"))
        ll.addView(tv("Based on PojavLauncher & Mojo Launcher"))
        ll.addView(tv("Part of the Yori Ecosystem"))

        val btnGithub = MaterialButton(ctx).apply {
            text="GitHub"
            setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/sparkynox"))) }
        }
        val btnModrinth = MaterialButton(ctx).apply {
            text="Modrinth"
            setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://modrinth.com/user/sparkynox"))) }
        }

        ll.addView(btnGithub)
        ll.addView(btnModrinth)
        return root
    }
}
