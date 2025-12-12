// app/src/main/java/com/example/ocreaite/navigation/AppNavGraph.kt
package com.example.ocreaite.navigation

import android.net.Uri
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

        composable("wardrobe") {
            WardrobeScreen(navController)
        }

        composable("calendar") {
            WardrobeScreen(navController)
        }

        composable("ai") {
            AIAssistantScreen(navController)
        }

        composable("outfit") {
            StylingScreen(navController)
        }

        composable("styling") {
            StylingScreen(navController)
        }

        composable("add") {
            AddItemsScreen(navController)
        }

        // ✅ CORRIGIDO: Tela de edição de metadados
        composable("edit_metadata") {
            // recupera o previousBackStackEntry (onde salvamos os dados antes de navegar)
            val previousEntry = navController.previousBackStackEntry

            // usamos ArrayList<String> porque foi o que salvamos (strings das URIs)
            val uriStrings = previousEntry
                ?.savedStateHandle
                ?.get<ArrayList<String>>("imageUriStrings")
                ?: arrayListOf()

            // converte para Uri
            val imageUris: List<Uri> = uriStrings.map { Uri.parse(it) }

            // recupera os base64 (se houver)
            val imagesBase64 = previousEntry
                ?.savedStateHandle
                ?.get<ArrayList<String>>("imagesBase64")
                ?: arrayListOf()

            EditClothingMetadataScreen(
                navController = navController,
                imageUris = imageUris,
                imagesBase64 = imagesBase64
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppNavGraph() {
    AppNavGraph(navController = rememberNavController(), activity = null)
}
