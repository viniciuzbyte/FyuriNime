package com.fyurinime.app.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.fyurinime.app.domain.model.Anime
import com.fyurinime.app.ui.components.GlassChip
import com.fyurinime.app.ui.components.glass
import com.fyurinime.app.ui.theme.FyuriBackground
import com.fyurinime.app.ui.theme.FyuriGold
import com.fyurinime.app.ui.theme.FyuriOnSurfaceMuted

@Composable
fun AnimeDetailDialog(anime: Anime, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .heightIn(max = 640.dp)
                .glass(RoundedCornerShape(28.dp), strong = true)
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(230.dp)) {
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
                                0.3f to Color.Transparent,
                                1f to FyuriBackground.copy(alpha = 0.95f)
                            )
                        )
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .glass(RoundedCornerShape(50))
                ) {
                    Icon(Icons.Rounded.Close, contentDescription = "Tutup", tint = Color.White)
                }
                Text(
                    text = anime.title,
                    color = Color.White,
                    fontSize = 21.sp,
                    lineHeight = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(18.dp)
                )
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp)
            ) {
                Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(6.dp)) {
                    anime.score?.let {
                        GlassChip(text = "%.1f".format(it), icon = Icons.Rounded.Star, iconTint = FyuriGold)
                    }
                    anime.year?.let { GlassChip(text = it.toString()) }
                    anime.status?.let { GlassChip(text = it, accent = true) }
                }
                if (anime.genres.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = anime.genres.joinToString(" • "),
                        color = FyuriOnSurfaceMuted,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = anime.synopsis ?: "Sinopsis belum tersedia.",
                    color = Color.White.copy(alpha = 0.88f),
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
            }
        }
    }
}
