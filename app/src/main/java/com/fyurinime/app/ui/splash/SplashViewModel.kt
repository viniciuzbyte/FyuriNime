package com.fyurinime.app.ui.splash

import androidx.lifecycle.ViewModel
import com.fyurinime.app.data.datastore.SessionManager
import com.fyurinime.app.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    suspend fun resolveStartDestination(): String {
        delay(1200) // brief splash animation window, like Crunchyroll's intro beat
        val isLoggedIn = sessionManager.isLoggedIn.first()
        if (!isLoggedIn) return Routes.LOGIN

        val hasOnboarded = sessionManager.hasOnboarded.first()
        return if (!hasOnboarded) Routes.ONBOARDING else Routes.HOME
    }
}
