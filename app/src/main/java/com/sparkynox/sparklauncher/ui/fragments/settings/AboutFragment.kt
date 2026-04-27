package com.sparkynox.sparklauncher.ui.fragments.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.sparkynox.sparklauncher.BuildConfig
import com.sparkynox.sparklauncher.databinding.FragmentAboutBinding
import com.sparkynox.sparklauncher.utils.CrashHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvVersion.text = "Version ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
        binding.tvAuthor.text = "Made with ❤️ by SparkyNox"
        binding.tvBasedOn.text = "Based on PojavLauncher & Mojo Launcher"
        binding.tvYori.text = "Part of the Yori Ecosystem"

        binding.btnGithub.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://github.com/sparkynox")))
        }

        binding.btnModrinth.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://modrinth.com/user/sparkynox")))
        }

        binding.btnClearLogs.setOnClickListener {
            CrashHandler.clearLogs()
            binding.btnClearLogs.text = "Logs cleared ✓"
        }

        val logCount = CrashHandler.getCrashLogs().size
        binding.tvCrashLogs.text = "$logCount crash log(s) saved"

        // Build info
        binding.tvBuildInfo.text = buildString {
            appendLine("Build: ${if (BuildConfig.DEBUG) "Debug" else "Release"}")
            appendLine("Package: ${BuildConfig.APPLICATION_ID}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
