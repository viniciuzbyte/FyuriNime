package com.fyurinime.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fyurinime.app.data.datastore.SessionManager
import com.fyurinime.app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value, errorMessage = null)
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, errorMessage = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    /**
     * NOTE: this is a local-only account flow (no remote auth backend wired yet).
     * Credentials are not transmitted anywhere; they're used only to personalize
     * the on-device profile. Plug in a real identity provider later if needed.
     */
    fun register(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.username.isBlank() || state.email.isBlank() || state.password.length < 6) {
            _uiState.value = state.copy(errorMessage = "Lengkapi semua kolom (password min 6 karakter)")
            return
        }
        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true)
            userRepository.createLocalAccount(state.username, state.email)
            sessionManager.login(state.username, state.email)
            _uiState.value = state.copy(isLoading = false)
            onSuccess()
        }
    }

    fun login(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Email dan password wajib diisi")
            return
        }
        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true)
            sessionManager.login(username = state.username.ifBlank { state.email.substringBefore("@") }, email = state.email)
            _uiState.value = state.copy(isLoading = false)
            onSuccess()
        }
    }
}
