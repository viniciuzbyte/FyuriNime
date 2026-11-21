package com.fyurinime.app.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.fyurinime.app.ui.components.FyuriLoadingScreen

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateNext: (startDestination: String) -> Unit
) {
    LaunchedEffect(Unit) {
        val destination = viewModel.resolveStartDestination()
        onNavigateNext(destination)
    }

    FyuriLoadingScreen(message = "Menyiapkan dunia anime-mu...")
}
