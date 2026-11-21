package com.fyurinime.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fyurinime.app.data.datastore.SessionManager
import com.fyurinime.app.data.repository.UserRepository
import com.fyurinime.app.domain.model.Achievement
import com.fyurinime.app.domain.model.UserProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val username: String = "",
    val progress: UserProgress = UserProgress(),
    val achievements: List<Achievement> = emptyList()
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        combine(
            sessionManager.username,
            userRepository.observeProgress(),
            userRepository.observeAchievements()
        ) { username, progress, achievements ->
            ProfileUiState(username = username ?: "Rookie", progress = progress, achievements = achievements)
        }.onEach { _uiState.value = it }.launchIn(viewModelScope)
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            sessionManager.logout()
            onDone()
        }
    }
}
