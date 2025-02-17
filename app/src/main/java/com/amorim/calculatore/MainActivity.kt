package com.amorim.calculatore

import Calculator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.amorim.calculatore.ui.theme.CalculatoreTheme

/**
 * MainActivity is the entry point of the application.
 * It sets the content to display the Calculator composable as the initial screen.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Apply your custom theme
            CalculatoreTheme {
                // Load the Calculator composable as the home screen
                Calculator()
            }
        }
    }
}