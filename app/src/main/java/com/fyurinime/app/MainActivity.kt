package com.fyurinime.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fyurinime.app.ui.navigation.FyuriNavGraph
import com.fyurinime.app.ui.theme.FyuriNimeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FyuriNimeTheme {
                FyuriNavGraph()
            }
        }
    }
}
