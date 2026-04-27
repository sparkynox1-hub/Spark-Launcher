package com.sparkynox.sparklauncher.ui.viewmodels

import android.app.Activity
import androidx.lifecycle.*
import com.microsoft.identity.client.*
import com.microsoft.identity.client.exception.MsalException
import com.sparkynox.sparklauncher.data.repository.AuthRepository
import com.sparkynox.sparklauncher.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val prefs: PreferencesManager
) : ViewModel() {

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _loginState = MutableLiveData<LoginState>(LoginState.Idle)
    val loginState: LiveData<LoginState> = _loginState

    fun loginCracked(username: String) {
        // Validate username
        val clean = username.trim()
        if (clean.length !in 3..16) {
            _loginState.value = LoginState.Error("Username must be 3-16 characters")
            return
        }
        if (!clean.matches(Regex("[a-zA-Z0-9_]+"))) {
            _loginState.value = LoginState.Error("Only letters, numbers, and underscores allowed")
            return
        }

        _loginState.value = LoginState.Loading
        prefs.accountType = "cracked"
        prefs.crackedUsername = clean
        _loginState.value = LoginState.Success
    }

    fun loginMicrosoft(activity: Activity) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                authRepository.loginMicrosoft(activity) { result ->
                    when (result) {
                        is AuthRepository.MsAuthResult.Success -> {
                            prefs.accountType = "microsoft"
                            prefs.microsoftToken = result.accessToken
                            prefs.microsoftRefreshToken = result.refreshToken
                            _loginState.postValue(LoginState.Success)
                        }
                        is AuthRepository.MsAuthResult.Error -> {
                            _loginState.postValue(LoginState.Error(result.message))
                        }
                    }
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Authentication failed")
            }
        }
    }
}
