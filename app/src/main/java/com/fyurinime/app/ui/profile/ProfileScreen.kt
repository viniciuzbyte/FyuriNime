package com.fyurinime.app.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fyurinime.app.domain.model.Achievement
import com.fyurinime.app.ui.theme.FyuriGold
import com.fyurinime.app.ui.theme.FyuriOrange
import com.fyurinime.app.ui.theme.FyuriSurface

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLoggedOut: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val progress = state.progress

    val xpIntoLevel = progress.xp % 100
    val levelProgressFraction = (xpIntoLevel / 100f).coerceIn(0f, 1f)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = state.username,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "${progress.rank.label} • Level ${progress.level}",
                color = FyuriOrange,
                style = MaterialTheme.typography.titleMedium
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = FyuriSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "XP: ${progress.xp}", fontWeight = FontWeight.SemiBold)
                    Box(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { levelProgressFraction },
                        modifier = Modifier.fillMaxWidth(),
                        color = FyuriOrange,
                        trackColor = FyuriSurface
                    )
                    Box(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Total waktu nonton: ${progress.totalWatchMinutes} menit",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        item {
            Text(text = "Achievements", style = MaterialTheme.typography.titleMedium)
        }

        items(state.achievements) { achievement ->
            AchievementRow(achievement)
        }

        item {
            Button(
                onClick = { viewModel.logout(onLoggedOut) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FyuriSurface)
            ) {
                Text("Keluar")
            }
        }
    }
}

@Composable
private fun AchievementRow(achievement: Achievement) {
    Card(
        colors = CardDefaults.cardColors(containerColor = FyuriSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (achievement.unlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                contentDescription = null,
                tint = if (achievement.unlocked) FyuriGold else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
            Box(modifier = Modifier.height(0.dp))
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = achievement.type.title,
                    fontWeight = FontWeight.SemiBold,
                    color = if (achievement.unlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Text(
                    text = achievement.type.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
