package com.fyurinime.app.ui.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fyurinime.app.data.datastore.SessionManager
import com.fyurinime.app.data.repository.UserRepository
import com.fyurinime.app.domain.model.Achievement
import com.fyurinime.app.domain.model.UserProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class ProfileUiState(
    val username: String = "",
    val bio: String = "",
    val avatarPath: String? = null,
    val progress: UserProgress = UserProgress(),
    val achievements: List<Achievement> = emptyList()
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        combine(
            sessionManager.username,
            sessionManager.bio,
            sessionManager.avatarPath,
            userRepository.observeProgress(),
            userRepository.observeAchievements()
        ) { username, bio, avatar, progress, achievements ->
            ProfileUiState(
                username = username ?: "Rookie",
                bio = bio ?: "",
                avatarPath = avatar,
                progress = progress,
                achievements = achievements
            )
        }.onEach { _uiState.value = it }.launchIn(viewModelScope)
    }

    fun saveProfile(name: String, bio: String) {
        val cleanName = name.trim()
        if (cleanName.isEmpty()) return
        viewModelScope.launch {
            sessionManager.updateProfile(cleanName, bio.trim())
            userRepository.updateUsername(cleanName)
        }
    }

    /** Salin foto pilihan ke penyimpanan internal app supaya tetap bisa dibuka setelah restart. */
    fun onAvatarPicked(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = File(context.filesDir, "avatar_${System.currentTimeMillis()}.jpg")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    file.outputStream().use { output -> input.copyTo(output) }
                } ?: return@launch
                val old = _uiState.value.avatarPath
                sessionManager.setAvatarPath(file.absolutePath)
                old?.let { File(it).delete() }
            } catch (_: Exception) {
                // abaikan: foto lama tetap dipakai
            }
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            sessionManager.logout()
            onDone()
        }
    }
}
