package com.sparkynox.sparklauncher.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.sparkynox.sparklauncher.R
import com.sparkynox.sparklauncher.databinding.FragmentHomeBinding
import com.sparkynox.sparklauncher.ui.activities.GameActivity
import com.sparkynox.sparklauncher.ui.activities.SettingsActivity
import com.sparkynox.sparklauncher.ui.viewmodels.HomeViewModel
import com.sparkynox.sparklauncher.utils.PreferencesManager
import com.sparkynox.sparklauncher.utils.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    @Inject lateinit var prefs: PreferencesManager
    @Inject lateinit var themeManager: ThemeManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupInstancesList()
        observeViewModel()
    }

    private fun setupUI() {
        // Greeting with username
        val username = if (prefs.accountType == "cracked")
            prefs.crackedUsername else "Player"

        binding.tvGreeting.text = "Welcome, $username ✨"
        binding.tvSubtitle.text = "Ready to play?"

        // Anime mascot based on theme
        if (prefs.animeEffectsEnabled && themeManager.currentTheme.isAnime) {
            binding.animeMascot.visibility = View.VISIBLE
            // Load theme-specific mascot
            Glide.with(this)
                .asGif()
                .load(getMascotRes())
                .into(binding.animeMascot)
        }

        // Play button
        binding.btnPlay.setOnClickListener {
            val instance = viewModel.selectedInstance.value
            if (instance != null) {
                startActivity(Intent(requireContext(), GameActivity::class.java).apply {
                    putExtra("instance_id", instance.id)
                })
            } else {
                binding.btnPlay.apply {
                    animate().translationX(-10f).setDuration(50).withEndAction {
                        animate().translationX(10f).setDuration(50).withEndAction {
                            animate().translationX(0f).setDuration(50).start()
                        }.start()
                    }.start()
                }
            }
        }

        // Settings shortcut
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        // Theme badge
        binding.tvThemeName.text = "🎨 ${themeManager.currentTheme.displayName}"
        binding.tvThemeName.setTextColor(themeManager.getAccentColor())
    }

    private fun getMascotRes(): Int = when (themeManager.currentTheme) {
        ThemeManager.Theme.SAKURA -> R.raw.mascot_sakura
        ThemeManager.Theme.CYBER_WAIFU -> R.raw.mascot_cyber
        ThemeManager.Theme.MIKU_TEAL -> R.raw.mascot_miku
        ThemeManager.Theme.DEMON_SLAYER -> R.raw.mascot_demon
        ThemeManager.Theme.PASTEL_IDOL -> R.raw.mascot_idol
        else -> R.raw.mascot_default
    }

    private fun setupInstancesList() {
        // RecyclerView for game instances
        binding.rvInstances.adapter = com.sparkynox.sparklauncher.ui.adapters.InstanceAdapter(
            onSelect = { instance -> viewModel.selectInstance(instance) },
            onEdit = { instance -> showInstanceEditor(instance) },
            onDelete = { instance -> viewModel.deleteInstance(instance) }
        )
    }

    private fun showInstanceEditor(instance: com.sparkynox.sparklauncher.data.models.GameInstance) {
        // Show bottom sheet for editing instance
    }

    private fun observeViewModel() {
        viewModel.instances.observe(viewLifecycleOwner) { instances ->
            (binding.rvInstances.adapter as com.sparkynox.sparklauncher.ui.adapters.InstanceAdapter)
                .submitList(instances)

            binding.tvNoInstances.visibility =
                if (instances.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.selectedInstance.observe(viewLifecycleOwner) { instance ->
            binding.btnPlay.isEnabled = instance != null
            binding.tvSelectedVersion.text = instance?.versionName ?: "No version selected"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
