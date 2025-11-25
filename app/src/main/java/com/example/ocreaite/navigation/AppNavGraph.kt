package com.example.ocreaite.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ocreaite.screens.*

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash1"
    ) {
        composable("splash1") { Splash1(navController) }
        composable("initial") { InitialScreen(navController) }
        composable("onboarding") { OnboardingScreens(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("welcome/{nome}") { backStack ->
            val nome = backStack.arguments?.getString("nome") ?: "Usuário"
            WelcomeScreen(nome, navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppNavGraph() {
    AppNavGraph(navController = rememberNavController())
}