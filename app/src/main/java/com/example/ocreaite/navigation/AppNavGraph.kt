// app/src/main/java/com/example/ocreaite/navigation/AppNavGraph.kt
package com.example.ocreaite.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ocreaite.MainActivity
import com.example.ocreaite.screens.*

@Composable
fun AppNavGraph(navController: NavHostController, activity: MainActivity? = null) {
    NavHost(
        navController = navController,
        startDestination = "splash1" // ✅ Volta para o fluxo normal
    ) {
        // ==================== SPLASH & ONBOARDING ====================

        composable("splash1") {
            Splash1(navController)
        }

        composable("initial") {
            InitialScreen(navController)
        }

        composable("onboarding") {
            OnboardingScreens(navController)
        }

        // ==================== AUTH SCREENS ====================

        composable("signup") {
            SignUpScreen(navController, activity)
        }

        composable("login") {
            LoginScreen(navController)
        }

        // ==================== REGISTRATION FLOW ====================

        composable("register/step1") {
            RegisterStep1Screen(navController)
        }

        composable("register/step2") {
            RegisterStep2Screen(navController)
        }

        composable("register/step3") {
            RegisterStep3Screen(navController)
        }

        composable("register/step4") {
            RegisterStep4Screen(navController)
        }

        composable("register/step5") {
            RegisterStep5Screen(navController)
        }

        // ==================== POST-REGISTRATION ====================

        composable("interests") {
            Interests(navController)
        }

        composable("welcome/{nome}") { backStack ->
            val nome = backStack.arguments?.getString("nome") ?: "Usuário"
            WelcomeScreen(nome, navController)
        }

        // ==================== MAIN APP SCREENS ====================

        // 🏠 Wardrobe (Home/Guarda-roupa)
        composable("wardrobe") {
            WardrobeScreen(navController)
        }

        // 📅 Calendar
        composable("calendar") {
            // TODO: Implementar CalendarScreen
            WardrobeScreen(navController) // Temporário - mostra wardrobe
        }

        // 🤖 AI Assistant
        composable("ai") {
            AIAssistantScreen(navController)
        }

        // 👔 Outfit/Styling
        composable("outfit") {
            StylingScreen(navController)
        }

        // Alias para "outfit" (mesmo que styling)
        composable("styling") {
            StylingScreen(navController)
        }

        // ➕ Add Items
        composable("add") {
            AddItemsScreen(navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppNavGraph() {
    AppNavGraph(navController = rememberNavController(), activity = null)
}