package com.fyurinime.app.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fyurinime.app.data.datastore.SessionManager
import com.fyurinime.app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
) : ViewModel() {

    val sourceOptions = listOf(
        "Teman", "Google Search", "Twitch", "Discord", "TikTok", "Instagram", "YouTube", "Lainnya"
    )

    fun selectSource(source: String, onDone: () -> Unit) {
        viewModelScope.launch {
            sessionManager.completeOnboarding(source)
            userRepository.setSource(source)
            onDone()
        }
    }
}
