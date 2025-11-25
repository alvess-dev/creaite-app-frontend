package com.example.ocreaite.screens
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import com.example.ocreaite.R

@Composable
fun Splash1(navController: NavController) {
    SplashAnimated(navController, Color.White, R.drawable.logosemfundo, "initial")  // Mude para "initial"
}

@Composable
fun SplashAnimated(
    navController: NavController,
    bgColor: Color,
    logo: Int,
    next: String
) {
    var startAnim by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (startAnim) 1.2f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleAnim"
    )

    val alpha by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0.6f,
        animationSpec = tween(800),
        label = "alphaAnim"
    )

    LaunchedEffect(Unit) {
        startAnim = true
        delay(3000) // 3 segundos
        navController.navigate(next) {
            popUpTo("splash1") { inclusive = true }
            launchSingleTop = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = logo),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .scale(scale)
                .graphicsLayer(alpha = alpha)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplash1() {
    Splash1(navController = rememberNavController())
}

