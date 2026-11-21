package com.fyurinime.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fyurinime.app.ui.navigation.Routes
import com.fyurinime.app.ui.theme.FyuriBackground
import com.fyurinime.app.ui.theme.FyuriOrange

private data class NavItem(val route: String, val label: String, val icon: ImageVector)

private val navItems = listOf(
    NavItem(Routes.HOME, "Beranda", Icons.Rounded.Home),
    NavItem(Routes.SEARCH, "Jelajah", Icons.Rounded.Search),
    NavItem(Routes.PROFILE, "Profil", Icons.Rounded.Person)
)

/** Navbar bawah melayang bergaya kaca (liquid glass). */
@Composable
fun FyuriBottomBar(
    currentRoute: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = 32.dp, vertical = 14.dp)
            .fillMaxWidth()
            .height(68.dp)
            .glass(RoundedCornerShape(34.dp), strong = true)
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navItems.forEach { item ->
            val selected = currentRoute == item.route
            val bg by animateColorAsState(
                targetValue = if (selected) FyuriOrange else Color.Transparent,
                label = "navbg"
            )
            val tint by animateColorAsState(
                targetValue = if (selected) FyuriBackground else Color.White.copy(alpha = 0.7f),
                label = "navtint"
            )
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(bg)
                    .clickable { onSelect(item.route) }
                    .padding(
                        horizontal = if (selected) 18.dp else 14.dp,
                        vertical = 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(item.icon, contentDescription = item.label, tint = tint)
                AnimatedVisibility(visible = selected) {
                    Text(
                        text = "  ${item.label}",
                        color = tint,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
