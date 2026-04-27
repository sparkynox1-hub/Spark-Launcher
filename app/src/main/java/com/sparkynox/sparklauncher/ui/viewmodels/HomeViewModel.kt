package com.sparkynox.sparklauncher.ui.viewmodels

import androidx.lifecycle.*
import com.sparkynox.sparklauncher.data.models.GameInstance
import com.sparkynox.sparklauncher.data.repository.InstanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val instanceRepository: InstanceRepository
) : ViewModel() {

    val instances: LiveData<List<GameInstance>> =
        instanceRepository.getInstancesFlow().asLiveData()

    private val _selectedInstance = MutableLiveData<GameInstance?>()
    val selectedInstance: LiveData<GameInstance?> = _selectedInstance

    fun selectInstance(instance: GameInstance) {
        _selectedInstance.value = instance
    }

    fun deleteInstance(instance: GameInstance) {
        viewModelScope.launch {
            instanceRepository.delete(instance)
            if (_selectedInstance.value?.id == instance.id) {
                _selectedInstance.value = null
            }
        }
    }
}
