package com.example.pantryplan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pantryplan.ui.PantryPlanApp
import com.example.pantryplan.ui.rememberPantryPlanAppState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberPantryPlanAppState()

            com.example.pantryplan.core.designsystem.theme.PantryPlanTheme {
                PantryPlanApp(appState)
            }
        }
    }
}
