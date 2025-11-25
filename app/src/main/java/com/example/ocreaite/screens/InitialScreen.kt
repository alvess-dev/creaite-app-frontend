package com.example.ocreaite.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ocreaite.R
import com.example.ocreaite.ui.theme.InterFont

@Composable
fun InitialScreen(navController: NavController) {
    // Detecta se está voltando da navegação
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val fromNavigation by savedStateHandle?.getStateFlow("from_navigation", false)
        ?.collectAsState() ?: remember { mutableStateOf(false) }

    var visible by remember { mutableStateOf(false) }

    // Debug: log para verificar o estado
    LaunchedEffect(fromNavigation) {
        println("InitialScreen - fromNavigation: $fromNavigation")
    }

    LaunchedEffect(Unit) {
        visible = true
    }

    // Limpa o flag após aplicar a animação
    LaunchedEffect(fromNavigation) {
        if (fromNavigation) {
            kotlinx.coroutines.delay(300) // Aguarda a animação terminar
            savedStateHandle?.set("from_navigation", false)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = if (fromNavigation) {
                // Animação vindo da esquerda (quando volta das telas de login/signup)
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = FastOutSlowInEasing
                    )
                )
            } else {
                // Animação inicial (só fade in, sem slide)
                fadeIn(animationSpec = tween(300))
            },
            exit = slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(250)
            ) + fadeOut(animationSpec = tween(250))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                // Conteúdo central (logo + textos)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    // Logo/Ícone
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(125.dp)
                            .padding(bottom = 24.dp)
                    )

                    // Texto principal
                    Text(
                        text = "Millions of clothing options",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = InterFont,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )

                    // Texto secundário
                    Text(
                        text = "Only in CreAite",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = InterFont,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Botões na parte inferior
                Column(
                    modifier = Modifier.padding(bottom = 40.dp)
                ) {
                    // Botão Sign up free com animação de escala
                    val signupInteractionSource = remember { MutableInteractionSource() }
                    val isSignupPressed by signupInteractionSource.collectIsPressedAsState()
                    val signupScale by animateFloatAsState(
                        targetValue = if (isSignupPressed) 0.95f else 1f,
                        animationSpec = tween(durationMillis = 100)
                    )

                    Button(
                        onClick = {
                            visible = false
                            navController.navigate("signup")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .graphicsLayer {
                                scaleX = signupScale
                                scaleY = signupScale
                            },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        ),
                        shape = RoundedCornerShape(28.dp),
                        interactionSource = signupInteractionSource
                    ) {
                        Text(
                            text = "Sign up free",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = InterFont,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botão Log in com animação de escala
                    val loginInteractionSource = remember { MutableInteractionSource() }
                    val isLoginPressed by loginInteractionSource.collectIsPressedAsState()
                    val loginScale by animateFloatAsState(
                        targetValue = if (isLoginPressed) 0.95f else 1f,
                        animationSpec = tween(durationMillis = 100)
                    )

                    Button(
                        onClick = {
                            visible = false
                            navController.navigate("login")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(
                                width = 1.5.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(28.dp)
                            )
                            .graphicsLayer {
                                scaleX = loginScale
                                scaleY = loginScale
                            },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(28.dp),
                        interactionSource = loginInteractionSource
                    ) {
                        Text(
                            text = "Log in",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = InterFont,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInitialScreen() {
    InitialScreen(navController = rememberNavController())
}