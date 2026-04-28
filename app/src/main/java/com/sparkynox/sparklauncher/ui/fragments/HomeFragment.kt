package com.sparkynox.sparklauncher.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sparkynox.sparklauncher.databinding.FragmentHomeBinding
import com.sparkynox.sparklauncher.theme.ThemeManager
import com.sparkynox.sparklauncher.ui.adapters.InstanceAdapter
import com.sparkynox.sparklauncher.ui.viewmodels.HomeViewModel
import com.sparkynox.sparklauncher.utils.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    @Inject lateinit var prefs: PreferencesManager
    @Inject lateinit var themeManager: ThemeManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val username = if (prefs.accountType == "cracked") prefs.crackedUsername else "Player"
        binding.tvGreeting.text = "Welcome, $username ✨"
        binding.tvSubtitle.text = "Ready to play?"
        binding.tvThemeName.text = "🎨 ${themeManager.currentTheme.displayName}"
        binding.tvThemeName.setTextColor(themeManager.getAccentColor())

        binding.btnPlay.setOnClickListener {
            if (viewModel.selectedInstance.value != null) {
                Toast.makeText(requireContext(), "Launching game…", Toast.LENGTH_SHORT).show()
                // TODO: launch GameActivity when JVM bridge is integrated
            } else {
                binding.btnPlay.animate().translationX(-8f).setDuration(50).withEndAction {
                    binding.btnPlay.animate().translationX(8f).setDuration(50).withEndAction {
                        binding.btnPlay.animate().translationX(0f).start()
                    }.start()
                }.start()
            }
        }

        binding.rvInstances.adapter = InstanceAdapter(
            onSelect = { viewModel.selectInstance(it) },
            onEdit   = { },
            onDelete = { viewModel.deleteInstance(it) }
        )

        viewModel.instances.observe(viewLifecycleOwner) { instances ->
            (binding.rvInstances.adapter as InstanceAdapter).submitList(instances)
            binding.tvNoInstances.visibility = if (instances.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.selectedInstance.observe(viewLifecycleOwner) { instance ->
            binding.btnPlay.isEnabled = instance != null
            binding.tvSelectedVersion.text = instance?.versionName ?: "No version selected"
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
