package com.fyurinime.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fyurinime.app.domain.model.Anime
import com.fyurinime.app.ui.components.AnimeCard
import com.fyurinime.app.ui.components.FyuriLoadingScreen
import com.fyurinime.app.ui.theme.FyuriBackground
import com.fyurinime.app.ui.theme.FyuriOrange

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onOpenProfile: () -> Unit,
    onOpenAnime: (Anime) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Fyuri Nime",
                        color = FyuriOrange,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = onOpenProfile) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profil")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FyuriBackground)
            )
        },
        containerColor = FyuriBackground
    ) { padding ->
        if (state.isLoading) {
            FyuriLoadingScreen(message = "Menyiapkan tontonan untukmu...")
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                state.errorMessage?.let {
                    Text(text = it, modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.error)
                }

                AnimeSection(title = "Trending hari ini", items = state.trending, onOpenAnime = onOpenAnime)
                Box(modifier = Modifier.height(16.dp))
                AnimeSection(title = "Musim ini", items = state.seasonNow, onOpenAnime = onOpenAnime)
            }
        }
    }
}

@Composable
private fun AnimeSection(
    title: String,
    items: List<Anime>,
    onOpenAnime: (Anime) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Box(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { anime ->
                AnimeCard(anime = anime, onClick = onOpenAnime)
            }
        }
    }
}
