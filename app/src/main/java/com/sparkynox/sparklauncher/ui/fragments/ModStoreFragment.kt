package com.sparkynox.sparklauncher.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.sparkynox.sparklauncher.R
import com.sparkynox.sparklauncher.data.models.ModSource
import com.sparkynox.sparklauncher.databinding.FragmentModStoreBinding
import com.sparkynox.sparklauncher.ui.adapters.ModAdapter
import com.sparkynox.sparklauncher.ui.viewmodels.ModStoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModStoreFragment : Fragment() {

    private var _binding: FragmentModStoreBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ModStoreViewModel by viewModels()

    private lateinit var modAdapter: ModAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabs()
        setupSearch()
        setupRecyclerView()
        setupCategoryChips()
        observeViewModel()
    }

    private fun setupTabs() {
        // Source tabs: All | Modrinth | CurseForge
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("All"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Modrinth"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("CurseForge"))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val source = when (tab?.position) {
                    1 -> ModSource.MODRINTH
                    2 -> ModSource.CURSEFORGE
                    else -> ModSource.ALL
                }
                viewModel.setSource(source)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.search(it) }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) viewModel.loadFeatured()
                return false
            }
        })
    }

    private fun setupRecyclerView() {
        modAdapter = ModAdapter(
            onDownload = { mod ->
                // Show instance selector dialog before downloading
                showInstanceSelector(mod)
            },
            onInfo = { mod ->
                ModDetailBottomSheet.newInstance(mod)
                    .show(parentFragmentManager, "mod_detail")
            }
        )
        binding.rvMods.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = modAdapter
            // Performance optimizations
            setHasFixedSize(true)
            recycledViewPool.setMaxRecycledViews(0, 20)
        }
    }

    private fun setupCategoryChips() {
        val categories = listOf("Mods", "Resource Packs", "Shaders", "Modpacks", "Plugins")
        categories.forEach { cat ->
            val chip = com.google.android.material.chip.Chip(requireContext()).apply {
                text = cat
                isCheckable = true
                setOnCheckedChangeListener { _, checked ->
                    if (checked) viewModel.setCategory(cat.lowercase())
                }
            }
            binding.chipGroupCategories.addView(chip)
        }
    }

    private fun showInstanceSelector(mod: com.sparkynox.sparklauncher.data.models.Mod) {
        val instances = viewModel.getInstances()
        if (instances.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Create a game instance first!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val names = instances.map { it.name }.toTypedArray()
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Install to which instance?")
            .setItems(names) { _, idx ->
                viewModel.downloadMod(mod, instances[idx])
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun observeViewModel() {
        viewModel.mods.observe(viewLifecycleOwner) { mods ->
            modAdapter.submitList(mods)
            binding.shimmerLayout.hideShimmer()
            binding.shimmerLayout.visibility = View.GONE
            binding.rvMods.visibility = View.VISIBLE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.shimmerLayout.startShimmer()
                binding.rvMods.visibility = View.GONE
            }
        }

        viewModel.downloadProgress.observe(viewLifecycleOwner) { progress ->
            if (progress != null) {
                binding.downloadProgressBar.visibility = View.VISIBLE
                binding.downloadProgressBar.progress = progress.percent
                binding.tvDownloadStatus.text = "Downloading: ${progress.name} (${progress.percent}%)"
            } else {
                binding.downloadProgressBar.visibility = View.GONE
                binding.tvDownloadStatus.text = ""
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { err ->
            err?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
