package com.example.ocreaite.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ocreaite.R
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(nome: String, navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scaleAnimation"
    )

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1000),
        label = "alphaAnimation"
    )

    LaunchedEffect(Unit) {
        delay(200)
        startAnimation = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1a1a1a),
                        Color(0xFF000000)
                    )
                )
            )
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo animada
        Image(
            painter = painterResource(id = R.drawable.logosemfundo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(120.dp)
                .scale(scale)
                .padding(bottom = 48.dp),
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
        )

        // Texto Welcome
        Text(
            text = "Welcome,",
            fontSize = 24.sp,
            color = Color(0xFFCCCCCC),
            fontWeight = FontWeight.Light,
            modifier = Modifier.scale(scale)
        )

        // Nome do usuário
        Text(
            text = nome,
            fontSize = 40.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .scale(scale),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Descrição
        Text(
            text = "Your style journey begins here.\nLet's create something amazing together!",
            fontSize = 16.sp,
            color = Color(0xFFAAAAAA),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .scale(scale)
        )

        Spacer(modifier = Modifier.height(64.dp))

        // Botão Explore
        Button(
            onClick = {
                // TODO: Navegar para tela principal do app
                // navController.navigate("home")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .scale(scale),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = "Explore Now",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão Secundário
        Button(
            onClick = {
                navController.navigate("login") {
                    popUpTo("splash1") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .scale(scale),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(28.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color.White)
        ) {
            Text(
                text = "Back to Login",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWelcomeScreen() {
    WelcomeScreen("Maria Silva", rememberNavController())
}