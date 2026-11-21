package com.fyurinime.app.ui.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.fyurinime.app.domain.model.Achievement
import com.fyurinime.app.ui.components.GlassChip
import com.fyurinime.app.ui.components.UserAvatar
import com.fyurinime.app.ui.components.glass
import com.fyurinime.app.ui.theme.FyuriBackground
import com.fyurinime.app.ui.theme.FyuriGold
import com.fyurinime.app.ui.theme.FyuriOnSurfaceMuted
import com.fyurinime.app.ui.theme.FyuriOrange

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLoggedOut: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val progress = state.progress
    var showEdit by remember { mutableStateOf(false) }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) viewModel.onAvatarPicked(uri)
    }
    val pickPhoto = {
        picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    val xpIntoLevel = progress.xp % 100
    val fraction by animateFloatAsState(
        targetValue = (xpIntoLevel / 100f).coerceIn(0f, 1f),
        label = "xp"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 130.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .glass(RoundedCornerShape(30.dp))
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    UserAvatar(
                        avatarPath = state.avatarPath,
                        name = state.username,
                        size = 104.dp,
                        modifier = Modifier.clip(CircleShape).clickable { pickPhoto() }
                    )
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(FyuriOrange)
                            .clickable { pickPhoto() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Rounded.PhotoCamera,
                            contentDescription = "Ganti foto",
                            tint = FyuriBackground,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = state.username,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    GlassChip(text = progress.rank.label, accent = true)
                    GlassChip(text = "Level ${progress.level}")
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = state.bio.ifBlank { "Belum ada bio. Ketuk Edit profil untuk menulis." },
                    color = FyuriOnSurfaceMuted,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(FyuriOrange)
                        .clickable { showEdit = true }
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Edit, null, tint = FyuriBackground, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit profil", color = FyuriBackground, fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatTile("${progress.xp}", "XP", Modifier.weight(1f))
                StatTile("${progress.level}", "Level", Modifier.weight(1f))
                StatTile("${progress.totalWatchMinutes}", "Menit nonton", Modifier.weight(1f))
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .glass()
                    .padding(18.dp)
            ) {
                Row {
                    Text(
                        text = "Menuju level ${progress.level + 1}",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    Text("$xpIntoLevel / 100 XP", color = FyuriOnSurfaceMuted, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction)
                            .clip(CircleShape)
                            .background(Brush.horizontalGradient(listOf(FyuriOrange, FyuriGold)))
                    )
                }
            }
        }

        item {
            Text(
                text = "Achievements",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(state.achievements) { achievement ->
            AchievementRow(achievement)
        }

        item {
            Text(
                text = "Keluar",
                color = Color(0xFFFF8A8A),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .glass(RoundedCornerShape(50))
                    .clickable { viewModel.logout(onLoggedOut) }
                    .padding(vertical = 14.dp)
            )
        }
    }

    if (showEdit) {
        EditProfileDialog(
            initialName = state.username,
            initialBio = state.bio,
            avatarPath = state.avatarPath,
            onPickPhoto = { pickPhoto() },
            onSave = { name, bio ->
                viewModel.saveProfile(name, bio)
                showEdit = false
            },
            onDismiss = { showEdit = false }
        )
    }
}

@Composable
private fun StatTile(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .glass(RoundedCornerShape(20.dp))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = FyuriOrange, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text(label, color = FyuriOnSurfaceMuted, fontSize = 12.sp)
    }
}

@Composable
private fun AchievementRow(achievement: Achievement) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .glass(RoundedCornerShape(20.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (achievement.unlocked) Icons.Rounded.CheckCircle else Icons.Rounded.Lock,
            contentDescription = null,
            tint = if (achievement.unlocked) FyuriGold else Color.White.copy(alpha = 0.35f)
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = achievement.type.title,
                fontWeight = FontWeight.SemiBold,
                color = if (achievement.unlocked) Color.White else Color.White.copy(alpha = 0.5f)
            )
            Text(
                text = achievement.type.description,
                fontSize = 13.sp,
                color = FyuriOnSurfaceMuted
            )
        }
    }
}

@Composable
private fun EditProfileDialog(
    initialName: String,
    initialBio: String,
    avatarPath: String?,
    onPickPhoto: () -> Unit,
    onSave: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var bio by remember { mutableStateOf(initialBio) }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = FyuriOrange,
        focusedBorderColor = FyuriOrange,
        unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
        focusedLabelColor = FyuriOrange,
        unfocusedLabelColor = FyuriOnSurfaceMuted
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .glass(RoundedCornerShape(28.dp), strong = true)
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Edit profil", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(16.dp))
            UserAvatar(
                avatarPath = avatarPath,
                name = name,
                size = 88.dp,
                modifier = Modifier.clip(CircleShape).clickable { onPickPhoto() }
            )
            Text(
                text = "Ketuk foto untuk mengganti",
                color = FyuriOnSurfaceMuted,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 6.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it.take(20) },
                label = { Text("Nama") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it.take(120) },
                label = { Text("Bio") },
                maxLines = 3,
                shape = RoundedCornerShape(16.dp),
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(18.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Batal",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .glass(RoundedCornerShape(50))
                        .clickable(onClick = onDismiss)
                        .padding(vertical = 12.dp)
                )
                Text(
                    text = "Simpan",
                    color = FyuriBackground,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(FyuriOrange)
                        .clickable { onSave(name, bio) }
                        .padding(vertical = 12.dp)
                )
            }
        }
    }
}
