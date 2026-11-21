package com.fyurinime.app.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fyurinime.app.domain.model.Anime
import com.fyurinime.app.ui.auth.LoginScreen
import com.fyurinime.app.ui.auth.RegisterScreen
import com.fyurinime.app.ui.components.FyuriBottomBar
import com.fyurinime.app.ui.components.LiquidBackground
import com.fyurinime.app.ui.detail.AnimeDetailDialog
import com.fyurinime.app.ui.home.HomeScreen
import com.fyurinime.app.ui.onboarding.OnboardingScreen
import com.fyurinime.app.ui.profile.ProfileScreen
import com.fyurinime.app.ui.search.SearchScreen
import com.fyurinime.app.ui.splash.SplashScreen
import com.fyurinime.app.ui.theme.FyuriBackground

@Composable
fun FyuriNavGraph(navController: NavHostController = rememberNavController()) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBar = currentRoute == Routes.HOME ||
        currentRoute == Routes.SEARCH ||
        currentRoute == Routes.PROFILE
    var selectedAnime by remember { mutableStateOf<Anime?>(null) }

    Box(modifier = Modifier.fillMaxSize().background(FyuriBackground)) {
        LiquidBackground()

        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier.fillMaxSize()
        ) {

            composable(Routes.SPLASH) {
                SplashScreen(
                    onNavigateNext = { destination ->
                        navController.navigate(destination) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoggedIn = {
                        navController.navigate(Routes.ONBOARDING) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onGoToRegister = { navController.navigate(Routes.REGISTER) }
                )
            }

            composable(Routes.REGISTER) {
                RegisterScreen(
                    onRegistered = {
                        navController.navigate(Routes.ONBOARDING) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onGoToLogin = { navController.popBackStack() }
                )
            }

            composable(Routes.ONBOARDING) {
                OnboardingScreen(
                    onFinished = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.ONBOARDING) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    onOpenProfile = {
                        navController.navigate(Routes.PROFILE) {
                            popUpTo(Routes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onOpenAnime = { selectedAnime = it }
                )
            }

            composable(Routes.SEARCH) {
                SearchScreen(onOpenAnime = { selectedAnime = it })
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    onLoggedOut = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = showBar,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it }
        ) {
            FyuriBottomBar(
                currentRoute = currentRoute,
                onSelect = { route ->
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo(Routes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        selectedAnime?.let { anime ->
            AnimeDetailDialog(anime = anime, onDismiss = { selectedAnime = null })
        }
    }
}
