package com.sparkynox.sparklauncher.ui.fragments.settings

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.sparkynox.sparklauncher.R
import com.sparkynox.sparklauncher.ui.activities.AuthActivity
import com.sparkynox.sparklauncher.utils.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountSettingsFragment : Fragment() {
    @Inject lateinit var prefs: PreferencesManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
        }
        val tvType = TextView(requireContext()).apply {
            text = "Account type: ${prefs.accountType}"
            textSize = 16f
            setTextColor(0xFFF0F0F5.toInt())
        }
        val tvUser = TextView(requireContext()).apply {
            text = if (prefs.accountType == "cracked") "Username: ${prefs.crackedUsername}"
                   else "Microsoft account"
            textSize = 14f
            setTextColor(0xFF9090A0.toInt())
            setPadding(0, 8, 0, 32)
        }
        val btnSwitch = MaterialButton(requireContext()).apply {
            text = "Switch Account"
            setOnClickListener {
                prefs.microsoftToken = ""
                prefs.crackedUsername = "Player"
                startActivity(Intent(requireContext(), AuthActivity::class.java))
                requireActivity().finish()
            }
        }
        root.addView(tvType)
        root.addView(tvUser)
        root.addView(btnSwitch)
        return root
    }
}
