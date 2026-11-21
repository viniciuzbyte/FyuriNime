package com.fyurinime.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fyurinime.app.domain.model.Anime
import com.fyurinime.app.ui.theme.FyuriGold
import com.fyurinime.app.ui.theme.FyuriOnSurfaceMuted
import com.fyurinime.app.ui.theme.FyuriSurface

/** Ukuran diatur dari luar lewat [modifier] (mis. width + height, atau fillMaxWidth + aspectRatio). */
@Composable
fun AnimeCard(
    anime: Anime,
    onClick: (Anime) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(22.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(FyuriSurface)
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(Color.White.copy(alpha = 0.35f), Color.White.copy(alpha = 0.04f))
                ),
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
                        0.45f to Color.Transparent,
                        1f to Color(0xF20B0B12)
                    )
                )
        )
        anime.score?.let { score ->
            GlassChip(
                text = "%.1f".format(score),
                icon = Icons.Rounded.Star,
                iconTint = FyuriGold,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                text = anime.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                lineHeight = 17.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            val sub = listOfNotNull(anime.year?.toString(), anime.genres.firstOrNull())
                .joinToString(" • ")
            if (sub.isNotEmpty()) {
                Text(
                    text = sub,
                    color = FyuriOnSurfaceMuted,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
