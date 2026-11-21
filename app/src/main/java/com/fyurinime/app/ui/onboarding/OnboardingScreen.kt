package com.fyurinime.app.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fyurinime.app.ui.theme.FyuriOrange

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onFinished: () -> Unit
) {
    var selected by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Kenalan dulu, yuk!",
            style = MaterialTheme.typography.headlineMedium,
            color = FyuriOrange
        )
        Text(
            text = "Kamu tahu Fyuri Nime dari mana?",
            style = MaterialTheme.typography.titleMedium
        )

        Column(modifier = Modifier.height(16.dp)) {}

        LazyColumn(
            modifier = Modifier.weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(viewModel.sourceOptions) { option ->
                val isSelected = option == selected
                OutlinedButton(
                    onClick = { selected = option },
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (isSelected) {
                        ButtonDefaults.outlinedButtonColors(containerColor = FyuriOrange.copy(alpha = 0.15f))
                    } else {
                        ButtonDefaults.outlinedButtonColors()
                    }
                ) {
                    Text(option)
                }
            }
        }

        Column(modifier = Modifier.height(16.dp)) {}

        Button(
            onClick = {
                selected?.let { viewModel.selectSource(it, onFinished) }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selected != null,
            colors = ButtonDefaults.buttonColors(containerColor = FyuriOrange)
        ) {
            Text("Lanjut")
        }
    }
}
