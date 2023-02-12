package com.example.fuzzyelectric

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.fuzzyelectric.ui.FuzzyElectricApp
import com.example.fuzzyelectric.ui.theme.FuzzyElectricTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FuzzyElectricTheme {
                FuzzyElectricApp()
            }
        }
    }
}


