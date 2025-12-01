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
        startDestination = "splash1"
    ) {
        // Splash screens
        composable("splash1") {
            Splash1(navController)
        }

        // Initial/Onboarding flow
        composable("initial") {
            InitialScreen(navController)
        }

        composable("onboarding") {
            OnboardingScreens(navController)
        }

        composable("signup") {
            SignUpScreen(navController, activity)
        }

        // Auth screens
        composable("login") {
            LoginScreen(navController)
        }

        // Registration multi-step flow
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

        // Onboarding screens after registration
        composable("interests") {
            Interests(navController)
        }

        // Main app screens
        composable("wardrobe") {
            WardrobeScreen(navController)
        }

        composable("styling") {
            StylingScreen(navController)
        }

        composable("add") {
            AddItemsScreen(navController)
        }

        composable("calendar") {
            // TODO: Implementar CalendarScreen
            WardrobeScreen(navController) // Temporário
        }

        composable("ai") {
            // TODO: Implementar AIScreen
            WardrobeScreen(navController) // Temporário
        }

        composable("outfit") {
            StylingScreen(navController)
        }

        // Welcome screen (removido - não é mais necessário)
        composable("welcome/{nome}") { backStack ->
            val nome = backStack.arguments?.getString("nome") ?: "Usuário"
            WelcomeScreen(nome, navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppNavGraph() {
    AppNavGraph(navController = rememberNavController(), activity = null)
}