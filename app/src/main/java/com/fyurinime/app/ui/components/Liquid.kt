package com.fyurinime.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fyurinime.app.ui.theme.FyuriBackground
import com.fyurinime.app.ui.theme.FyuriBlue
import com.fyurinime.app.ui.theme.FyuriOrange
import com.fyurinime.app.ui.theme.FyuriViolet
import java.io.File

/** Latar "liquid": tiga bola cahaya yang bergerak pelan di belakang semua layar. */
@Composable
fun LiquidBackground(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "liquid")
    val a by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 16000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "a"
    )
    val b by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 21000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "b"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        drawRect(color = FyuriBackground)
        val w = size.width
        val h = size.height
        blob(FyuriOrange, 0.30f, Offset(w * (0.10f + 0.55f * a), h * (0.10f + 0.12f * b)), w * 0.85f)
        blob(FyuriViolet, 0.28f, Offset(w * (0.95f - 0.60f * b), h * (0.45f + 0.10f * a)), w * 0.95f)
        blob(FyuriBlue, 0.20f, Offset(w * (0.20f + 0.40f * b), h * (0.92f - 0.12f * a)), w * 0.90f)
    }
}

private fun DrawScope.blob(color: Color, alpha: Float, center: Offset, radius: Float) {
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(color.copy(alpha = alpha), Color.Transparent),
            center = center,
            radius = radius
        )
    )
}

/** Efek kaca: dasar gelap transparan + kilau gradien + garis tepi tipis. */
fun Modifier.glass(
    shape: Shape = RoundedCornerShape(22.dp),
    strong: Boolean = false
): Modifier = this
    .clip(shape)
    .background(FyuriBackground.copy(alpha = if (strong) 0.78f else 0.35f), shape)
    .background(
        Brush.linearGradient(
            listOf(
                Color.White.copy(alpha = if (strong) 0.16f else 0.09f),
                Color.White.copy(alpha = 0.02f)
            )
        ),
        shape
    )
    .border(
        width = 1.dp,
        brush = Brush.linearGradient(
            listOf(
                Color.White.copy(alpha = 0.38f),
                Color.White.copy(alpha = 0.05f),
                Color.White.copy(alpha = 0.18f)
            )
        ),
        shape = shape
    )

@Composable
fun GlassChip(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconTint: Color = Color.White,
    accent: Boolean = false
) {
    val shape = RoundedCornerShape(50)
    Row(
        modifier = modifier
            .clip(shape)
            .background(if (accent) FyuriOrange.copy(alpha = 0.92f) else Color.Black.copy(alpha = 0.38f))
            .border(1.dp, Color.White.copy(alpha = 0.22f), shape)
            .padding(horizontal = 9.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (accent) FyuriBackground else Color.White
        )
    }
}

@Composable
fun UserAvatar(
    avatarPath: String?,
    name: String,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val ring = Brush.sweepGradient(listOf(FyuriOrange, FyuriViolet, FyuriBlue, FyuriOrange))
    Box(
        modifier = modifier
            .size(size)
            .border(2.dp, ring, CircleShape)
            .padding(3.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.08f)),
        contentAlignment = Alignment.Center
    ) {
        if (avatarPath != null) {
            AsyncImage(
                model = File(avatarPath),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = name.take(1).uppercase().ifEmpty { "?" },
                color = FyuriOrange,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.4f).sp
            )
        }
    }
}
