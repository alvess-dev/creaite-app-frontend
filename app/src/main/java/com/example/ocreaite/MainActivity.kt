package com.example.ocreaite

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.ocreaite.navigation.AppNavGraph
import com.example.ocreaite.ui.theme.OcreaiteTheme
import com.example.ocreaite.utils.GoogleSignInHelper
import com.example.ocreaite.utils.GoogleSignInManager

class MainActivity : ComponentActivity() {

    lateinit var googleSignInHelper: GoogleSignInHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "=== MainActivity onCreate ===")
        Log.d("MainActivity", "Activity instance: $this")

        // Inicializar Google Sign In Helper (duas formas para garantir)
        googleSignInHelper = GoogleSignInHelper(this)
        GoogleSignInManager.initialize(this)

        Log.d("MainActivity", "googleSignInHelper initialized: ${::googleSignInHelper.isInitialized}")
        Log.d("MainActivity", "GoogleSignInManager initialized: ${GoogleSignInManager.isInitialized()}")

        setContent {
            OcreaiteTheme {
                val navController = rememberNavController()
                Log.d("MainActivity", "Setting up navigation with activity: $this")
                AppNavGraph(navController = navController, activity = this@MainActivity)
            }
        }
    }
}