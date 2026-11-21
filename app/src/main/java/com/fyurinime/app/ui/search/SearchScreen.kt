package com.fyurinime.app.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fyurinime.app.domain.model.Anime
import com.fyurinime.app.ui.components.AnimeCard
import com.fyurinime.app.ui.components.glass
import com.fyurinime.app.ui.theme.FyuriOnSurfaceMuted
import com.fyurinime.app.ui.theme.FyuriOrange

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onOpenAnime: (Anime) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        Text(
            text = "Jelajah",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 12.dp)
        )

        TextField(
            value = state.query,
            onValueChange = viewModel::onQueryChange,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .glass(RoundedCornerShape(26.dp)),
            placeholder = { Text("Cari judul anime...", color = FyuriOnSurfaceMuted) },
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = FyuriOrange) },
            singleLine = true,
            shape = RoundedCornerShape(26.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = FyuriOrange,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading -> CircularProgressIndicator(
                    color = FyuriOrange,
                    modifier = Modifier.align(Alignment.Center)
                )
                state.errorMessage != null -> CenterMessage(state.errorMessage.orEmpty())
                state.query.isBlank() -> CenterMessage("Ketik judul anime untuk mulai mencari ✨")
                state.results.isEmpty() -> CenterMessage("Tidak ada hasil untuk \"${state.query}\"")
                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 130.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.results) { anime ->
                        AnimeCard(
                            anime = anime,
                            onClick = onOpenAnime,
                            modifier = Modifier.fillMaxWidth().aspectRatio(0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.BoxScope.CenterMessage(text: String) {
    Text(
        text = text,
        color = FyuriOnSurfaceMuted,
        textAlign = TextAlign.Center,
        fontSize = 15.sp,
        modifier = Modifier
            .align(Alignment.Center)
            .padding(horizontal = 32.dp)
    )
}
