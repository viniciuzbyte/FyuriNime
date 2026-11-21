package com.fyurinime.app.ui.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.fyurinime.app.domain.model.Anime
import com.fyurinime.app.ui.components.AnimeCard
import com.fyurinime.app.ui.components.GlassChip
import com.fyurinime.app.ui.components.UserAvatar
import com.fyurinime.app.ui.components.glass
import com.fyurinime.app.ui.theme.FyuriBackground
import com.fyurinime.app.ui.theme.FyuriGold
import com.fyurinime.app.ui.theme.FyuriOnSurfaceMuted
import com.fyurinime.app.ui.theme.FyuriOrange

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onOpenProfile: () -> Unit,
    onOpenAnime: (Anime) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 130.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            HomeHeader(
                username = state.username,
                avatarPath = state.avatarPath,
                onOpenProfile = onOpenProfile
            )
        }

        if (state.isLoading) {
            item { GlassPlaceholder(Modifier.padding(horizontal = 20.dp).fillMaxWidth().height(230.dp)) }
            item { SkeletonRow() }
        } else {
            state.errorMessage?.let { message ->
                item { ErrorCard(message = message, onRetry = viewModel::loadHome) }
            }
            state.trending.firstOrNull()?.let { hero ->
                item { HeroCard(anime = hero, onClick = onOpenAnime) }
            }
            if (state.trending.size > 1) {
                item {
                    AnimeSection(
                        title = "Trending hari ini",
                        items = state.trending.drop(1),
                        onOpenAnime = onOpenAnime
                    )
                }
            }
            if (state.seasonNow.isNotEmpty()) {
                item {
                    AnimeSection(
                        title = "Musim ini",
                        items = state.seasonNow,
                        onOpenAnime = onOpenAnime
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(username: String, avatarPath: String?, onOpenProfile: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Fyuri Nime",
                color = FyuriOrange,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Halo, $username 👋",
                color = FyuriOnSurfaceMuted,
                fontSize = 14.sp
            )
        }
        UserAvatar(
            avatarPath = avatarPath,
            name = username,
            size = 46.dp,
            modifier = Modifier.clip(RoundedCornerShape(50)).clickable(onClick = onOpenProfile)
        )
    }
}

@Composable
private fun HeroCard(anime: Anime, onClick: (Anime) -> Unit) {
    val shape = RoundedCornerShape(28.dp)
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(240.dp)
            .clip(shape)
            .border(
                1.dp,
                Brush.linearGradient(listOf(Color.White.copy(alpha = 0.4f), Color.White.copy(alpha = 0.05f))),
                shape
            )
            .clickable { onClick(anime) }
    ) {
        AsyncImage(
            model = anime.imageUrl,
            contentDescription = anime.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.25f to Color.Transparent,
                        1f to FyuriBackground.copy(alpha = 0.95f)
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(18.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                GlassChip(text = "TRENDING", accent = true)
                anime.score?.let {
                    GlassChip(text = "%.1f".format(it), icon = Icons.Rounded.Star, iconTint = FyuriGold)
                }
                anime.genres.firstOrNull()?.let { GlassChip(text = it) }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = anime.title,
                color = Color.White,
                fontSize = 22.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
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
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(50))
                    .background(FyuriOrange)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(items) { anime ->
                AnimeCard(
                    anime = anime,
                    onClick = onOpenAnime,
                    modifier = Modifier.width(150.dp).height(215.dp)
                )
            }
        }
    }
}

@Composable
private fun ErrorCard(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .glass()
            .padding(16.dp)
    ) {
        Text(text = message, color = Color(0xFFFF8A8A), fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Coba lagi",
            color = FyuriBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(FyuriOrange)
                .clickable(onClick = onRetry)
                .padding(horizontal = 18.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun SkeletonRow() {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        repeat(3) {
            GlassPlaceholder(Modifier.width(150.dp).height(215.dp))
        }
    }
}

@Composable
private fun GlassPlaceholder(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val pulse by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "pulse"
    )
    Box(modifier = modifier.alpha(pulse).glass(RoundedCornerShape(22.dp)))
}
