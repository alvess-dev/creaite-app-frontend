package com.example.ocreaite.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ocreaite.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var visible by remember { mutableStateOf(false) }
    var navigateBack by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    // Observer para navegação de volta após animação
    LaunchedEffect(navigateBack) {
        if (navigateBack) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("from_navigation", true)
            navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        // TopBar com seta
        IconButton(
            onClick = {
                visible = false
                navigateBack = true
            },
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp)
                .align(Alignment.TopStart),
            interactionSource = remember { MutableInteractionSource() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Color(0xFF121212)
            )
        }

        // Conteúdo com animação
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(250)
            ) + fadeIn(animationSpec = tween(250)),
            exit = fadeOut(animationSpec = tween(0)) // Desaparece instantaneamente
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Área central: polvo + título
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.wrapContentHeight()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "CreAite Logo",
                            modifier = Modifier.size(120.dp),
                            tint = Color(0xFF121212)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Log in to CreAite",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF121212)
                        )
                    }
                }

                // Área inferior: botões e link de signup
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Botão Continue with email
                    val emailInteractionSource = remember { MutableInteractionSource() }
                    val isEmailPressed by emailInteractionSource.collectIsPressedAsState()
                    val emailScale by animateFloatAsState(
                        targetValue = if (isEmailPressed) 0.95f else 1f,
                        animationSpec = tween(durationMillis = 100)
                    )

                    OutlinedButton(
                        onClick = { /* Navegar para tela de login com email */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .graphicsLayer {
                                scaleX = emailScale
                                scaleY = emailScale
                            },
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFF121212)
                        ),
                        border = BorderStroke(1.5.dp, Color(0xFF121212)),
                        interactionSource = emailInteractionSource
                    ) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_dialog_email),
                            contentDescription = "Email",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFF121212)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Continue with email",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botão Continue with Google
                    val googleInteractionSource = remember { MutableInteractionSource() }
                    val isGooglePressed by googleInteractionSource.collectIsPressedAsState()
                    val googleScale by animateFloatAsState(
                        targetValue = if (isGooglePressed) 0.95f else 1f,
                        animationSpec = tween(durationMillis = 100)
                    )

                    Button(
                        onClick = { /* Implementar login com Google */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .graphicsLayer {
                                scaleX = googleScale
                                scaleY = googleScale
                            },
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF121212),
                            contentColor = Color(0xFFFAFAFA)
                        ),
                        interactionSource = googleInteractionSource
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "Google",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Continue with Google",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Texto e Link para Sign up
                    var isSignUpPressed by remember { mutableStateOf(false) }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Don't have an account?",
                            fontSize = 14.sp,
                            color = Color(0xFF121212),
                            fontWeight = FontWeight.Normal
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        val signUpAlpha by animateFloatAsState(
                            targetValue = if (isSignUpPressed) 0.5f else 1f,
                            animationSpec = tween(durationMillis = 100)
                        )

                        val signUpScale by animateFloatAsState(
                            targetValue = if (isSignUpPressed) 0.95f else 1f,
                            animationSpec = tween(durationMillis = 100)
                        )

                        Text(
                            text = "Sign up",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF121212).copy(alpha = signUpAlpha),
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = signUpScale
                                    scaleY = signUpScale
                                }
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            isSignUpPressed = true
                                            val released = tryAwaitRelease()
                                            isSignUpPressed = false
                                            if (released) {
                                                navController.navigate("signup") {
                                                    popUpTo("login") { inclusive = true }
                                                }
                                            }
                                        }
                                    )
                                }
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}