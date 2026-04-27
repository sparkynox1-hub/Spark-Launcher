package com.sparkynox.sparklauncher.ui.viewmodels

import androidx.lifecycle.*
import com.sparkynox.sparklauncher.data.models.*
import com.sparkynox.sparklauncher.data.repository.InstanceRepository
import com.sparkynox.sparklauncher.data.repository.ModRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModStoreViewModel @Inject constructor(
    private val modRepository: ModRepository,
    private val instanceRepository: InstanceRepository
) : ViewModel() {

    private val _mods = MutableLiveData<List<Mod>>()
    val mods: LiveData<List<Mod>> = _mods

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _downloadProgress = MutableLiveData<DownloadProgress?>()
    val downloadProgress: LiveData<DownloadProgress?> = _downloadProgress

    private var currentSource = ModSource.ALL
    private var currentCategory = ModCategory.MODS
    private var currentQuery = ""
    private var searchJob: Job? = null

    init {
        loadFeatured()
    }

    fun loadFeatured() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = modRepository.getFeatured()
                _mods.value = result
            } catch (e: Exception) {
                _error.value = "Failed to load mods: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun search(query: String) {
        searchJob?.cancel()
        currentQuery = query
        searchJob = viewModelScope.launch {
            delay(300) // Debounce
            _isLoading.value = true
            try {
                val result = modRepository.search(
                    query = query,
                    source = currentSource,
                    category = currentCategory
                )
                _mods.value = result
            } catch (e: Exception) {
                _error.value = "Search failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setSource(source: ModSource) {
        currentSource = source
        if (currentQuery.isNotEmpty()) search(currentQuery)
        else loadFeatured()
    }

    fun setCategory(cat: String) {
        currentCategory = when (cat) {
            "mods" -> ModCategory.MODS
            "resource packs" -> ModCategory.RESOURCE_PACKS
            "shaders" -> ModCategory.SHADERS
            "modpacks" -> ModCategory.MODPACKS
            "plugins" -> ModCategory.PLUGINS
            else -> ModCategory.MODS
        }
        if (currentQuery.isNotEmpty()) search(currentQuery)
        else loadFeatured()
    }

    fun downloadMod(mod: Mod, instance: GameInstance) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Get download URL based on source
                // This is simplified; real impl fetches specific version
                _downloadProgress.value = DownloadProgress(mod.name, 0, 0, 0)
                // TODO: Integrate DownloadService
                _downloadProgress.value = null
            } catch (e: Exception) {
                _error.value = "Download failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getInstances(): List<GameInstance> =
        instanceRepository.getInstancesSync()
}
